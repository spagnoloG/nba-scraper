package co.uk.codeyogi.selenium.service;

import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Service;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Scanner;


@Service
@AllArgsConstructor
public class ScraperService {
    private static final String URL = "https://www.nba.com/stats";

    private final ChromeDriver driver;

    @PostConstruct
    void postConstruct() {
        scrape();
    }

    public void scrape() {
        // Read from scanner
        System.out.print("Enter player name and surname: ");
        Scanner sc = new Scanner(System.in);
        String playerName = sc.nextLine();
        sc.close();

        driver.get(URL);

        System.out.println("Player name: " + playerName);

        // Wait for cookies page to appear, then to finish loading page, then redirect to page
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler"))).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/main/div/div/div[3]/div[1]/section[1]/div/div[1]/div[1]/a"))).click();
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//table/*")));
        List<WebElement> playersList = driver.findElements(By.xpath("//table/tbody/tr/td[contains(@class, 'player')]/a"));

        // Go through list of players
        final String[] player_link = new String[1];
        playersList.forEach(player -> {
            if(player.getText().equals(playerName)) {
                player_link[0] = player.getAttribute("href");
            }
        });

        // If player is not found, quit
        if (player_link[0] == null) {
            System.out.println("Could not find player you are searching for!");
            driver.quit();
            return;
        }

        // redirect to player page
        driver.navigate().to(player_link[0]);
        System.out.println(" ...Redirecting --> " + player_link[0]);

        // wait for page to load, then scrape points
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("/html/body/main/div/div/div/div[4]/div/div/div/div/nba-stat-table[1]/div[2]/div[1]/table/tbody/tr[1]/td[10]")));

        List<WebElement> threePAPoints = driver.findElements(By.xpath("/html/body/main/div/div/div/div[4]/div/div/div/div/nba-stat-table[1]/div[2]/div[1]/table/tbody/tr[*]/td[10]"));
        List<WebElement> year = driver.findElements(By.xpath("/html/body/main/div/div/div/div[4]/div/div/div/div/nba-stat-table[1]/div[2]/div[1]/table/tbody/tr[*]/td[1]"));

        // print Values
        for(int i = 0; i < threePAPoints.size(); i++) {
            System.out.print(year.get(i).getText() + " ");
            System.out.println(threePAPoints.get(i).getText());
        }

        driver.quit();
    }

}
