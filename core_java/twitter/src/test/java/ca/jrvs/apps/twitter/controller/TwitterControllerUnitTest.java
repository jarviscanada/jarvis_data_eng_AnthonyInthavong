package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

  @Mock
  TwitterService service;

  @InjectMocks
  TwitterController controller;

  String[] args;
  Tweet tweet;

  @Before
  public void setUp() throws Exception {
    BasicConfigurator.configure();
  }

  @Test
  public void postTweet() {
    // too many arguments
    try {
      args = new String[]{"1", "2", "3", "4"};
      controller.postTweet(args);
      fail();
    } catch (Exception e) {
      assertTrue(true);
    }

    // empty text
    try {
      args = new String[]{"post", "", "1:1"};
      controller.postTweet(args);
      fail();
    } catch (Exception e) {
      assertTrue(true);
    }

    // invalid coordinates
    try {
      args = new String[]{"post", "tweet_text", "abc:abc"};
      controller.postTweet(args);
      fail();
    } catch (Exception e) {
      assertTrue(true);
    }


    args = new String[]{
        "post",
        "This is my tweet text #abc" + System.currentTimeMillis(),
        "1:1"
    };

    Tweet tweet = TweetUtil.buildTweet("post", 1.0, 1.0);
    doReturn(tweet).when(service).postTweet(isNotNull());
    tweet = controller.postTweet(args);
    verify(service).postTweet(isNotNull());
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void showTweet() {
    // Test not working arguments
    try {
      args = new String[]{
          "show"
      };
      controller.showTweet(args);
      fail();
    } catch (Exception e){
      assertTrue(true);
    }

    // Test working arguments
    args = new String[]{
        "show",
        "123"
    };
    tweet = TweetUtil.buildTweet("tweet_text", 1.0, 1.0);
    doReturn(tweet).when(service).showTweet(isNotNull(), any());
    tweet = controller.showTweet(args);
    verify(service).showTweet(isNotNull(), any());
    assertNotNull(tweet);
    assertNotNull(tweet.getText());

    // testing with optional parameters
    args = new String[]{
        "show",
        "123",
        "field1:field2"
    };
    tweet = TweetUtil.buildTweet("tweet_text", 1.0, 1.0);
    doReturn(tweet).when(service).showTweet(isNotNull(), any());
    tweet = controller.showTweet(args);
    verify(service, times(2)).showTweet(isNotNull(), any());
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void deleteTweet() {
    // testing invalid length
    try {
      args = new String[] {
          "delete"
      };
      controller.deleteTweet(args);
      fail();
    } catch (Exception e) {
      assertTrue(true);
    }


    args = new String[]{
        "delete",
        "1,2,3"
    };
    List<Tweet> tweetList = new ArrayList<Tweet>();
    tweet = TweetUtil.buildTweet("tweet_text", 1.0, 1.0);
    tweetList.add(tweet);

    doReturn(tweetList).when(service).deleteTweets(isNotNull());
    tweetList = controller.deleteTweet(args);
    verify(service).deleteTweets(isNotNull());
    assertNotNull(tweetList);
  }
}