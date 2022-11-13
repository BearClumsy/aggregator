package dplatonov.scaner;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import dplatonov.scaner.dao.CompanyDao;
import dplatonov.scaner.dao.ConfigDao;
import dplatonov.scaner.entity.Address;
import dplatonov.scaner.entity.Company;
import dplatonov.scaner.validator.CompanyValidator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Parser {
  private static final Logger log = LogManager.getLogger(Parser.class);
  private static final String URL = "https://jobs.dou.ua/companies/";
  private final CompanyDao companyDAO;
  private final ConfigDao configDao;
  private final CompanyValidator companyValidator;
  public static final String REMOTE_URL_FIREFOX = "http://selenium-hub:4444/wd/hub/";
  private RemoteWebDriver remoteWebDriver;

  @Scheduled(fixedDelay = 3600000) // once in day
  public void task() {
    config();
    open(URL);
    downloadPages();
    pars();
    closeWebDriver();
  }

  private void config() {
    try {
      remoteWebDriver =
          new RemoteWebDriver(new URL(REMOTE_URL_FIREFOX), DesiredCapabilities.firefox());
      remoteWebDriver.manage().window().maximize();
      WebDriverRunner.setWebDriver(remoteWebDriver);
    } catch (MalformedURLException e) {
      log.info("PARSER-001: Can't connect to remote web driver");
    }
  }

  private void downloadPages() {
    log.info("PARSER-002: Start download HTML");
    int countOfPage = configDao.findAll().get(0).getScannerPageNum();
    IntStream.range(0, countOfPage)
        .forEach(
            value -> {
              try {
                Thread.sleep(1000);
                $(By.linkText("Больше компаний")).click();
              } catch (InterruptedException e) {
                log.error("PARSER-003: HTML download is interrupted! " + e);
              }
            });
    log.info("PARSER-004: Download HTML is complete");
  }

  private ArrayList<Company> pars() {
    log.info("PARSER-005: Start HTML parsing!");
    ArrayList<Company> companies = new ArrayList<>();
    ElementsCollection company = $$(By.className("l-items")).get(0).$$(By.className("company"));
    int size = company.size();
    IntStream.range(0, size)
        .forEach(
            i -> {
              SelenideElement el = company.get(i);
              if (!el.$(By.className("cn-a")).exists()
                  || !el.$(By.className("city")).exists()
                  || !el.$(By.className("descr")).exists()) {
                return;
              }

              String name = el.$(By.className("cn-a")).text();
              String cities = el.$(By.className("city")).text();
              String description = el.$(By.className("descr")).text();
              ArrayList<Address> addresses = downloadAndParsOfficesForCurrentCompany(el);
              save(
                  Company.builder()
                      .name(name)
                      .city(cities)
                      .description(description)
                      .addresses(addresses)
                      .build());
            });

    log.info("PARSER-006: HTML parsing is complete!");

    return companies;
  }

  private ArrayList<Address> downloadAndParsOfficesForCurrentCompany(SelenideElement sel) {
    ArrayList<Address> addresses = new ArrayList<>();
    WebElement officesLink = sel.findElement(By.linkText("Офисы"));
    Actions actions = new Actions(this.remoteWebDriver);
    ((JavascriptExecutor) this.remoteWebDriver)
        .executeScript("arguments[0].scrollIntoView(true);", officesLink);
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      log.warn("PARSER-007: Application was interrupted when scroll down to link");
    }
    actions.keyDown(Keys.CONTROL).click(officesLink).keyUp(Keys.CONTROL).build().perform();
    ArrayList<String> tabs = new ArrayList<>(this.remoteWebDriver.getWindowHandles());
    this.remoteWebDriver.switchTo().window(tabs.get(1));
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      log.warn("PARSER-008: Application was interrupted when scroll down to link");
    }
    ElementsCollection cityEl = $$(By.className("city"));
    for (SelenideElement el : cityEl) {
      String cityValue = el.$(By.tagName("h4")).text();
      List<String> addressValues = el.$$(By.className("address")).texts();
      for (String addressValue : addressValues) {
        addresses.add(
            Address.builder()
                .city(cityValue)
                .address(addressValue.replace(" (показать на карте)", ""))
                .build());
      }
    }
    closeWindow();
    this.remoteWebDriver.switchTo().window(tabs.get(0));
    return addresses;
  }

  private void save(Company company) {
    String companyName = company.getName();
    Optional<Company> exist = companyValidator.isExist(companyName);
    if (exist.isPresent()) {
      log.warn("PARSER-009: Company with name " + companyName + " is exist!");
      return;
    }
    log.info("PARSER-010: Founded company start save in to PostgreSQL");
    companyDAO.save(company);
    log.info("PARSER-011: Founded company is saved in PostgreSQL");
  }

  private void closeWebDriver() {
    WebDriverRunner.closeWebDriver();
    log.info("PARSER-012: Web Driver is closed success");
  }
}
