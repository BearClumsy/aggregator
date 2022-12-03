package dplatonov.portal.mapper;

import dplatonov.portal.entity.ScannerConfigs;
import dplatonov.portal.entity.ScannerSteps;
import dplatonov.portal.payload.ScannerConfigsPayload;
import dplatonov.portal.payload.ScannerStepPayload;
import dplatonov.portal.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScannerConfigsMapper {

  private final UserService userService;

  public List<ScannerConfigsPayload> mapConfigsEntityToPayload(List<ScannerConfigs> configs) {
    return configs.stream().map(this::mapConfigEntityToPayload).collect(Collectors.toList());
  }

  public ScannerConfigsPayload mapConfigEntityToPayload(ScannerConfigs scannerConfigs) {
    return ScannerConfigsPayload.builder()
        .id(scannerConfigs.getId())
        .name(scannerConfigs.getName())
        .url(scannerConfigs.getUrl())
        .scannerSteps(mapStepsEntityToPayload(scannerConfigs.getScannerSteps()))
        .active(scannerConfigs.getActive())
        .userId(scannerConfigs.getUser().getId())
        .build();
  }

  public List<ScannerStepPayload> mapStepsEntityToPayload(List<ScannerSteps> scannerSteps) {
    return scannerSteps.stream().map(ScannerConfigsMapper::mapStepEntityToPayload)
        .collect(Collectors.toList());
  }

  public static ScannerStepPayload mapStepEntityToPayload(ScannerSteps scannerSteps) {
    return ScannerStepPayload.builder()
        .tag(scannerSteps.getTag())
        .type(scannerSteps.getType())
        .action(scannerSteps.getAction())
        .value(scannerSteps.getValue())
        .active(scannerSteps.getActive())
        .build();
  }

  public ScannerConfigs mapConfigPayloadToEntity(ScannerConfigsPayload payload) {
    return ScannerConfigs.builder()
        .id(payload.getId())
        .name(payload.getName())
        .url(payload.getUrl())
        .scannerSteps(mapStepsPayloadToEntity(payload.getScannerSteps()))
        .active(payload.isActive())
        .user(userService.getUserById(payload.getUserId()))
        .build();
  }

  public List<ScannerSteps> mapStepsPayloadToEntity(List<ScannerStepPayload> scannerStepPayloads) {
    return scannerStepPayloads.stream().map(ScannerConfigsMapper::mapStepPayloadToEntity).collect(
        Collectors.toList());
  }

  public static ScannerSteps mapStepPayloadToEntity(ScannerStepPayload payload) {
    return ScannerSteps.builder()
        .tag(payload.getTag())
        .type(payload.getType())
        .action(payload.getAction())
        .value(payload.getValue())
        .active(payload.isActive())
        .build();
  }

}
