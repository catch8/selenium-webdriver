import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.CookieStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageTests {
    WebDriver driver;//переменная экземпляра класса
    //String baseURL = "https://bonigarcia.dev/selenium-webdriver-java/";
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();// инициализация переменной КЛАССА
        driver.get(BASE_URL);
        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        driver.quit();//закрывает весь браузер
    }

    @Test
    void openHomePageTest() {
        String actualtitle = driver.getTitle();

        Assertions.assertEquals("Hands-On Selenium WebDriver with Java", actualtitle);
    }

    @Test
    void openWebFormTest() {
        String webFormUrl = "web-form.html";
        driver.findElement(By.linkText("Web form")).click();
        String currentUrl = driver.getCurrentUrl();

        WebElement title = driver.findElement(By.className("display-6"));

        Assertions.assertEquals(BASE_URL + webFormUrl, currentUrl);
        Assertions.assertEquals("Web form", title.getText());
    }

    @Test
    void openNavigationTest() {
        //Найти ссылку и кликнуть
        WebElement navigationLink = driver.findElement(
                By.xpath("//h5[text() = 'Chapter 3. WebDriver Fundamentals']/../a[contains(@href, 'navigation1')]")
        );
        navigationLink.click();

        //Проверить, что URL корректный
        String navigationUrl = "navigation1.html";
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertEquals(BASE_URL + navigationUrl, currentUrl);

        //Проверить заголовок новой страницы
        WebElement pageTitle = driver.findElement(By.className("display-6"));
        Assertions.assertEquals("Navigation example", pageTitle.getText());
    }

    @Test
    void openCookiesTest(){
        WebElement cookiesLink = driver.findElement(
                By.xpath("//h5[contains(text(), 'Chapter 4')]/../a[contains(@href, 'cookies')]"));
        ////h5[contains(text(), 'Chapter 4')] - находит заголовок с текстом "Chapter 4".
        // /.. — поднимаемся к родителю (div.card-body)
        ///a[contains(@href, 'cookies')] — ищем ссылку внутри, у которой href содержит "cookies"
        cookiesLink.click();

        String cookiesUrl ="cookies.html";//"ab-testing.html"
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertEquals(BASE_URL+cookiesUrl, currentUrl);

        WebElement title = driver.findElement(By.className("display-6"));
        Assertions.assertEquals("Cookies", title.getText());
    }
    @Test
    void openUserMediaLinkTest() {
        WebElement userMediaLink = driver.findElement(By.xpath("//a[@href = 'get-user-media.html']"));
        userMediaLink.click();

        String userMediaUrl = "get-user-media.html";
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertEquals(BASE_URL + userMediaUrl, currentUrl);

        WebElement title = driver.findElement(By.className("display-6"));
        Assertions.assertEquals("Get user media", title.getText());
    }

    @Test
    void openLoginFormTest() {
        WebElement loginFormLink = driver.findElement(
                By.xpath("//h5[contains(text(),'Chapter 7')]/../a[contains(@href, 'login-form')]"));
        loginFormLink.click();

        String loginFormUrl = "login-form.html";
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertEquals(BASE_URL + loginFormUrl, currentUrl);

        WebElement title = driver.findElement(By.xpath("//h1[contains(text(),'Login form')]"));
        Assertions.assertEquals("Login form",title.getText());
    }

    @Test
    void checlLinksInChapter3Test() {
        // Заполняем
        var expectedList = new ArrayList<String>();
        expectedList.add(BASE_URL + "web-form.html");
        expectedList.add(BASE_URL + "navigation1.html");
        expectedList.add(BASE_URL + "dropdown-menu.html");
        expectedList.add(BASE_URL + "mouse-over.html");
        expectedList.add(BASE_URL + "drag-and-drop.html");
        expectedList.add(BASE_URL + "draw-in-canvas.html");
        expectedList.add(BASE_URL + "loading-images.html");
        expectedList.add(BASE_URL + "slow-calculator.html");

        // Находим все ссылки внутри Chapter 3
        var elements = driver.findElements(
                By.xpath("//*[@class='card-body' and h5[contains(text(),'Chapter 3')]]//a")
        );

        // Собираем фактические href всех найденных ссылок в разделе chapter 3
        var actualList = new ArrayList<String>();
        for (int i = 0; i < elements.stream().count(); i++) {
            actualList.add(elements.get(i).getAttribute("href"));
        }

        Assertions.assertEquals(expectedList, actualList);

    }

    @Test
    void checkLinksInChapters4to9Test() {
        Map<String, List<String>> expectedLinks = new HashMap<>();
        expectedLinks.put("Chapter 4", List.of(
                "long-page.html", "infinite-scroll.html", "shadow-dom.html",
                "cookies.html", "frames.html", "iframes.html", "dialog-boxes.html", "web-storage.html"
        ));
        expectedLinks.put("Chapter 5", List.of(
                "geolocation.html", "notifications.html", "get-user-media.html",
                "multilanguage.html", "console-logs.html"
        ));
        expectedLinks.put("Chapter 7", List.of(
                "login-form.html", "login-slow.html"
        ));
        expectedLinks.put("Chapter 8", List.of(
                "random-calculator.html"
        ));
        expectedLinks.put("Chapter 9", List.of(
                "download.html", "ab-testing.html", "data-types.html"
        ));

        for (String chapter : expectedLinks.keySet()) { // map is key- value, ключ (chapter)-значение (List ссылок)
            List<WebElement> linkElements = driver.findElements(
                    By.xpath("//*[@class='card-body' and h5[contains(text(), '" + chapter + "')]]//a")
            );

            List<String> actualLinks = new ArrayList<>();
            for (WebElement link : linkElements) {
                actualLinks.add(link.getAttribute("href").replace(BASE_URL, ""));
            }

            Assertions.assertEquals(expectedLinks.get(chapter), actualLinks, "Не совпадают в " + chapter);
        }
    }

    @Test
    void openAllLinksTest() throws InterruptedException {
        int qtyLinks = 0;
        List<WebElement> chapters = driver.findElements(By.cssSelector("h5.card-title")) ;
        for (WebElement chapter : chapters){
            System.out.println(chapter.getText());

            List<WebElement> links = chapter.findElements(By.xpath("./../a"));
            qtyLinks += links.size();
            for ( WebElement link : links){
                System.out.println(link.getText());
                link.click();
                //Thread.sleep(1000);
                driver.navigate().back();//возвращаемся назад
            }
        }
        Assertions.assertEquals(6,chapters.stream().count()); //или .size()
        Assertions.assertEquals(27, qtyLinks);
    }

    @Test
    void classesTest(){
        List<WebElement> links = driver.findElements(By.cssSelector(".btn.btn-outline-primary.mb-2"));
        Assertions.assertEquals(27, links.size());
    }
}


