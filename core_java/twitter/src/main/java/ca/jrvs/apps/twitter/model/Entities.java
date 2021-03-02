package ca.jrvs.apps.twitter.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "hashtags",
    "user_mentions"
})
public class Entities {

  @JsonProperty("hashtags")
  private List<Hashtag> hashtags = null;
  @JsonProperty("user_mentions")
  private List<UserMention> userMentions = null;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("hashtags")
  public List<Hashtag> getHashtags() {
    return hashtags;
  }

  @JsonProperty("hashtags")
  public void setHashtags(List<Hashtag> hashtags) {
    this.hashtags = hashtags;
  }

  @JsonProperty("user_mentions")
  public List<UserMention> getUserMentions() {
    return userMentions;
  }

  @JsonProperty("user_mentions")
  public void setUserMentions(List<UserMention> userMentions) {
    this.userMentions = userMentions;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}