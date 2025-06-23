import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import static org.junit.jupiter.api.Assertions.*;

public class DragAndDropTests {

    WebDriver driver;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/drag-and-drop.html";

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
    void dragAndDropTest() {
        WebElement draggable = driver.findElement(By.id("draggable"));
        WebElement targetDropForm = driver.findElement(By.id("target"));

        Point draggablePositionBefore = draggable.getLocation();

        Actions actions = new Actions(driver);
        actions.dragAndDrop(draggable, targetDropForm).perform();

        Point draggablePositionAfter = draggable.getLocation();
        Point targetPosition = targetDropForm.getLocation();

        assertNotEquals(draggablePositionAfter, draggablePositionBefore);
        assertEquals(targetPosition, draggablePositionAfter);
    }
}
