package ca.jrvs.apps.twitter.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonUtil {
  public static <T> T toObjectFromJson(String json, Class cls) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return (T) objectMapper.readValue(json, cls);
  }
}
