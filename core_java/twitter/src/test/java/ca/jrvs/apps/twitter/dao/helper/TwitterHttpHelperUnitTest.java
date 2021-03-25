package ca.jrvs.apps.twitter.dao.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URLEncoder;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.BasicConfigurator;


import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TwitterHttpHelperUnitTest {
  private HttpHelper httpHelper;

  private static final Logger logger = LoggerFactory.getLogger(TwitterHttpHelperUnitTest.class);

  @Before
  public void setup() {
    BasicConfigurator.configure();
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);

  }

  @Test
  public void httpPost() throws Exception {
    String hashTag = "#abc";
    String text = "first tweet " + hashTag + " " + System.currentTimeMillis();
    text = URLEncoder.encode(text, "UTF-8");

    String uri = "https://api.twitter.com/1.1/statuses/update.json?status="
        + text
        + "&lat=1.0&long=-1.0";

    HttpResponse response = httpHelper.httpPost(new URI(uri));
    System.out.println(EntityUtils.toString(response.getEntity()));

    assertNotNull(response);
    assertEquals(200, response.getStatusLine().getStatusCode());

  }

  @Test
  public void httpGet() throws Exception {
    String id = "1228393702244134912";
    String uri = "https://api.twitter.com/1.1/statuses/show.json?id=" + id;
//    uri = URLEncoder.encode(uri, "UTF-8");
    HttpResponse response = httpHelper.httpGet(new URI(uri));
    System.out.println(EntityUtils.toString(response.getEntity()));

    assertNotNull(response);
    assertEquals(200, response.getStatusLine().getStatusCode());

  }
}
