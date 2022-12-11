package dplatonov.scaner.component;

import dplatonov.scaner.enums.MsgStatus;
import dplatonov.scaner.messages.Action;
import dplatonov.scaner.service.ScannerConfigsService;
import dplatonov.scaner.utils.StringUtils;
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
  private CountDownLatch latch = new CountDownLatch(1);

  private final ScannerConfigsService scannerConfigsService;

  @KafkaListener(topics = "${scanner.topic}")
  public void receive(ConsumerRecord<?, ?> consumerRecord) {
    LOGGER.info("received payload='{}'", consumerRecord.toString());
    String value = consumerRecord.value().toString();
    StringUtils<Action> stringUtils = new StringUtils<>();
    Action action = stringUtils.toObjectFromString(value, Action.class);
    if (action.getStatus().equals(MsgStatus.START)) {
      scannerConfigsService.start(action.getScannerId());
    } else if (action.getStatus().equals(MsgStatus.STOP)) {
      scannerConfigsService.stop(action.getScannerId());
    }
    latch.countDown();
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }

}
