package ru.netology.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll

    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSendOrderForm() {
        driver.findElement(By.cssSelector("input[name='name']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("input[name='phone']")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        assertTrue(result.isDisplayed());
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", result.getText().trim());
    }

    @Test
    void noFillOfNameField() {
        driver.findElement(By.cssSelector("input[name='phone']")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        WebElement subtext = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));

        assertTrue(subtext.isDisplayed());
        assertEquals("Поле обязательно для заполнения", subtext.getText().trim());
    }

    @Test
    void fillOfNameFieldWithInvalidData() {
        driver.findElement(By.cssSelector("input[name='name']")).sendKeys("Jorick");
        driver.findElement(By.cssSelector("input[name='phone']")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        WebElement subtext = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));

        assertTrue(subtext.isDisplayed());
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", subtext.getText().trim());
    }

    @Test
    void noFillOfPhoneField() {
        driver.findElement(By.cssSelector("input[name='name']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        WebElement subtext = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));

        assertTrue(subtext.isDisplayed());
        assertEquals("Поле обязательно для заполнения", subtext.getText().trim());
    }

    @Test
    void fillOfPhoneFieldWithInvalidData() {
        driver.findElement(By.cssSelector("input[name='name']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("input[name='phone']")).sendKeys("+7укенгшщзьт");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();
        WebElement subtext = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));

        assertTrue(subtext.isDisplayed());
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", subtext.getText().trim());
    }

    @Test
    void doNotClickCheckBox() {
        driver.findElement(By.cssSelector("input[name='name']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("input[name='phone']")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[type='button']")).click();
        WebElement notCLickedCheckbox = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text"));

        assertTrue(notCLickedCheckbox.isDisplayed());
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", notCLickedCheckbox.getText().trim());
    }

    @Test
    void nothingFilled() {
        driver.findElement(By.cssSelector("[type='button']")).click();
        WebElement subtext = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));

        assertTrue(subtext.isDisplayed());
        assertEquals("Поле обязательно для заполнения", subtext.getText().trim());
    }


}
