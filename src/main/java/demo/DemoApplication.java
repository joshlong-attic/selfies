package demo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Josh Long
 *         <p/>
 *         TODO build a client that finds all tweets either tweeted or RT'd or
 *         TODO starred that have a photo attachment then download the photos
 *         TODO and the mention'd usernames, if any
 */
@SpringBootApplication
@EnableConfigurationProperties(SelfiesProperties.class)
public class DemoApplication {

    Log log = LogFactory.getLog(getClass());

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    Twitter twitter(SelfiesProperties properties) {
        return new TwitterTemplate(properties.getConsumerKey(),
                properties.getConsumerKeySecret(),
                properties.getAccessToken(),
                properties.getAccessTokenSecret());
    }

    //    @Bean
    CommandLineRunner runner(Twitter twitter) {
        return args -> {
            List<Tweet> tweets = twitter.timelineOperations()
                    .getFavorites();

            // show us what we're working w..
            tweets.forEach(x -> System.out.println(ToStringBuilder.reflectionToString(x,
                    ToStringStyle.MULTI_LINE_STYLE)));

            Collection<Selfie> tweetsWithMedia = tweets.stream()
                    .filter(t -> {
                        // does it have a file attachment?
                        return false;
                    })
                    .map(t -> {
                        return new Selfie(null, Collections.emptySet());
                    })
                    .collect(Collectors.toSet());

            tweetsWithMedia.forEach(System.out::println);

        };
    }
}


class Selfie {

    private String originalScreenName;

    private Set<URI> photoUris = new HashSet<>();

    public Selfie(String osgs, Set<URI> photoUris) {
        this.originalScreenName = osgs;
        this.photoUris = photoUris;
    }

    public String getOriginalScreenName() {
        return originalScreenName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Selfie) {
            Selfie other = Selfie.class.cast(obj);
            return StringUtils.hasText(other.originalScreenName) &&
                    StringUtils.hasText(this.originalScreenName) &&
                    other.originalScreenName.equals(this.originalScreenName) &&
                    other.photoUris.equals(this.photoUris);
        }
        return false;
    }

    public Set<URI> getPhotoUris() {
        return photoUris;
    }
}

@ConfigurationProperties("selfies")
class SelfiesProperties {

    private String consumerKeySecret, consumerKey;
    private String accessToken, accessTokenSecret;

    public String getConsumerKeySecret() {
        return consumerKeySecret;
    }

    public void setConsumerKeySecret(String consumerKeySecret) {
        this.consumerKeySecret = consumerKeySecret;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }
}