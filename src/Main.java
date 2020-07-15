import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {
    public static void main(String [] args){
        ChromeDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        System.setProperty("WebDriver.Chrome.driver", "../chromedriver");
        driver.navigate().to("https://www.imdb.com/search/title/?groups=top_250&sort=user_rating,desc");

    }
}
