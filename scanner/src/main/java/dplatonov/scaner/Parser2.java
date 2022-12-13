package dplatonov.scaner;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import dplatonov.scaner.component.KafkaProducer;
import dplatonov.scaner.dao.ScannerConfigsCutoffDao;
import dplatonov.scaner.dao.ScannerResultDao;
import dplatonov.scaner.entity.ScannerConfigCutoff;
import dplatonov.scaner.entity.ScannerConfigs;
import dplatonov.scaner.entity.ScannerResult;
import dplatonov.scaner.entity.ScannerSteps;
import dplatonov.scaner.enums.MsgStatus;
import dplatonov.scaner.enums.ScannerConfigActions;
import dplatonov.scaner.enums.ScannerConfigTypes;
import dplatonov.scaner.messages.Action;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.util.StringUtils;

public class Parser2 implements Runnable {

  private static final Logger log = LogManager.getLogger(Parser.class);
    public static final String REMOTE_URL_FIREFOX = "http://selenium-hub:4444/wd/hub/";
//  public static final String REMOTE_URL_FIREFOX = "http://localhost:4444/wd/hub/";
  private final ScannerConfigs configs;
  private final List<ScannerSteps> scannerSteps;
  private final ScannerConfigCutoff scannerConfigCutoff;
  private final ScannerResultDao scannerResultDao;
  private final ScannerConfigsCutoffDao scannerConfigsCutoffDao;
  private final KafkaProducer producer;
  private final String topic;

  public Parser2(ScannerConfigs configs, ScannerConfigCutoff scannerConfigCutoff,
      ScannerResultDao scannerResultDao,
      ScannerConfigsCutoffDao scannerConfigsCutoffDao, KafkaProducer producer, String topic) {
    this.configs = configs;
    this.scannerSteps = configs.getScannerSteps();
    this.scannerConfigCutoff = scannerConfigCutoff;
    this.scannerResultDao = scannerResultDao;
    this.scannerConfigsCutoffDao = scannerConfigsCutoffDao;
    this.producer = producer;
    this.topic = topic;
  }

  @Override
  public void run() {
    log.info("PARSER-010: Scanner is started");
    configureWebDriver();
    log.info("PARSER-011: Web Driver is configured");
    open(this.configs.getUrl());
    log.info("PARSER-012: Url: " + this.configs.getUrl() + " was opened");
    pars(0, 0, 0, 0, new int[0], 0);
    log.info("PARSER-013: Parsing was finished");
    finish();
    closeWebDriver();
  }

  private void crash() {
    scannerConfigCutoff.setStopDate(new Date());
    scannerConfigCutoff.setIsInterrupted(true);
    scannerConfigsCutoffDao.save(scannerConfigCutoff);
    Action action = Action.builder()
        .scannerId(configs.getId())
        .status(MsgStatus.CRASH)
        .build();
    producer.send(topic, action);
    log.info("PARSER-007: Scanner is finished");
    closeWebDriver();
    Thread.currentThread().interrupt();
  }

  private void finish() {
    scannerConfigCutoff.setStopDate(new Date());
    scannerConfigsCutoffDao.save(scannerConfigCutoff);
    Action action = Action.builder()
        .scannerId(configs.getId())
        .status(MsgStatus.FINISH)
        .build();
    producer.send(topic, action);
    log.info("PARSER-007: Scanner is finished");
  }

