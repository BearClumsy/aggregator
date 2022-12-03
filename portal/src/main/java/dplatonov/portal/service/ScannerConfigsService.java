package dplatonov.portal.service;

import dplatonov.portal.dao.ScannerConfigsDao;
import dplatonov.portal.entity.ScannerConfigs;
import dplatonov.portal.mapper.ScannerConfigsMapper;
import dplatonov.portal.payload.ScannerConfigsPayload;
import dplatonov.portal.validator.ScannerConfigsValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScannerConfigsService {

  private final ScannerConfigsDao dao;
  private final ScannerConfigsValidator validator;

  private final ScannerConfigsMapper mapper;

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
    //TODO make functionality to send jms message via kafka to scanner

    return mapper.mapConfigEntityToPayload(dao.save(configs));
  }

  public ScannerConfigsPayload stop(ScannerConfigsPayload payload) throws IllegalAccessException {
    ScannerConfigs configs = validator.getScannerConfigForOwner(payload.getId());
    //TODO make functionality to send jms message via kafka to scanner

    return mapper.mapConfigEntityToPayload(dao.save(configs));
  }
}
