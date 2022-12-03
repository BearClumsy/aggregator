package dplatonov.portal.validator;

import dplatonov.portal.dao.ScannerConfigsDao;
import dplatonov.portal.entity.ScannerConfigs;
import dplatonov.portal.entity.User;
import dplatonov.portal.enums.RoleEnum;
import dplatonov.portal.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ScannerConfigsValidator {

  private final ScannerConfigsDao dao;
  private final UserService userService;

  public ScannerConfigs getScannerConfigForOwner(Long scannerId) throws IllegalAccessException {
    User user = this.userService.getCurentUser();
    if (user.getRole().getRole().equals(RoleEnum.getRoleEnum(RoleEnum.ADMIN))) {
      return dao.findById(scannerId).orElseThrow(IllegalAccessException::new);
    } else {
      return dao.findByUserIdAndId(user.getId(), scannerId)
          .orElseThrow(IllegalAccessException::new);
    }
  }

  public List<ScannerConfigs> getAllActiveScannerConfigs() throws IllegalAccessException {
    User user = this.userService.getCurentUser();
    List<ScannerConfigs> result;
    if (user.getRole().getRole().equals(RoleEnum.getRoleEnum(RoleEnum.ADMIN))) {
      result = dao.findAllAndActive();
    } else {
      result = dao.findAllByUserId(user.getId()).orElseThrow(IllegalAccessException::new);
    }

    result.forEach(config -> config.getScannerSteps()
        .sort((o1, o2) -> Math.toIntExact(o1.getId() - o2.getId())));
    return result;
  }

}
