package dplatonov.scaner.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class StringUtils<T> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  public String toStringFromObject(T t) {
    return objectMapper.writeValueAsString(t);
  }

  @SneakyThrows
  public T toObjectFromString(String json, Class<T> t) {
    return objectMapper.readValue(json, t);
  }

}
