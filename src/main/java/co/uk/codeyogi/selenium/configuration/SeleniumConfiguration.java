package co.uk.codeyogi.selenium.configuration;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SeleniumConfiguration {

    @PostConstruct
    void postConstruct() {
        String chromedriverLocation = "/usr/bin/chromedriver";
        System.setProperty("webdriver.chrome.driver", chromedriverLocation);
        // Print chrome driver location
        System.out.println("* chromedriver location: " + chromedriverLocation + " *");
    }


    @Bean
    public ChromeDriver driver() {
        final ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("--headless");
        return new ChromeDriver(chromeOptions);
    }
}
