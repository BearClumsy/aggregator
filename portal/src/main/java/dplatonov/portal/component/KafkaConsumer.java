package dplatonov.portal.component;

import dplatonov.portal.messages.Action;
import dplatonov.portal.service.ScannerConfigsService;
import dplatonov.portal.utils.StringUtils;
import java.util.concurrent.CountDownLatch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
  @Getter
  private final CountDownLatch latch = new CountDownLatch(1);
  @Getter
  private String payload;

  private final ScannerConfigsService scannerConfigsService;

  @KafkaListener(topics = "${scanner.topic2}")
  public void receive(ConsumerRecord<?, ?> consumerRecord) {
    LOGGER.info("received payload='{}'", consumerRecord.toString());
    payload = consumerRecord.toString();
    String value = consumerRecord.value().toString();
    StringUtils<Action> stringUtils = new StringUtils<>();
    Action action = stringUtils.toObjectFromString(value, Action.class);
    scannerConfigsService.updateStatus(action);
    latch.countDown();
  }

}
