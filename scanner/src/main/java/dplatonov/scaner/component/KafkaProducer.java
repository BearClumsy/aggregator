package dplatonov.scaner.component;

import dplatonov.scaner.messages.Action;
import dplatonov.scaner.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void send(String topic, Action action) {
    StringUtils<Action> utils = new StringUtils<>();
    String payload = utils.toStringFromObject(action);
    LOGGER.info("sending payload='{}' to topic='{}'", payload, topic);
    kafkaTemplate.send(topic, payload);
  }
}
