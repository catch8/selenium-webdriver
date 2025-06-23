import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NavigationTests {

    WebDriver driver;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/navigation1.html";

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
    void navigationTitleTest() {
        WebElement navigationTitleText = driver.findElement(By.xpath("//h1[@class = 'display-6']"));

        assertEquals("Navigation example", navigationTitleText.getText());
    }

    @Test
    void backToIndexLinkTest() throws InterruptedException {
        WebElement backToIndexLink = driver.findElement(By.xpath("//a[@href = 'index.html']"));
        backToIndexLink.click();
        Thread.sleep(1000);

        assertEquals("https://bonigarcia.dev/selenium-webdriver-java/index.html", driver.getCurrentUrl());
    }

    @Test
    void nextButtonTest() {
        WebElement secondPage = driver.findElement(By.xpath("//a[@class= 'page-link' and text() = '2']"));
        secondPage.click();

        WebElement btnNext = driver.findElement(By.xpath("//a[@class = 'page-link' and text() = 'Next']"));
        assertEquals("Next", btnNext.getText());
        btnNext.click();

        WebElement disabledBtnNext = driver.findElement(By.xpath("//a[text() = 'Next']/parent::li"));
        String classes = disabledBtnNext.getAttribute("class"); // вернет "page-item disabled"
        assert classes != null;
        assertTrue(classes.contains("disabled"), "Next button should be disabled");
    }

    @Test
    void previousButtonTest() {
        WebElement secondPage = driver.findElement(By.xpath("//a[@class= 'page-link' and text() = '2']"));
        secondPage.click();

        WebElement btnPrevious = driver.findElement(By.xpath("//a[@class = 'page-link' and text() = 'Previous']"));
        assertEquals("Previous", btnPrevious.getText());
        btnPrevious.click();

        WebElement disabledBtnPrevious = driver.findElement(By.xpath("//a[text() = 'Previous']/parent::li"));
        String classes = disabledBtnPrevious.getAttribute("class"); // вернет "page-item disabled"
        assert classes != null;
        assertTrue(classes.contains("disabled"), "Previous button should be disabled");
    }

    @Test
    void paginationNavigationTest() {
        //проверка страницы 1
        assertEquals(BASE_URL, driver.getCurrentUrl());
        WebElement paragraph1 = driver.findElement(By.cssSelector("p.lead"));
        assertEquals(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                paragraph1.getText());

        //переход на страницу 3
        driver.findElement(By.xpath("//a[@class='page-link' and text()='3']")).click();
        assertEquals("https://bonigarcia.dev/selenium-webdriver-java/navigation3.html", driver.getCurrentUrl());

        WebElement paragraph3 = driver.findElement(By.cssSelector("p.lead"));
        assertEquals(
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                paragraph3.getText());

        //переход на страницу 2 через кнопку Previous
        WebElement btnPrevious = driver.findElement(By.xpath("//a[@class='page-link' and text()='Previous']"));
        assertEquals("Previous", btnPrevious.getText());
        btnPrevious.click();
        assertEquals("https://bonigarcia.dev/selenium-webdriver-java/navigation2.html", driver.getCurrentUrl());

        WebElement paragraph2 = driver.findElement(By.cssSelector("p.lead"));
        assertEquals(
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
                paragraph2.getText());
    }
}