  private void pars(int i, int repeatCount, int pagesBack, int next,
      int[] positionsOfTagsIsIterable, int maxRepeatCount) {
    Map<String, ScannerSteps> mapOfSteps = new HashMap<>();
    int size = this.scannerSteps.size();
    int iter = next;
    for (; i < size; i++) {
      ScannerSteps step = this.scannerSteps.get(i);
      if (step.getActive()) {
        String type = step.getType();
        String action = StringUtils.trimAllWhitespace(step.getAction());
        String value = step.getValue();
        String tag = step.getTag();
        if (action.contains(ScannerConfigActions.REPEAT.toString())) {
          if (pagesBack != 0 && next >= maxRepeatCount) {
            continue;
          }
          String[] values = value.split("/");
          if (repeatCount == 0) {
            repeatCount = Integer.parseInt(values[0]);
            maxRepeatCount = repeatCount;
            pagesBack = Integer.parseInt(values[1]);
          }
          if (repeatCount > 0) {
            next++;
            positionsOfTagsIsIterable = Arrays.stream(values[2].split(","))
                .mapToInt(Integer::parseInt).toArray();
            log.info("PARSER-006: Repeat with tag: " + tag + "; repeatCount: " + repeatCount
                + "; pages back: " + pagesBack + " next index is: " + next
                + " positions of tags is iterable: " + Arrays.toString(positionsOfTagsIsIterable));
            for (int index = 0; index < pagesBack; index++) {
              log.info("PARSER-016: Page back on: " + (pagesBack) + (" page(s)"));
              Selenide.back();
            }
            repeatCount--;
            pars(this.scannerSteps.indexOf(mapOfSteps.get(tag)), repeatCount, pagesBack, next,
                positionsOfTagsIsIterable, maxRepeatCount);
          }
        } else {
          mapOfSteps.put(tag, step);
          int positionOfTagIsIterable = 0;
          if (positionsOfTagsIsIterable.length > 0) {
            positionOfTagIsIterable = positionsOfTagsIsIterable[0];
            positionsOfTagsIsIterable = Arrays.stream(positionsOfTagsIsIterable).skip(1).toArray();
          }
          SelenideElement element = getElement(tag, type, iter, positionOfTagIsIterable);
          if (Objects.nonNull(element) && element.toString().contains("NoSuchElementException")) {
            log.error(
                "PARSER-015: Scanner is crushed on tag: " + tag + "; type: " + type + " action: "
                    + action);
            crash();
          }
          if (positionsOfTagsIsIterable.length == 0) {
            iter = 0;
          }
          if (action.contains(ScannerConfigActions.INSERT.toString()) && Objects.nonNull(element)) {
            log.info(
                "PARSER-003: Type INSERT tag: " + tag + "; value: " + value + "; type: " + type);
            element.setValue(value);
          } else if (action.contains(ScannerConfigActions.CLICK.toString()) && Objects.nonNull(
              element)) {
            log.info("PARSER-004: Type CLICK tag: " + tag + "; type: " + type);
            element.click();
          } else if (action.contains(ScannerConfigActions.SCAN.toString()) && Objects.nonNull(
              element)) {
            log.info("PARSER-005: Type SCAN tag: " + tag + "; type: " + type);
            String scan = element.getText();
            ScannerResult result = ScannerResult.builder()
                .scannerId(this.configs.getId())
                .value(scan)
                .build();
            scannerResultDao.save(result);
          } else if (action.contains(ScannerConfigActions.PAUSE.toString())) {
            try {
              log.info("PARSER-008: Waite seconds: " + value);
              Thread.sleep(TimeUnit.SECONDS.toMillis(Long.parseLong(value)));
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          } else if (action.contains(ScannerConfigActions.CLEAR.toString()) && Objects.nonNull(
              element)) {
            log.info("PARSER-009: Clear tag: " + tag);
            element.clear();
          } else if (action.contains(ScannerConfigActions.BACK.toString())) {
            log.info("PARSER-014: To the previous page");
            Selenide.back();
          }
        }
      }
    }
  }

  private SelenideElement getElement(String incommingTags, String incomingType, int next,
      int positionOfTagIsIterable) {
    SelenideElement element = null;
    if (Objects.isNull(incomingType) && Objects.isNull(incommingTags)) {
      return null;
    }
    ScannerConfigTypes type = ScannerConfigTypes.forValue(incomingType);
    if (next > 0) {
      String[] tags = incommingTags.split("/");
      String node = tags[positionOfTagIsIterable];
      String index = node.replaceAll("\\D+", "");
      String newIndex = String.valueOf(Integer.parseInt(index) + next);
      String newNode = node.split("\\[")[0] + "[" + newIndex + "]";
      tags[positionOfTagIsIterable] = newNode;
      StringBuilder sb = new StringBuilder();
      Arrays.stream(tags).skip(1).forEach(tag -> {
        sb.append("/");
        sb.append(tag);
      });
      incommingTags = sb.toString();
    }
    if (Objects.requireNonNull(type) == ScannerConfigTypes.XPATH) {
      element = $(By.xpath(incommingTags));
    }

    return element;
  }

  private void configureWebDriver() {
    try {
      RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(REMOTE_URL_FIREFOX),
          DesiredCapabilities.firefox());
      remoteWebDriver.manage().window().maximize();
      WebDriverRunner.setWebDriver(remoteWebDriver);
    } catch (MalformedURLException e) {
      log.info("PARSER-001: Can't connect to remote web driver");
    }
  }

  private void closeWebDriver() {
    WebDriverRunner.closeWebDriver();
    log.info("PARSER-002: Web Driver is closed success");
  }

}
