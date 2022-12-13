package dplatonov.portal.service;

import dplatonov.portal.component.FileGenerator;
import dplatonov.portal.component.KafkaProducer;
import dplatonov.portal.dao.ScannerConfigsCutoffDao;
import dplatonov.portal.dao.ScannerConfigsDao;
import dplatonov.portal.dao.ScannerPreviewDao;
import dplatonov.portal.entity.ScannerConfigCutoff;
import dplatonov.portal.entity.ScannerConfigs;
import dplatonov.portal.entity.ScannerPreview;
import dplatonov.portal.enums.FileType;
import dplatonov.portal.enums.MsgStatus;
import dplatonov.portal.mapper.ScannerConfigsMapper;
import dplatonov.portal.messages.Action;
import dplatonov.portal.payload.POJOResponsePayload;
import dplatonov.portal.payload.ScannerConfigsPayload;
import dplatonov.portal.payload.ScannerPreviewPayload;
import dplatonov.portal.utils.StringUtils;
import dplatonov.portal.validator.ScannerConfigsValidator;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScannerConfigsService {

  private final ScannerConfigsDao dao;
  private final ScannerConfigsValidator validator;

  private final ScannerConfigsMapper mapper;
  private final KafkaProducer producer;
  private final Map<Long, String> statuses = new HashMap<>();
  private final ScannerPreviewDao scannerPreviewDao;
  private final FileGenerator fileGenerator;
  private final ScannerConfigsCutoffDao scannerConfigsCutoffDao;

  @Value("${scanner.topic1}")
  private String topic;

  public List<ScannerConfigsPayload> get() throws IllegalAccessException {
    return mapper.mapConfigsEntityToPayload(validator.getAllActiveScannerConfigs());
  }

  public ScannerConfigsPayload create(ScannerConfigsPayload payload) {
    return mapper.mapConfigEntityToPayload(dao.save(mapper.mapConfigPayloadToEntity(payload)));
  }

  @Transactional
  public ScannerConfigsPayload update(ScannerConfigsPayload payload) throws IllegalAccessException {
    ScannerConfigs configs = mapper.mapConfigPayloadToEntity(payload);
    ScannerConfigs config = validator.getScannerConfigForOwner(configs.getId());

    ScannerConfigs result = ScannerConfigs.builder()
        .id(payload.getId())
        .name(payload.getName())
        .url(payload.getUrl())
        .scannerSteps(mapper.mapStepsPayloadToEntity(payload.getScannerSteps()))
        .active(payload.isActive())
        .user(config.getUser())
        .build();

    return mapper.mapConfigEntityToPayload(dao.save(result));
  }

  @Transactional
  public ScannerConfigsPayload markAsDelete(ScannerConfigsPayload payload)
      throws IllegalAccessException {
    ScannerConfigs configs = validator.getScannerConfigForOwner(payload.getId());
    configs.setActive(false);
    configs.getScannerSteps().forEach(step -> step.setActive(false));
    return mapper.mapConfigEntityToPayload(dao.save(configs));
  }

  @Transactional
  public ScannerConfigsPayload start(ScannerConfigsPayload payload) throws IllegalAccessException {
    ScannerConfigs configs = validator.getScannerConfigForOwner(payload.getId());
    statuses.put(configs.getId(), MsgStatus.START.getStatus());
    String message = getMessage(configs, MsgStatus.START);
    producer.send(topic, message);

    return mapper.mapConfigEntityToPayload(dao.save(configs));
  }

  @Transactional
  public ScannerConfigsPayload stop(ScannerConfigsPayload payload) throws IllegalAccessException {
    ScannerConfigs configs = validator.getScannerConfigForOwner(payload.getId());
    String message = getMessage(configs, MsgStatus.STOP);
    producer.send(topic, message);

    return mapper.mapConfigEntityToPayload(dao.save(configs));
  }

  public POJOResponsePayload checkStatus(Long id) throws IllegalAccessException {
    return POJOResponsePayload.builder().string(statuses.get(id)).build();
  }

  public void updateStatus(Action action) {
    statuses.put(action.getScannerId(), action.getStatus().getStatus());
  }

  @Transactional
  public List<ScannerPreviewPayload> getPreview(Long scannerId) throws IllegalAccessException {
    ScannerConfigs scannerConfigForOwner = validator.getScannerConfigForOwner(scannerId);
    if (Objects.isNull(scannerConfigForOwner)) {
      throw new IllegalAccessException();
    }
    List<ScannerPreview> previews = scannerPreviewDao.findByScannerId(scannerId);
    Optional<ScannerConfigCutoff> optional = scannerConfigsCutoffDao.findByScannerId(scannerId);
    if (!previews.isEmpty() && optional.isPresent()) {
      ScannerConfigCutoff scannerConfigCutoff = optional.get();
      return previews.stream().map(preview -> ScannerPreviewPayload.builder()
          .id(preview.getId())
          .scannerId(preview.getScannerId())
          .value(preview.getValue())
          .startDate(scannerConfigCutoff.getStartDate())
          .finishDate(scannerConfigCutoff.getStopDate())
          .build()).collect(Collectors.toList());
    } else {
      throw new IllegalAccessException();
    }
  }

  @Transactional
  public Resource createFile(Long scannerId, String strType)
      throws IllegalAccessException, IOException {
    Resource resource = null;
    ScannerConfigs scannerConfigForOwner = validator.getScannerConfigForOwner(scannerId);
    if (Objects.isNull(scannerConfigForOwner)) {
      throw new IllegalAccessException();
    }
    List<ScannerPreview> previews = scannerPreviewDao.findByScannerId(scannerId);
    Optional<ScannerConfigCutoff> optional = scannerConfigsCutoffDao.findByScannerId(scannerId);
    if (optional.isPresent()) {
      FileType type = FileType.getType(strType);
      if (Objects.requireNonNull(type) == FileType.CSV) {
        resource = fileGenerator.getCsvFile(previews, scannerConfigForOwner, optional.get());
      }
    }
    return resource;
  }

  private String getMessage(ScannerConfigs configs, MsgStatus status)
      throws IllegalAccessException {
    StringUtils<Action> utils = new StringUtils<>();
    if (Objects.nonNull(configs)) {
      Action action = Action.builder()
          .scannerId(configs.getId())
          .status(status)
          .build();
      return utils.toStringFromObject(action);
    } else {
      throw new IllegalAccessException();
    }
  }
}
