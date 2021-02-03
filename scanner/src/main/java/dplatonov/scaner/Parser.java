package dplatonov.scaner;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.back;
import static com.codeborne.selenide.Selenide.open;
import com.codeborne.selenide.SelenideElement;
import dplatonov.scaner.dao.CompanyDAO;
import dplatonov.scaner.entity.Address;
import dplatonov.scaner.entity.Company;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Parser {
  private static final Logger log = LogManager.getLogger(Parser.class);
  private static final String URL = "https://jobs.dou.ua/companies/";
  private static final int COUNT_OF_PAGE = 0;
  private final CompanyDAO dao;

  @Scheduled(fixedDelay = 3600000) // once in day
  public void task() {
    open(URL);
    downloadPages();
    ArrayList<Company> companies = pars();
    save(companies);
  }

  private void downloadPages() {
    log.info("Start download HTML");
    IntStream.range(0, COUNT_OF_PAGE)
        .forEach(
            value -> {
              try {
                Thread.sleep(1000);
                $(By.linkText("Больше компаний")).click();
              } catch (InterruptedException e) {
                log.error("HTML download is interrupted! " + e);
              }
            });
    log.info("Download HTML is complete");
  }

  private ArrayList<Company> pars() {
    log.info("Start HTML parsing!");
    ArrayList<Company> companies = new ArrayList<>();
    int size = $$(By.className("l-company")).size();
    IntStream.range(0, size)
        .forEach(
            i -> {
              SelenideElement el = $$(By.className("l-company")).get(i);
              String name = el.$(By.className("cn-a")).text();
              String cities = el.$(By.className("city")).text();
              String description = el.$(By.className("descr")).text();
              ArrayList<Address> addresses = downloadAndParsOfficesForCurrentCompany(el);
              companies.add(
                  Company.builder()
                      .name(name)
                      .city(cities)
                      .description(description)
                      .addresses(addresses)
                      .build());
            });

    log.info("HTML parsing is complete!");

    return companies;
  }

  private ArrayList<Address> downloadAndParsOfficesForCurrentCompany(SelenideElement sel) {
    ArrayList<Address> addresses = new ArrayList<>();
    sel.find(By.linkText("Офисы")).click();
    $$(By.className("city"))
        .forEach(
            el -> {
              String city = el.$(By.tagName("h4")).text();
              el.$$(By.className("address"))
                  .texts()
                  .forEach(
                      address ->
                          addresses.add(
                              Address.builder()
                                  .city(city)
                                  .address(address.replace(" (показать на карте)", ""))
                                  .build()));
            });
    back();
    return addresses;
  }

  private void save(List<Company> companies) {
    log.info("Founded companies start save in to PostgreSQL");
    dao.saveAll(companies);
    log.info("Founded companies is saved in PostgreSQL");
  }
}
