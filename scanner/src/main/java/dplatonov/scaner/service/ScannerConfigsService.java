package dplatonov.scaner.service;

import dplatonov.scaner.Parser2;
import dplatonov.scaner.component.KafkaProducer;
import dplatonov.scaner.dao.ScannerConfigsCutoffDao;
import dplatonov.scaner.dao.ScannerConfigsDao;
import dplatonov.scaner.dao.ScannerResultDao;
import dplatonov.scaner.entity.ScannerConfigCutoff;
import dplatonov.scaner.entity.ScannerConfigs;
import dplatonov.scaner.entity.ScannerResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScannerConfigsService {

  private final ScannerConfigsDao dao;
  private final ExecutorService executorService = Executors.newFixedThreadPool(1);
  private final Map<Long, Future<?>> futureMap = new HashMap<>();
  private final ScannerResultDao scannerResultDao;
  private final ScannerConfigsCutoffDao scannerConfigsCutoffDao;
  private final KafkaProducer producer;

  @Value("${scanner.topic2}")
  private String topic;

  public void start(Long scannerConfigsId) {
    clearPreviousData(scannerConfigsId);
    ScannerConfigs scannerConfigs = dao.findByIdAndActive(scannerConfigsId).orElseThrow();
    ScannerConfigCutoff cutoff = scannerConfigsCutoffDao.save(
        ScannerConfigCutoff.builder().scannerId(scannerConfigsId).isInterrupted(false).build());
    Parser2 task = new Parser2(scannerConfigs, cutoff, scannerResultDao, scannerConfigsCutoffDao,
        producer, topic);
    futureMap.put(scannerConfigsId, executorService.submit(task));
  }

  @Transactional
  void clearPreviousData(Long scannerConfigsId) {
    List<ScannerResult> oldData = scannerResultDao.findAllByScannerId(scannerConfigsId);
    scannerResultDao.deleteAll(oldData);
    Optional<ScannerConfigCutoff> oldCutoff = scannerConfigsCutoffDao.findByScannerId(
        scannerConfigsId);
    oldCutoff.ifPresent(scannerConfigsCutoffDao::delete);
  }

  public void stop(Long scannerConfigsId) {
    if (futureMap.containsKey(scannerConfigsId)) {
      boolean cancel = futureMap.get(scannerConfigsId).cancel(true);
      if (cancel) {
        futureMap.remove(scannerConfigsId);
      }
    }
  }

}
