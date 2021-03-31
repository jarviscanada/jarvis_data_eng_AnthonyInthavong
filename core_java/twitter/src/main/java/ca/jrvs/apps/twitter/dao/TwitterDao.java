package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterDao implements CrdDao<Tweet, String> {

  private final static Logger logger = LoggerFactory.getLogger(TwitterDao.class);

  //URI constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";

  //URI symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";

  //URI query
  private static final String ID = "id";
  private static final String LONG = "long";
  private static final String LAT = "lat";
  private static final String STATUS = "status";

  //Response code
  private static final int HTTP_OK = 200;

  private final HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  @Override
  public Tweet create(Tweet tweet) {
    URI uri;
    try {
      uri = getPostUri(tweet);
    } catch (URISyntaxException | UnsupportedEncodingException e) {
      throw new IllegalArgumentException("Invalid tweet input", e);
    }

    // Execute HTTP Request
    HttpResponse response = httpHelper.httpPost(uri);
    logger.info("executed httpPost request");
    // Validate response and deserialize response to Tweet object
    return parseResponseBody(response, HTTP_OK);
  }

  private URI getPostUri(Tweet tweet) throws URISyntaxException, UnsupportedEncodingException {
    String uri;
    String tweet_long = tweet.getCoordinates().getCoordinates().get(0).toString();
    String tweet_lat = tweet.getCoordinates().getCoordinates().get(1).toString();
    String tweet_status = tweet.getText();
    uri = API_BASE_URI + POST_PATH + QUERY_SYM + STATUS + EQUAL + tweet_status
        + AMPERSAND + LAT + EQUAL + tweet_lat
        + AMPERSAND + LONG + EQUAL + tweet_long;
    try {
      return new URI(uri);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Check response status code Convert Response Entity to Tweet
   */
  Tweet parseResponseBody(HttpResponse response, Integer expectedStatusCode) {
    Tweet tweet;

    //Check response status
    int status = response.getStatusLine().getStatusCode();
    if (status != expectedStatusCode) {
      try {
        System.out.println(EntityUtils.toString(response.getEntity()));
      } catch (IOException e) {
        System.out.println("Response has no entity");
      }
      throw new RuntimeException("Unexpected HTTP status: " + status);
    }

    if (response.getEntity() == null) {
      throw new RuntimeException("Empty response body");
    }

    //Convert Response Entity to str
    String jsonStr;
    try {
      jsonStr = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      throw new RuntimeException("Failed to convert entity to String", e);
    }

    //Deserialize JSON string to Tweet object
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      tweet = objectMapper.readValue(jsonStr, Tweet.class);
    } catch (IOException e) {
      logger.error("Unable to process JSON to object: {}", jsonStr);
      throw new RuntimeException("Unable to process JSON to object", e);
    }

    return tweet;
  }

  @Override
  public Tweet findById(String s) {
    HttpResponse response;
    URI uri;
    try {
       uri = new URI(API_BASE_URI + SHOW_PATH + QUERY_SYM
           + ID + EQUAL + s
       );
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid Syntax for URI", e);
    }

    // execute http request
    response = httpHelper.httpGet(uri);

    // validate response and deserialize response to tweet object
    return parseResponseBody(response, HTTP_OK);
  }

  @Override
  public Tweet deleteById(String s) {
    URI uri;
    HttpResponse response;

    try {
      uri = new URI(API_BASE_URI + DELETE_PATH + '/' + s + ".json");
      logger.info("deleteById URI: {}", uri.toString());
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid Syntax for URI", e);
    }

    // execute http request
    response = httpHelper.httpPost(uri);

    // validate response and deserialize response to tweet object
    return parseResponseBody(response, HTTP_OK);
  }
}
