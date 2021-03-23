package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.log4j.BasicConfigurator;
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
    BasicConfigurator.configure();
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

//  @Test
//  public void showTweet() {
//    service.showTweet();
//  }
//
//  @Test
//  public void deleteTweets() {
//    service.deleteTweets();
//  }
}