package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@org.springframework.stereotype.Service
public class TwitterService implements Service {

  private final static Logger logger = LoggerFactory.getLogger(Service.class);

  private CrdDao dao;

  public TwitterService(CrdDao dao) {
    this.dao = dao;
  }


  @Override
  public Tweet postTweet(Tweet tweet) {
    //Business logic:
    //e.g text length, lat/lon range, id format
    validatePostTweet(tweet);
    
    //create tweet via dao
    return (Tweet) this.dao.create(tweet);
  }

  void validatePostTweet(Tweet tweet) throws IllegalArgumentException {
    List<Double> coordinates = tweet.getCoordinates().getCoordinates();
    double lon = coordinates.get(1);
    double lat = coordinates.get(0);
    int textLength = tweet.getText().length();
    // check if tweet text exceeds 140 characters
    if (textLength > 140) {
      logger.error("Tweet text cannot be longer than 140 characters: {}", textLength);
      throw new IllegalArgumentException("Tweet text cannot be longer than 140 characters");
    }
    // longitude
    else if (-180 > lon || lon > 180) {
      logger.error("Longitude must be in range [-180, 180]: {}", lon);
      throw new IllegalArgumentException("Longitude must be in range [-180, 180]");
    }
    // latitude
    else if (-90 > lat || lat > 90) {
      logger.error("Latitude must be in range [-90, 90]: {}", lat);
      throw new IllegalArgumentException("Latitude must be in range [-90, 90]");
    }

  }

  @Override
  public Tweet showTweet(String id, String[] fields) {
    validateId(id);
    return (Tweet) dao.findById(id);
  }

  void validateId(String id) {
    try {
      Long.parseLong(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("ID: " + id + " is invalid");
    }
  }

  @Override
  public List<Tweet> deleteTweets(String[] ids) {
    Arrays.stream(ids).forEach(s -> validateId(s));
    List<Tweet> deletedTweets = new ArrayList<>();
    Arrays.stream(ids).forEach(s -> deletedTweets.add((Tweet) dao.deleteById(s)));
    return deletedTweets;
  }
}
