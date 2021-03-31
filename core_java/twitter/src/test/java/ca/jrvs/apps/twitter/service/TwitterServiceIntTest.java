package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceIntTest {

  private static final Logger logger = LoggerFactory.getLogger(TwitterServiceIntTest.class);

  HttpHelper httpHelper;
  TwitterDao dao;
  TwitterService service;

  Tweet tweet;
  String text;

  double longitude;
  double latitude;

  @Before
  public void setUp() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);

    dao = new TwitterDao(httpHelper);
    service = new TwitterService(dao);
  }

  @Test
  public void postTweet() throws UnsupportedEncodingException {
    // test failed tweet
    // text longer 140 characters
    try {
      text = URLEncoder.encode(new String(new char[141]).replace('\0', ' ')
          + System.currentTimeMillis(), "UTF-8");
      tweet = TweetUtil.buildTweet(text, 1d, 1d);
      service.postTweet(tweet);
      fail();
    } catch (IllegalArgumentException e) {
      logger.debug("text longer than 140 characters: {}", tweet.getText().length());
    }
    // longitude out of range [-180, 180]
    try {
      tweet = TweetUtil.buildTweet(text, -181d, 1d);
      service.postTweet(tweet);
      fail();
    } catch (IllegalArgumentException e) {
      logger.debug("longitude out of range < -180: {}",
          tweet.getCoordinates().getCoordinates().get(0));
    }

    try {
      tweet = TweetUtil.buildTweet(text, 181d, 1d);
      service.postTweet(tweet);
      fail();
    } catch (IllegalArgumentException e) {
      logger.debug("longitude out of range > 180: {}",
          tweet.getCoordinates().getCoordinates().get(0));
    }
    // latitude out of range [-90, 90]
    try {
      tweet = TweetUtil.buildTweet(text, 1d, -91d);
      service.postTweet(tweet);
      fail();
    } catch (IllegalArgumentException e) {
      logger.debug("latitude out of range < -90: {}",
          tweet.getCoordinates().getCoordinates().get(1));
    }
    try {
      tweet = TweetUtil.buildTweet(text, -1d, 91d);
      service.postTweet(tweet);
      fail();
    } catch (IllegalArgumentException e) {
      logger.debug("latitude out of range > 90: {}",
          tweet.getCoordinates().getCoordinates().get(1));
    }

    // testing working post request
    // minimum requirements to build tweet object
    String hashTag = "#abc";
    text = "sometext " + hashTag + " " + System.currentTimeMillis();
    longitude = -1d;
    latitude = 1d;

    tweet = TweetUtil.buildTweet(
        URLEncoder.encode(text, "UTF-8"),
        longitude, latitude
    );
    tweet = service.postTweet(tweet);
    assertNotNull(tweet);
    assertEquals(text, tweet.getText());
    assertEquals((Double) longitude, tweet.getCoordinates().getCoordinates().get(0));
    assertEquals((Double) latitude, tweet.getCoordinates().getCoordinates().get(1));


    // tear down - delete tweet
    try {
      logger.error(tweet.getText());
      service.deleteTweets(new String[]{tweet.getIdStr()});
    } catch (Exception e) {
      logger.error("Tweet ID not found: {}", tweet.getIdStr());
      throw new RuntimeException(e);
    }
  }

  @Test
  public void showTweet() throws UnsupportedEncodingException {
    // invalid tweet id
    try {
      service.showTweet("abc", null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    // valid tweet id
    text = "Hello World";
    longitude = 1d;
    latitude = 1d;
    tweet = TweetUtil.buildTweet(URLEncoder.encode(text, "UTF-8"),
        longitude, latitude);
    tweet = service.postTweet(tweet);
    String id = tweet.getIdStr();
    tweet = service.showTweet(id, null);
    assertNotNull(tweet);
    assertEquals(text, tweet.getText());
    assertEquals((Double) longitude, tweet.getCoordinates().getCoordinates().get(0));
    assertEquals((Double) latitude, tweet.getCoordinates().getCoordinates().get(1));

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
  public void deleteTweets() throws UnsupportedEncodingException {
    // test fail
    try {
      service.deleteTweets(new String[]{"abc"});
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }


    // test success
    // create n tweets then delete
    int n = 3;
    String[] tweetListIds = new String[n];
    for (int i=0; i<n; i++) {
      text = "First tweet #abc " + System.currentTimeMillis();
      text = URLEncoder.encode(text, "UTF-8");
      tweet = TweetUtil.buildTweet(text, 1d, 1d);
      tweet = service.postTweet(tweet);
      tweetListIds[i] = tweet.getIdStr();
    }
    List<Tweet> deletedTweets = service.deleteTweets(tweetListIds);
    assertNotNull(deletedTweets);
    for (int i=0; i<n; i++) {
      assertEquals(tweetListIds[i], deletedTweets.get(i).getIdStr());
    }
  }
}