package ca.jrvs.apps.twitter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ca.jrvs.apps.twitter")
public class TwitterCLISprintBoot implements CommandLineRunner {

  private final TwitterCLIApp app;

  @Autowired
  public TwitterCLISprintBoot(TwitterCLIApp app) {
    this.app = app;
  }

  public static void main(String[] args) {

    SpringApplication app = new SpringApplication(TwitterCLISprintBoot.class);

    // Turn off web
    app.setWebApplicationType(WebApplicationType.NONE);
    app.run(args);
  }

  @Override
  public void run(String... args) {
    System.out.print("Hi");
    app.run(args);
  }
}
