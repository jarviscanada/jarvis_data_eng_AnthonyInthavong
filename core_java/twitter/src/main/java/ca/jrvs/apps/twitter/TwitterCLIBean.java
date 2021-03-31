//package ca.jrvs.apps.twitter;
//
//import ca.jrvs.apps.twitter.controller.TwitterController;
//import ca.jrvs.apps.twitter.dao.CrdDao;
//import ca.jrvs.apps.twitter.Controller;
//import ca.jrvs.apps.twitter.dao.TwitterDao;
//import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
//import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
//import ca.jrvs.apps.twitter.service.Service;
//import ca.jrvs.apps.twitter.service.TwitterService;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.context.annotation.Bean;
//
////@Configuration
//public class TwitterCLIBean {
//
//  public static void main(String[] args) {
//    ApplicationContext context = new AnnotationConfigApplicationContext(TwitterCLIBean.class);
//    TwitterCLIApp app = context.getBean(TwitterCLIApp.class);
//    app.run(args);
//  }
//
//  @Bean
//  public TwitterCLIApp twitterCLIApp(Controller controller) {
//    return new TwitterCLIApp(controller);
//  }
//
//  @Bean
//  public TwitterController twitterController(Service service) {
//    return new TwitterController(service);
//  }
//
//  @Bean
//  public TwitterService twitterService(CrdDao dao) {
//    return new TwitterService(dao);
//  }
//
//  @Bean
//  public TwitterDao twitterDao(HttpHelper httpHelper) {
//    return new TwitterDao(httpHelper);
//  }
//
//  @Bean
//  HttpHelper helper() {
//    String consumerKey = System.getenv("consumerKey");
//    String consumerSecret = System.getenv("consumerSecret");
//    String accessToken = System.getenv("accessToken");
//    String tokenSecret = System.getenv("tokenSecret");
//    return new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
//  }
//}
