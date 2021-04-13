package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.verify;

public class TwitterDaoIntTest {

  private static final Logger logger = LoggerFactory.getLogger(TwitterDaoIntTest.class);

  HttpHelper httpHelper;
  CrdDao dao;
  Tweet tweet;
  String text;
  Double longitude;
  Double latitude;
  String hashTag;

  @Before
  public void setUp() throws UnsupportedEncodingException {

    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
    dao = new TwitterDao(httpHelper);

    // minimum requirements to build tweet object
    hashTag = "#abc";
    text = "sometext " + hashTag + " " + System.currentTimeMillis();
    longitude = -1d;
    latitude = 1d;

    tweet = TweetUtil.buildTweet(
        URLEncoder.encode(text, "UTF-8"),
        longitude, latitude
    );
  }



  @Test
  public void create() {
    tweet = (Tweet) dao.create(tweet);
    assertNotNull(tweet);
    assertEquals(text, tweet.getText());
    assertEquals(longitude, tweet.getCoordinates().getCoordinates().get(0));
    assertEquals(latitude, tweet.getCoordinates().getCoordinates().get(1));

    // tear down - delete tweet
    try {
      logger.error(tweet.getText());
      dao.deleteById(tweet.getIdStr());
    } catch (Exception e) {
      logger.error("Tweet ID not found: {}", tweet.getIdStr());
      throw new RuntimeException(e);
    }
  }

  @Test
  public void findById() {
    tweet = (Tweet) dao.create(tweet);
    tweet = (Tweet) dao.findById(tweet.getIdStr());

    assertEquals(text, tweet.getText());
    assertEquals(longitude, tweet.getCoordinates().getCoordinates().get(0));
    assertEquals(latitude, tweet.getCoordinates().getCoordinates().get(1));
  }

  @Test
  public void deleteById() {
    tweet = (Tweet) dao.create(tweet);
    tweet = (Tweet) dao.deleteById(tweet.getIdStr());
    assertEquals(text, tweet.getText());
    assertEquals(longitude, tweet.getCoordinates().getCoordinates().get(0));
    assertEquals(latitude, tweet.getCoordinates().getCoordinates().get(1));
  }
}