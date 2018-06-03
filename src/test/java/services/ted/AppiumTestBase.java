package services.ted;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppiumTestBase {

    protected static AndroidDriver<MobileElement> driver;

    @BeforeTest(alwaysRun = true)
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Google Pixel");
        File app = new File("bin/ted.apk");
        caps.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.ted.android");
        caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.ted.android.view.home.HomeActivity");
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Tools.setDriver(driver);
    }

    @Test
    public void loginTest() {
        MobileElement profile = driver.findElementByAccessibilityId("My talks");
        profile.click();
        MobileElement loginSection = driver.findElementById("myTalksLoggedOutActionLogin");
        loginSection.click();
        MobileElement emailInput = driver.findElementById("user_email");
        emailInput.sendKeys("echornobai@intersog.com");
        MobileElement pwdInput = driver.findElementById("user_password");
        pwdInput.sendKeys("testpass");
        MobileElement logInBtn = driver.findElementByAccessibilityId("Log in");
        logInBtn.click();
        MobileElement OKButton = driver.findElementById("button2");
        OKButton.click();
        MobileElement loggedInTitle = driver.findElementById("myTalksLoggedInTitle");
        Assert.assertEquals(loggedInTitle.getText(), "Elena Chornobai ");
    }

    @Test
    public void timeFilterTest() {
        //MobileElement surpriseSection = driver.findElementByAccessibilityId("Surprise me");
        //surpriseSection.click();
        Tools.swipeByCoords(109, 1370, 882, 1370);
        MobileElement filteredSection = driver.findElementByXPath("//android.widget.TextView[@text='Jaw-dropping']");
        filteredSection.click();
        MobileElement selectedTimeEl = driver.findElementById("timeTextView");
        int selectedSeconds = Integer.parseInt(selectedTimeEl.getText())*60;
        MobileElement continueBtn = driver.findElementById("surpriseMeAction");
        continueBtn.click();
        List<MobileElement> videos = driver.findElementsById("talkListItemRow");
        for (int i = 0; i < videos.size(); ++i) {
            List <MobileElement> textBoxes = videos.get(i).findElementsByClassName("android.widget.TextView");
            String videoDurationText = textBoxes.get(textBoxes.size() - 1).getText();
            String[] splitTime = videoDurationText.split(":");
            int videoDurationSeconds = Integer.parseInt(splitTime[0])*60 + Integer.parseInt(splitTime[1]);
            Assert.assertTrue(videoDurationSeconds <= selectedSeconds);
        }
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        driver.quit();

    }
}
