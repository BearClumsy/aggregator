package dplatonov.portal.component;

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

  public void send(String topic, String payload) {
    kafkaTemplate.send(topic, payload);
    LOGGER.info("sending payload='{}' to topic='{}'", payload, topic);
  }

}
