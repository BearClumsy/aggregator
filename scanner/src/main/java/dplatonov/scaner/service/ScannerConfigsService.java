package dplatonov.scaner.service;

import dplatonov.scaner.Parser2;
import dplatonov.scaner.dao.ScannerConfigsCutoffDao;
import dplatonov.scaner.dao.ScannerConfigsDao;
import dplatonov.scaner.dao.ScannerResultDao;
import dplatonov.scaner.entity.ScannerConfigCutoff;
import dplatonov.scaner.entity.ScannerConfigs;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableScheduling
@AllArgsConstructor
@Transactional
public class ScannerConfigsService {

  private final ScannerConfigsDao dao;
  private final ExecutorService executorService = Executors.newFixedThreadPool(1);
  private final Map<Long, Future<?>> futureMap = new HashMap<>();
  private final ScannerResultDao scannerResultDao;
  private final ScannerConfigsCutoffDao scannerConfigsCutoffDao;

  @Scheduled(fixedDelay = 2000000)
  private void init() {
    start(70L);
//    stop(70L);
  }

  private void start(Long scannerConfigsId) {
    ScannerConfigs scannerConfigs = dao.findByIdAndActive(scannerConfigsId).orElseThrow();
    ScannerConfigCutoff cutoff = scannerConfigsCutoffDao.save(
        ScannerConfigCutoff.builder().scannerId(scannerConfigsId).isInterrupted(false).build());
    Parser2 task = new Parser2(scannerConfigs, cutoff, scannerResultDao, scannerConfigsCutoffDao);
    futureMap.put(scannerConfigsId, executorService.submit(task));
//    try {
//      futureMap.get(scannerConfigsId).get();
//    } catch (InterruptedException | ExecutionException e) {
//      throw new RuntimeException(e);
//    }
  }

  private void stop(Long scannerConfigsId) {
    futureMap.get(scannerConfigsId).cancel(true);
  }

}
