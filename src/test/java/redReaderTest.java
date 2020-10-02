/**
 * redReaderTest es una clase que realiza pruebas de Record & Replay para aplicacion
 * RedReader usando Appium, Selenium, TestNG
 *
 * @author Luis Fernando Martinez
 * @version 1.0
 * @since 2020-09-30
 */

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class redReaderTest {

    public AndroidDriver<MobileElement> driver;
    public WebDriverWait wait;

    /**
     * Este metodo instrumenta la configuración necesaria para lanzar el emulador
     * y la aplicación a probar
     * @return Nothing.
     * @exception MalformedURLException On input error.
     * @see MalformedURLException
     */
    @BeforeMethod
    public void setup() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("deviceName", "Nexus_5_API_26_2");
        caps.setCapability("udid", "emulator-5554"); //DeviceId from "adb devices" command
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "8.0.0");
        caps.setCapability("skipUnlock", "true");
        caps.setCapability("appPackage", "org.quantumbadger.redreader");
        caps.setCapability("appActivity", "org.quantumbadger.redreader.activities.MainActivity");
        caps.setCapability("noReset", "false");
        driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
        wait = new WebDriverWait(driver, 10);
    }

    /**
     * Este metodo se encarga de aceptar el mensaje de bienvenida de la app
     * para dejar la aplicacion en la pantalla inicial
     * y la aplicación a probar
     * @return Nothing.
     */
    private void setupReddist() {
        wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.id("android:id/button2"))).click();
    }

    /**
     * Este metodo valida que el color del bar tool corresponda al de la aplicacion original
     * @return Nothing.
     * @exception Exception
     * @see Exception
     */
    @Test
    public void colorHeaderTest() throws Exception {
        setupReddist();
        MobileElement elem = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("org.quantumbadger.redreader:id/rr_actionbar_toolbar")));
        org.openqa.selenium.Point point = elem.getCenter();
        int centerx = point.getX();
        int centerY = point.getY();
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage image = ImageIO.read(scrFile);
        int clr = image.getRGB(centerx, centerY);
        int red = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue = clr & 0x000000ff;
        //el oraculo se valida contra los colores RGB originales
        Assert.assertEquals(red, 211);
        Assert.assertEquals(green, 47);
        Assert.assertEquals(blue, 47);
    }

    /**
     * Este metodo valida que el mensaje de bienvenida corresponda al de la aplicacion original
     * @return Nothing.
     * @exception Exception
     * @see Exception
     */
    @Test
    public void welcomeMessageTest() throws Exception {
        //Click and pass Splash
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.id("android:id/message"))).getText();
        //Click to continued as anonymous
        wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.id("android:id/button2"))).click();
        //oraculo basado en el mensaje de bienvenida
        Assert.assertEquals(text, "You are not currently logged in. You can access the account list by selecting Accounts from the menu.");
    }

    /**
     * Este metodo valida que la opcion suscribedSubreddits este en la pantalla del home
     * @return Nothing.
     * @exception Exception
     * @see Exception
     */
    @Test
    public void suscribedRedditsTest() throws Exception {
        setupReddist();
        String subscribed = "\t\n" +
                "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget." +
                "FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget." +
                "FrameLayout/android.view.ViewGroup/android.support.v7.widget.RecyclerView/android.widget.TextView";
        //El oraculo se basa en que este elemento debe estar presente y sino se genera un crash en el caso de prueba
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(subscribed)));
    }

    /**
     * Este metodo valida que al hacer click en la opcion openPage este lleve a la pagina openpage
     * @return Nothing.
     * @exception Exception
     * @see Exception
     */
    @Test
    public void openPageTest() throws Exception {
        setupReddist();
        String xpathOpen = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.support.v7.widget.RecyclerView/android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.TextView";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOpen))).click();
        String spathFronLabel = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.support.v7.widget.RecyclerView/android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.TextView[1]";
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(spathFronLabel))).getText();
        //Oraculo basado en el label de la pagina que se abre
        Assert.assertEquals(text, "Front Page");
    }

    /**
     * Este metodo valida que al hacer click en la opcion all subreddits este lleve a la pagina allsubredits
     * @return Nothing.
     * @exception Exception
     * @see Exception
     */
    @Test
    public void allSubredditsTest() throws Exception {
        setupReddist();
        String xpathOpen = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.support.v7.widget.RecyclerView/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.TextView";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOpen))).click();
        String xpathSubredditsLabel = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.support.v7.widget.RecyclerView/android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.TextView[1]";
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathSubredditsLabel))).getText();
        //Oraculo basado en el label de la pagina que se abre
        Assert.assertEquals(text, "All Subreddits");
    }

    /**
     * Este metodo valida que al hacer click en la opcion account se desoliegue el menu contextual de accounts
     * @return Nothing.
     * @exception Exception
     * @see Exception
     */
    @Test
    public void accountOptionsTest() throws Exception {
        setupReddist();
        String xpathOptions = "\t\n" +
                "//android.widget.ImageView[@content-desc=\"More options\"]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOptions))).click();
        String xpathAccountsOption = "\t\n" +
                "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathAccountsOption))).click();

        String text = wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.id("android:id/alertTitle"))).getText();

        wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.id("android:id/button3"))).click();
        //Oraculo basado en el label de la pagina que se abre
        Assert.assertEquals(text, "Reddit Accounts");
    }

    /**
     * Este metodo valida que al hacer click en la opcion Themes se despliegue el menu contextual de Themes
     * @return Nothing.
     * @exception Exception
     * @see Exception
     */
    @Test
    public void themeOptionsTest() throws Exception {
        setupReddist();
        String xpathOptions = "\t\n" +
                "//android.widget.ImageView[@content-desc=\"More options\"]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOptions))).click();
        String xpathThemes = "\t\n" +
                "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathThemes))).click();

        String text = wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.id("android:id/alertTitle"))).getText();
        String xpathRedOption = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.CheckedTextView[1]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathRedOption))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.id("android:id/button3"))).click();
        //Oraculo basado en el label de la pagina que se abre
        Assert.assertEquals(text, "Theme");
    }

    /**
     * Este metodo valida que la seccion Settings se encuewntre el label Black
     * @return Nothing.
     * @exception Exception
     * @see Exception
     */
    @Test
    public void settingsApperaanceTest() throws Exception {
        setupReddist();
        String xpathOptions = "\t\n" +
                "//android.widget.ImageView[@content-desc=\"More options\"]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOptions))).click();
        String xpathSettings = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[3]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathSettings))).click();
        String xpathAppereance = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.LinearLayout[1]/android.widget.RelativeLayout/android.widget.TextView";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathAppereance))).click();
        String xpathColor = "\t\n" +
                "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[3]/android.widget.RelativeLayout/android.widget.TextView[2]";
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathColor))).getText();
        String xpathUp = "//android.widget.ImageButton[@content-desc=\"Navigate up\"]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathUp))).click();
        //Oraculo basado en un label que debe estar en la pagina settings
        Assert.assertEquals(text, "Black");
    }

    /**
     * Este metodo valida que la seccion About los elementos se muestren en el mismo orden que la aplicacion original
     * @return Nothing.
     * @exception Exception
     * @see Exception
     */
    @Test
    public void aboutTest() throws Exception {
        setupReddist();
        String xpathOptions = "\t\n" +
                "//android.widget.ImageView[@content-desc=\"More options\"]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOptions))).click();
        String xpathSettings = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[3]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathSettings))).click();
        String xpathAbout = "\t\n" +
                "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.LinearLayout[7]/android.widget.RelativeLayout/android.widget.TextView";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathAbout))).click();

        List<MobileElement> linearLayoutElements = (List<MobileElement>) driver.findElementsByClassName("android.widget.TextView");
        for (MobileElement e : linearLayoutElements) {
            System.out.println(e.getText());
        }
        //este oraculo esta basado en el orden que se deben mostrar los elementos
        Assert.assertEquals(linearLayoutElements.get(4).getText(), "License");
    }

    /**
     * Este metodo valida que al abrir la seccion Whatsnew se despliegue la informacion correctamente
     * @return Nothing.
     * @exception Exception
     * @see Exception
     */
    @Test
    public void aboutWhatsNewTest() throws Exception {
        setupReddist();
        String xpathOptions = "\t\n" +
                "//android.widget.ImageView[@content-desc=\"More options\"]";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOptions))).click();
        String xpathSettings = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[3]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathSettings))).click();
        String xpathAbout = "\t\n" +
                "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.ListView/android.widget.LinearLayout[7]/android.widget.RelativeLayout/android.widget.TextView";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathAbout))).click();
        String xpathDetail = "\t\n" +
                "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[2]/android.widget.RelativeLayout/android.widget.TextView";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathDetail))).click();
        String xpathVersion = "\t\n" +
                "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.ScrollView/android.widget.LinearLayout/android.widget.TextView[1]";
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathVersion))).getText();
        //oraculo basado en la informacion que se debe mostrar al hacer clic en whatsnew
        Assert.assertEquals(text, "1.9.10");
    }

    /**
     * Este metodo baja la aplicacion para restablecer las precondiciones de los escenarios
     * @return Nothing.
     */
    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}