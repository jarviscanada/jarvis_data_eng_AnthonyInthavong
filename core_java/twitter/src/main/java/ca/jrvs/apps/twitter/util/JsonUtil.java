package ca.jrvs.apps.twitter.util;

import ca.jrvs.apps.twitter.model.Tweet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonUtil {
  public static <T> T toObjectFromJson(String json, Class cls) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return (T) objectMapper.readValue(json, cls);
  }

  public static String toPrettyJson(Tweet tweet) throws JsonProcessingException {
    //Creating the ObjectMapper object
    ObjectMapper mapper = new ObjectMapper();
    //Converting the Object to JSONString
    return mapper.writeValueAsString(tweet);
  }
}
