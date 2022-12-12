package dplatonov.scaner;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import dplatonov.scaner.dao.ScannerConfigsCutoffDao;
import dplatonov.scaner.dao.ScannerResultDao;
import dplatonov.scaner.entity.ScannerConfigCutoff;
import dplatonov.scaner.entity.ScannerConfigs;
import dplatonov.scaner.entity.ScannerResult;
import dplatonov.scaner.entity.ScannerSteps;
import dplatonov.scaner.enums.ScannerConfigActions;
import dplatonov.scaner.enums.ScannerConfigTypes;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.util.StringUtils;

public class Parser2 implements Runnable {

  private static final Logger log = LogManager.getLogger(Parser.class);
  //  public static final String REMOTE_URL_FIREFOX = "http://selenium-hub:4444/wd/hub/";
  public static final String REMOTE_URL_FIREFOX = "http://localhost:4444/wd/hub/";
  private final ScannerConfigs configs;
  private final List<ScannerSteps> scannerSteps;
  private final ScannerConfigCutoff scannerConfigCutoff;
  private final ScannerResultDao scannerResultDao;
  private final ScannerConfigsCutoffDao scannerConfigsCutoffDao;
  private final List<ScannerResult> savedResult = new ArrayList<>();

  public Parser2(ScannerConfigs configs, ScannerConfigCutoff scannerConfigCutoff,
      ScannerResultDao scannerResultDao,
      ScannerConfigsCutoffDao scannerConfigsCutoffDao) {
    this.configs = configs;
    this.scannerSteps = configs.getScannerSteps();
    this.scannerConfigCutoff = scannerConfigCutoff;
    this.scannerResultDao = scannerResultDao;
    this.scannerConfigsCutoffDao = scannerConfigsCutoffDao;
  }

  @Override
  public void run() {
    log.info("PARSER-010: Scanner is started");
    configureWebDriver();
    log.info("PARSER-011: Web Driver is configured");
    open(this.configs.getUrl());
    log.info("PARSER-012: Url: " + this.configs.getUrl() + " was opened");
    pars(0, 0, 0);
    finish();
    closeWebDriver();
  }

  private void finish() {
    log.info("PARSER-007: Scanner is finished");
    scannerConfigCutoff.setStopDate(new Date());
    scannerConfigsCutoffDao.save(scannerConfigCutoff);
    //TODO send jms message to server via kafka 'task is finished'
  }

  private void pars(int i, int repeatCount, int pagesBack) {
    Map<String, ScannerSteps> mapOfSteps = new HashMap<>();
    int size = this.scannerSteps.size();
    for (; i < size; i++) {
      ScannerSteps step = this.scannerSteps.get(i);
      if (step.getActive()) {
        String type = step.getType();
        String action = StringUtils.trimAllWhitespace(step.getAction());
        String value = step.getValue();
        String tag = step.getTag();
        if (action.contains(ScannerConfigActions.REPEAT.toString())) {
          String[] values = value.split(",");
          if (repeatCount == 0) {
            repeatCount = Integer.parseInt(values[0]);
            pagesBack = Integer.parseInt(values[1]);
            step.setValue("0,0");
          }
          if (repeatCount > 0) {
            log.info("PARSER-006: Repeat with tag: " + tag + "; repeatCount: " + repeatCount
                + "; pages back: " + pagesBack);
            IntStream.range(0, pagesBack).forEach(index -> Selenide.back());
            repeatCount--;
            nextTag++;
            pars(this.scannerSteps.indexOf(mapOfSteps.get(tag)), repeatCount, pagesBack);
          }
        } else {
          mapOfSteps.put(tag, step);
          SelenideElement element = getElement(tag, type);
          if (action.contains(ScannerConfigActions.INSERT.toString())) {
            log.info(
                "PARSER-003: Type INSERT tag: " + tag + "; value: " + value + "; type: " + type);
            element.setValue(value);
          } else if (action.contains(ScannerConfigActions.CLICK.toString())) {
            log.info("PARSER-004: Type CLICK tag: " + tag + "; type: " + type);
            element.click();
          } else if (action.contains(ScannerConfigActions.SCAN.toString())) {
            log.info("PARSER-005: Type SCAN tag: " + tag + "; type: " + type);
            String scan = element.getText();
            ScannerResult result = ScannerResult.builder()
                .scannerId(this.configs.getId())
                .value(scan)
                .build();
            scannerResultDao.save(result);
            savedResult.add(result);
          } else if (action.contains(ScannerConfigActions.PAUSE.toString())) {
            try {
              log.info("PARSER-008: Waite seconds: " + value);
              Thread.sleep(TimeUnit.SECONDS.toMillis(Long.parseLong(value)));
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          } else if (action.contains(ScannerConfigActions.CLEAR.toString())) {
            log.info("PARSER-009: Clear tag: " + tag);
            element.clear();
          }
        }
      }
    }
  }

  private SelenideElement getElement(String incommingTags, String incomingTypes) {
    SelenideElement element = null;
    if (Objects.isNull(incomingTypes) && Objects.isNull(incommingTags)) {
      return element;
    }
    String[] tags = incommingTags.split(",");
    String[] types = incomingTypes.split(",");
    int tagsLength = tags.length;
    for (int i = 0; i < tagsLength; i++) {
      String stringTag = tags[i];
      String stringType = types[i];
      String countOfTheSameTagsInTreeString =
          stringTag.contains("(") ? stringTag.replaceAll("\\D+", "") : "";
      int countOfTheSameTagsInTree = countOfTheSameTagsInTreeString.isEmpty() ? 0
          : Integer.parseInt(countOfTheSameTagsInTreeString) - 1;
      String tag = stringTag.split("[](]")[0];
      ScannerConfigTypes type = ScannerConfigTypes.forValue(
          stringType.replaceAll("[^A-Za-z]+", ""));
      switch (type) {
        case ID:
          if (countOfTheSameTagsInTree > 0) {
            element = Objects.isNull(element) ? null
                : element.$$(By.id(tag)).get(countOfTheSameTagsInTree);
          } else {
            element = Objects.isNull(element) ? $(By.id(tag)) : element.$(By.id(tag));
          }
          break;
        case TAG:
          if (countOfTheSameTagsInTree > 0) {
            element = Objects.isNull(element) ? null
                : element.$$(By.tagName(tag)).get(countOfTheSameTagsInTree);
          } else {
            element = Objects.isNull(element) ? $(By.tagName(tag)) : element.$(By.tagName(tag));
          }
          break;
        case NAME:
          if (countOfTheSameTagsInTree > 0) {
            element = Objects.isNull(element) ? null
                : element.$$(By.name(tag)).get(countOfTheSameTagsInTree);
          } else {
            element = Objects.isNull(element) ? $(By.name(tag)) : element.$(By.name(tag));
          }
          break;
        case HREF:
          if (countOfTheSameTagsInTree > 0) {
            element = Objects.isNull(element) ? null
                : element.$$(By.linkText(tag)).get(countOfTheSameTagsInTree);
          } else {
            element = Objects.isNull(element) ? $(By.linkText(tag)) : element.$(By.linkText(tag));
          }
          break;
      }
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
