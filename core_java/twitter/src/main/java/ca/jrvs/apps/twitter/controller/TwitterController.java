package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.Controller;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.StringUtil;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class TwitterController implements Controller {

  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";

  private static final String POST_USAGE = "USAGE:\n"
          + "TwitterApp \"post\" \"tweet_text\" \"latitude:longitude\"\n"
          + "\n"
          + "Arguments:\n"
          + "tweet_text         - tweet_text cannot exceed 140 UTF-8 encoded characters.\n"
          + "latitude:longitude - Geo location";

  private static final String SHOW_USAGE = "USAGE:\n"
      + "TwitterApp show tweet_id [field1,fields2]\n"
      + "\n"
      + "Arguments:\n"
      + "tweet_id  - Tweet ID. Same as id_str in the tweet object\n"
      + "[field1,fields2]  - A comma-separated list of top-level fields from the tweet object (similar to SELECT clause in SQL)\n";

  private static final String DELETE_USAGE = "USAGE: TwitterApp delete [id1,id2,..]\n"
      + "\n"
      + "Arguments:\n"
      + "tweet_ids - A comma-separated list of tweets.\n";

  private Service service;

  public TwitterController(Service service){
    this.service = service;
  }

  @Override
  public Tweet postTweet(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException(POST_USAGE);
    }
    String tweet_text = args[1];
    try {
       tweet_text = URLEncoder.encode(tweet_text, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException("Invalid tweet text, URL encoding error", e);
    }
    String coord = args[2];
    String[] coordArray = coord.split(COORD_SEP);
    if (coordArray.length != 2 || StringUtil.isEmpty(tweet_text)) {
      throw new IllegalArgumentException(POST_USAGE);
    }
    double lat;
    double lon;
    try {
      lat = Double.parseDouble(coordArray[0]);
      lon = Double.parseDouble(coordArray[1]);
    } catch (Exception e) {
      throw new IllegalArgumentException(POST_USAGE, e);
    }

    Tweet postTweet = TweetUtil.buildTweet(tweet_text, lon, lat);
    return service.postTweet(postTweet);
  }

  @Override
  public Tweet showTweet(String[] args) {
    String id;
    String[] fields;
    if (args.length == 2) {
      id = args[1];
      return service.showTweet(id, null);
    } else if (args.length == 3) {
      id = args[1];
      fields = args[2].split(COMMA);
      return service.showTweet(id, fields);
    }
    throw new IllegalArgumentException(SHOW_USAGE);
  }

  @Override
  public List<Tweet> deleteTweet(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException(DELETE_USAGE);
    }
    String[] ids = args[1].split(COMMA);
    return service.deleteTweets(ids);
  }
}
