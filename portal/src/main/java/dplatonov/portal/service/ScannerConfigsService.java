package dplatonov.portal.service;

import dplatonov.portal.component.KafkaProducer;
import dplatonov.portal.dao.ScannerConfigsDao;
import dplatonov.portal.entity.ScannerConfigs;
import dplatonov.portal.enums.MsgStatus;
import dplatonov.portal.mapper.ScannerConfigsMapper;
import dplatonov.portal.messages.Action;
import dplatonov.portal.payload.ScannerConfigsPayload;
import dplatonov.portal.utils.StringUtils;
import dplatonov.portal.validator.ScannerConfigsValidator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScannerConfigsService {

  private final ScannerConfigsDao dao;
  private final ScannerConfigsValidator validator;

  private final ScannerConfigsMapper mapper;
  private final KafkaProducer producer;

  @Value("${scanner.topic}")
  private String topic;

  public List<ScannerConfigsPayload> get() throws IllegalAccessException {
    return mapper.mapConfigsEntityToPayload(validator.getAllActiveScannerConfigs());
  }

  public ScannerConfigsPayload create(ScannerConfigsPayload payload) {
    return mapper.mapConfigEntityToPayload(dao.save(mapper.mapConfigPayloadToEntity(payload)));
  }

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

  public ScannerConfigsPayload markAsDelete(ScannerConfigsPayload payload)
      throws IllegalAccessException {
    ScannerConfigs configs = validator.getScannerConfigForOwner(payload.getId());
    configs.setActive(false);
    configs.getScannerSteps().forEach(step -> step.setActive(false));
    return mapper.mapConfigEntityToPayload(dao.save(configs));
  }

  public ScannerConfigsPayload start(ScannerConfigsPayload payload) throws IllegalAccessException {
    ScannerConfigs configs = validator.getScannerConfigForOwner(payload.getId());
    String message = getMessage(configs, MsgStatus.START);
    producer.send(topic, message);

    return mapper.mapConfigEntityToPayload(dao.save(configs));
  }

  public ScannerConfigsPayload stop(ScannerConfigsPayload payload) throws IllegalAccessException {
    ScannerConfigs configs = validator.getScannerConfigForOwner(payload.getId());
    String message = getMessage(configs, MsgStatus.STOP);
    producer.send(topic, message);

    return mapper.mapConfigEntityToPayload(dao.save(configs));
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
