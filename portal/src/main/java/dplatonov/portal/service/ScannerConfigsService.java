package dplatonov.portal.service;

import dplatonov.portal.dao.ScannerConfigsDao;
import dplatonov.portal.entity.ScannerConfigs;
import dplatonov.portal.mapper.ScannerConfigsMapper;
import dplatonov.portal.payload.ScannerConfigsPayload;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScannerConfigsService {

  private final ScannerConfigsDao dao;

  private final ScannerConfigsMapper mapper;

  public List<ScannerConfigsPayload> get() {
    return mapper.mapConfigsEntityToPayload(dao.findAllByActive(true));
  }

  public ScannerConfigsPayload create(ScannerConfigsPayload payload) {
    return mapper.mapConfigEntityToPayload(dao.save(mapper.mapConfigPayloadToEntity(payload)));
  }

  public ScannerConfigsPayload update(ScannerConfigsPayload payload)
      throws IllegalArgumentException {
    ScannerConfigs configs = mapper.mapConfigPayloadToEntity(payload);
    Optional<ScannerConfigs> optional = dao.findById(configs.getId());
    if (optional.isEmpty()) {
      throw new IllegalArgumentException();
    }

    ScannerConfigs result = ScannerConfigs.builder()
        .id(payload.getId())
        .name(payload.getName())
        .url(payload.getUrl())
        .scannerSteps(mapper.mapStepsPayloadToEntity(payload.getScannerSteps()))
        .active(payload.isActive())
        .userId(optional.get().getUserId())
        .build();

    return mapper.mapConfigEntityToPayload(dao.save(result));
  }

  public ScannerConfigsPayload markAsDelete(ScannerConfigsPayload payload) {
    ScannerConfigs configs = mapper.mapConfigPayloadToEntity(payload);
    configs.setActive(false);
    configs.getScannerSteps().forEach(step -> step.setActive(false));
    return mapper.mapConfigEntityToPayload(dao.save(configs));
  }
}
