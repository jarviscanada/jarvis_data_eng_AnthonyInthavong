package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.io.IOException;
import java.net.URLEncoder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

  private static final Logger logger = LoggerFactory.getLogger(TwitterDaoUnitTest.class);

  @Mock
  HttpHelper mockHelper;

  @InjectMocks
  TwitterDao dao;

  String tweetJsonStr;

  Tweet expectedTweet;

  @Before
  public void setUp() throws Exception{

    tweetJsonStr = "{\n" +
        "  \"created_at\" : \"Thu Jun 04 01:46:18 +0000 2020\",\n" +
        "  \"id\" : 1268358385075961862,\n" +
        "  \"id_str\" : \"1268358385075961862\",\n" +
        "  \"text\" : \"timestamp20159115163212\",\n" +
        "  \"entities\" : {\n" +
        "    \"hashtags\" : [ ],\n" +
        "    \"user_mentions\" : [ ]\n" +
        "  },\n" +
        "  \"coordinates\" : {\n" +
        "    \"coordinates\" : [ 10.0, 1.0 ],\n" +
        "    \"type\" : \"Point\"\n" +
        "  },\n" +
        "  \"retweet_count\" : 0,\n" +
        "  \"retweeted\" : false,\n" +
        "  \"entites\" : {\n" +
        "    \"hashtags\" : [ ],\n" +
        "    \"user_mentions\" : [ ]\n" +
        "  },\n" +
        "  \"favourite_count\" : 0\n" +
        "}";

    expectedTweet = JsonUtil.toObjectFromJson(tweetJsonStr, Tweet.class);
  }

  @Test
  public void create() throws IOException {
    //test failed request
    String hashTag = "#abc";
    String text = "sometext " + hashTag + " " + System.currentTimeMillis();
    text = URLEncoder.encode(text, "UTF-8");
    Double lat = 1d;
    Double lon = -1d;
    //expectation is expected here
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      dao.create(TweetUtil.buildTweet(text, lon, lat));
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }
    verify(mockHelper).httpPost(isNotNull());

    //Test happy path
    //however, we don't want to call parseResponseBody.
    //we will make a spyDao which can fake parseResponseBody return value
    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = spy(dao);
    //mock parseResponseBody
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());


    Tweet tweet = spyDao.create(TweetUtil.buildTweet(text, lon, lat));
    verify(mockHelper, times(2)).httpPost(isNotNull());
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void findById(){

    // test failed request
    when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
    try {
      dao.findById("");
      fail();
    } catch (RuntimeException e){
      assertTrue(true);
    }

    // verify httpGet is called
    verify(mockHelper).httpGet(isNotNull());

    // test successful request
    TwitterDao spyDao = spy(dao);

    when(mockHelper.httpGet(isNotNull())).thenReturn(null);
    // mock parseResponseBody
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(),anyInt());
    Tweet tweet = spyDao.findById("1268358385075961862");
    verify(mockHelper, times(2)).httpGet(isNotNull());
    assertNotNull(tweet);
    assertNotNull(tweet.getText());

  }

  @Test
  public void deleteById() throws Exception {
    TwitterDao spyDao = spy(dao);
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
    Tweet tweet = spyDao.deleteById("1268358385075961862");
    verify(mockHelper).httpPost(isNotNull());
    assertNotNull(tweet);
    assertNotNull(tweet.getText());

  }
}