package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterControllerIntTest {

  private static final Logger logger = LoggerFactory.getLogger(TwitterControllerIntTest.class);

  TwitterHttpHelper httpHelper;
  TwitterDao dao;
  TwitterService service;
  TwitterController controller;

  String[] args;
  Tweet tweet;

  @Before
  public void setUp() {

    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);

    dao = new TwitterDao(httpHelper);
    service = new TwitterService(dao);
    controller = new TwitterController(service);


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

    String text = "This is my tweet text #abc" + System.currentTimeMillis();
    Double longitude = 1d;
    Double latitude = 1d;
    args = new String[]{
        "post",
        text,
        longitude.toString() + ":" + latitude.toString()
    };

    tweet = controller.postTweet(args);
    assertNotNull(tweet);
    assertEquals(text, tweet.getText());
    assertEquals(longitude, tweet.getCoordinates().getCoordinates().get(0));
    assertEquals(latitude, tweet.getCoordinates().getCoordinates().get(1));

    // tear down - delete tweet
    try {
      logger.error(tweet.getText());
      controller.deleteTweet(new String[]{"delete", tweet.getIdStr()});
    } catch (Exception e) {
      logger.error("Tweet ID not found: {}", tweet.getIdStr());
      throw new RuntimeException(e);
    }
  }

  @Test
  public void showTweet() {

    String text = "This is my text #abc " + System.currentTimeMillis();
    Double longitude = 1.0;
    Double latitude = 1.0;
    tweet = controller.postTweet(new String[]{
        "post",
        text,
        longitude.toString() + ":" + latitude.toString()
    });

    String id = tweet.getIdStr();

    args = new String[]{
        "show",
        id
    };
    tweet = controller.showTweet(args);

    assertNotNull(tweet);
    assertEquals(text, tweet.getText());
    assertEquals(longitude, tweet.getCoordinates().getCoordinates().get(0));
    assertEquals(latitude, tweet.getCoordinates().getCoordinates().get(1));

    // tear down
    try {
      args = new String[]{"delete", id};
      controller.deleteTweet(args);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
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

    List<String> ids = new ArrayList<>();

    int n = 3;
    for (int i=0; i<n; i++) {
      tweet = controller.postTweet(new String[]{
          "post", "some text" + System.currentTimeMillis(), "1.0:1.0"
      });
      ids.add(tweet.getIdStr());
    }


    args = new String[]{
      "delete", String.join(",", ids)
    };
    List<Tweet> tweetList = controller.deleteTweet(args);
    assertNotNull(tweetList);
    for (int i=0; i<n; i++) {
      assertEquals(ids.get(i), tweetList.get(i).getIdStr());
    }
  }
}