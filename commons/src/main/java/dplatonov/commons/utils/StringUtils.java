package dplatonov.commons.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class StringUtils {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  public String toStringJson(Object o) {
    return objectMapper.writeValueAsString(o);
  }

  @SneakyThrows
  public Object toObjectFromStringJson(String s) {
    return objectMapper.readValue(s, Object.class);
  }

}
