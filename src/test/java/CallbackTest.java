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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CallbackTest {
    private WebDriver driver;

    @BeforeAll
    public  static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void testFoolValidForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванович Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79644361516");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement actualElement = driver.findElement(By.cssSelector("[data-test-id=order-success]"));
        String actualText = actualElement.getText().trim();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    public void testFormNoName() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79644361516");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement actualElement = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        String actualText = actualElement.getText().trim();
        assertEquals("Поле обязательно для заполнения", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    public void testFormNoPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement actualElement = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String actualText = actualElement.getText().trim();
        assertEquals("Поле обязательно для заполнения", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    public void testFormInvalidName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Bobrova");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79644361516");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement actualElement = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        String actualText = actualElement.getText().trim();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    public void testFormInvalidPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("9644361516");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector(".button")).click();
        WebElement actualElement = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        String actualText = actualElement.getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", actualText);
        assertTrue(actualElement.isDisplayed());
    }
}
