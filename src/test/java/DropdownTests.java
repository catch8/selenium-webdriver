import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DropdownTests {

    WebDriver driver;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/dropdown-menu.html";

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.get(BASE_URL);
        driver.manage().window().maximize();
    }


    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void leftClickTest() {
        WebElement leftClickDropdown = driver.findElement(By.id("my-dropdown-1"));
        Actions actions = new Actions(driver);
        actions.click(leftClickDropdown).perform();

        WebElement options = driver.findElement(By.xpath("//ul[@class='dropdown-menu show']"));
        assertTrue(options.isDisplayed());

        WebElement option = driver.findElement(By.xpath("//a[text() = 'Separated link']"));
        option.click();
        assertFalse(options.isDisplayed());

    }

    @Test
    void rightClickTest() {
        WebElement rightClickDropdown = driver.findElement(By.id("my-dropdown-2"));
        Actions actions = new Actions(driver);
        actions.contextClick(rightClickDropdown).perform();

        WebElement options = driver.findElement(By.xpath("//ul[@id= 'context-menu-2']"));
        assertTrue(options.isDisplayed());

        WebElement option = driver.findElement(By.xpath("//ul[@id= 'context-menu-2']//a[text() = 'Something else here']"));
        option.click();
        assertFalse(options.isDisplayed());
    }

    @Test
    void doubleClickTest() {
        WebElement doubleClickDropdown = driver.findElement(By.id("my-dropdown-3"));
        Actions actions = new Actions(driver);
        actions.doubleClick(doubleClickDropdown).perform();

        WebElement options = driver.findElement(By.xpath("//ul[@class = 'dropdown-menu' and @id= 'context-menu-3']"));
        assertTrue(options.isDisplayed());

        WebElement option = driver.findElement(By.xpath("//ul[@id= 'context-menu-3']//a[text() = 'Another action']"));
        option.click();
        assertFalse(options.isDisplayed());
    }
}
