import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class WebFormTests {
    WebDriver driver;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";

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
    void textInputFieldTest() {
        WebElement textField = driver.findElement(By.id("my-text-id"));
        textField.sendKeys("test123");//ввод значения, ничего не возвращает
        String actualText = textField.getAttribute("value");
        assertEquals("test123", actualText);
    }

    @Test
    void passwordInputFieldTest() {
        WebElement passwordField = driver.findElement(By.name("my-password"));
        passwordField.sendKeys("qwerty123*");
        String actualPassword = passwordField.getAttribute("value");
        assertEquals("qwerty123*", actualPassword);
    }

    @Test
    void textareaFieldTest() {
        WebElement textarea = driver.findElement(By.name("my-textarea"));
        textarea.sendKeys("something");
        String actualTextArea = textarea.getAttribute("value");
        assertEquals("something", actualTextArea);
    }

    @Test
    void disabledInput() {
        WebElement inputField = driver.findElement(By.cssSelector(".form-control[name = 'my-disabled']"));
        assertFalse(inputField.isEnabled(), "The field should be disabled");

        //попытка ввести в disabled поле
        assertThrows(ElementNotInteractableException.class, () -> {inputField.sendKeys("Hello");});
    }

    @Test
    void readonlyInputFieldTest() {
        WebElement readonlyInputField = driver.findElement(By.xpath("//*[@name= 'my-readonly']"));
        String fieldAttribute = readonlyInputField.getAttribute("readonly");
        assertNotNull(fieldAttribute, "Атрибут readonly должен присутствовать");
    }

    @Test
    void checkboxesTest() {
        WebElement checkbox1 = driver.findElement(By.cssSelector(".form-check-input#my-check-1"));
        // assertEquals(true, checkbox1.isSelected());
        assertTrue(checkbox1.isSelected(), "Чекбокс выбран");
        checkbox1.click();
        assertFalse(checkbox1.isSelected(), "Чекбокс не выбран");

        WebElement checkbox2 = driver.findElement(By.cssSelector(".form-check-input#my-check-2"));
        assertFalse(checkbox2.isSelected(), "Чекбокс не выбран");
        checkbox2.click();
        assertTrue(checkbox2.isSelected(), "Чекбокс выбран");
    }

    @Test
    void radiobuttonsTest() {
        WebElement checkedRadio = driver.findElement(By.id("my-radio-1"));
        assertTrue(checkedRadio.isSelected(), "Checked radio");

        WebElement defaultRadio = driver.findElement(
                By.xpath("//*[@class = 'form-check-input' and @id = 'my-radio-2']"));
        assertFalse(defaultRadio.isSelected(), "default radio");
    }

    @Test
    void hyperlinkTest() {
        WebElement returnButton = driver.findElement(By.xpath("//*[@href = './index.html']"));
        returnButton.click();
        String currentUrl = driver.getCurrentUrl();
        String homepageUrl = "https://bonigarcia.dev/selenium-webdriver-java/index.html";

        assertEquals(homepageUrl, currentUrl);
    }

    @Test
    void dropdownSelectTest() {
        WebElement selectElement = driver.findElement(By.name("my-select"));
        Select list = new Select(selectElement);

        List<WebElement> options = list.getOptions();
        assertEquals(4, options.size());

        list.selectByVisibleText("Two");
        String selectedOption = list.getFirstSelectedOption().getText();
        assertEquals("Two", selectedOption);

        assertFalse(list.isMultiple(), "Можно выбрать только одно значение из списка");

        list.selectByIndex(1);
        assertEquals("One", list.getFirstSelectedOption().getText());

        list.selectByValue("3");
        assertEquals("Three", list.getFirstSelectedOption().getText());
    }


    @Test
    void dropdownDatalistTest() {
        WebElement input = driver.findElement(By.name("my-datalist"));
        String datalistId = input.getAttribute("list");

        WebElement datalist = driver.findElement(By.id(datalistId));
        List<WebElement> options = datalist.findElements(By.tagName("option"));

        assertEquals(5, options.size());

        input.sendKeys("San Francisco");
        assertEquals("San Francisco", input.getAttribute("value"), "Поле должно содержать введённое значение");

        input.clear();
        input.sendKeys("N");

        List<String> filteredOptions = new ArrayList<>();
        for (WebElement option : options) {
            String value = option.getAttribute("value").toLowerCase();
            if (value.contains("n")) {
                filteredOptions.add(option.getAttribute("value"));
            }
        }

        List<String> expectedOptions = List.of("San Francisco", "New York", "Los Angeles");
        assertEquals(expectedOptions, filteredOptions);
    }


    @Test
    void fileInputTest() {
        WebElement fileInput = driver.findElement(By.name("my-file"));

        String filePath = "C:\\Users\\Kate\\Downloads\\Telegram Desktop\\allure_report.png";
        fileInput.sendKeys(filePath);
        String showUploadedFileName = fileInput.getAttribute("value");

        assert showUploadedFileName != null;
        assertTrue(showUploadedFileName.contains("allure_report.png"));
    }

    @Test
    void submitButtonTest() throws InterruptedException {
        WebElement submitButton = driver.findElement(By.cssSelector(".btn.btn-outline-primary.mt-3")); //By.xpath("//button[text()='Submit']")
        submitButton.click();
        Thread.sleep(1000);

        WebElement submittedForm = driver.findElement(By.className("display-6"));

        assertEquals("Form submitted", submittedForm.getText());
    }

    @Test
    void colorPickerTest() {
        WebElement colorPicker = driver.findElement(By.xpath("//*[@name = 'my-colors']"));

        String newColor = "#d63384";
        colorPicker.sendKeys(newColor);
        String showNewColor = colorPicker.getAttribute("value");

        assertEquals(newColor, showNewColor);
    }


    @Test
    void datePickerTest() {
        WebElement datePickerField = driver.findElement(By.name("my-date"));
        String validDate  = "12/12/2024";
        datePickerField.sendKeys(validDate);

        assertEquals(validDate, datePickerField.getAttribute("value"));
        datePickerField.clear();

        String invalidDate = "hello world";
        datePickerField.sendKeys(invalidDate + Keys.ENTER);
        String value = datePickerField.getAttribute("value");
        LocalDate today = LocalDate.now();

        //проверим, что value содержит год, месяц и день сегодняшней даты (строки)
        assertTrue(value.contains(String.valueOf(today.getYear()))
                && value.contains(String.format("%02d", today.getMonthValue()))
                && value.contains(String.format("%02d", today.getDayOfMonth())));
    }

    @Test
    void exampleRangeTest() {
        WebElement exampleRange = driver.findElement(By.className("form-range"));
        String newRange = "3";
        //устанавливаем значение слайдера через JavaScript
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input'));",
                exampleRange,
                newRange
        );

        assertEquals(newRange, exampleRange.getAttribute("value"));
    }
}