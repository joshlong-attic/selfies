package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

/**
 * @author Josh Long
 *
 * TODO build a client that finds all tweets either tweeted or RT'd or
 * TODO starred that have a photo attachment then download the photos
 * TODO and the mention'd usernames, if any
 *
 */
@SpringBootApplication
@EnableConfigurationProperties(SelfiesProperties.class)
public class DemoApplication {

    @Bean
    Twitter twitter( SelfiesProperties properties) {
        return new TwitterTemplate( properties.getConsumerKey(),
                properties.getConsumerKeySecret(),
                properties.getAccessToken() ,
                properties.getAccessTokenSecret());
    }

    @Bean
    CommandLineRunner runner(Twitter twitter) {
        return args -> {
            twitter.timelineOperations()
                    .getHomeTimeline()
                    .forEach(System.out::println);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@ConfigurationProperties("selfies")
class SelfiesProperties {

    private String consumerKeySecret, consumerKey;
    private String accessToken, accessTokenSecret;

    public String getConsumerKeySecret() {
        return consumerKeySecret;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setConsumerKeySecret(String consumerKeySecret) {
        this.consumerKeySecret = consumerKeySecret;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }
}