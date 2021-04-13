package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

  private static final Logger logger = LoggerFactory.getLogger(TwitterServiceUnitTest.class);

  @Mock
  CrdDao mockDao;

  @InjectMocks
  TwitterService service;

  String hashTag;
  String text;
  double longitude;
  double latitude;
  Tweet tweet;

  @Before
  public void setUp() throws UnsupportedEncodingException {
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

    // minimum requirements to build tweet object
    hashTag = "#abc";
    text = "sometext " + hashTag + " " + System.currentTimeMillis();
    longitude = -1d;
    latitude = 1d;

    tweet = TweetUtil.buildTweet(
        URLEncoder.encode(text, "UTF-8"),
        longitude, latitude
    );
    doReturn(tweet).when(mockDao).create(isNotNull());
    tweet = service.postTweet(tweet);
    verify(mockDao).create(isNotNull());
    assertNotNull(tweet);
  }

  @Test
  public void showTweet() {
    // invalid tweet id
    try {
      service.showTweet("abc", null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    // valid tweet id
    tweet = TweetUtil.buildTweet("HellWorld", 1d, 1d);
    String id = "1228393702244134912";
    doReturn(tweet).when(mockDao).findById(id);
    tweet = service.showTweet(id, null);
    verify(mockDao).findById(id);
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void deleteTweets() {
    // test fail
    String[] ids = {"abc"};
    try {
      service.deleteTweets(ids);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    // test success
    ids = new String[]{"1", "2"};
    Arrays.stream(ids).forEach(
        s -> doReturn(TweetUtil.buildTweet(s.toString(), 1d, 1d)).
            when(mockDao).deleteById(s.toString())
    );
    service.deleteTweets(ids).stream().forEach(
        tweet -> assertNotNull(tweet)
    );


  }
}