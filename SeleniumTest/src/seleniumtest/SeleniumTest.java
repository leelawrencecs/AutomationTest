/*
Log-in to application from a web browser (e.g. Internet Explorer, Firefox, Chrome, etc.).
Navigate to "My Accounts" using tab near top of home page.
Select "Adams, Bob" from the My Accounts list.
Near the top of the Account page is a "Record A Call" button. Select this button.
Validate the Call Report page is displayed.
On Call Report page, select "Mass Add Promo Call" from the Record Type drop down list.
On Call Report page, the script should select Cholecap and Labrinone in Detail Priority section.
Under Call Discussions section, make sure a subsection appeared for both Cholecap and Labrinone. Also make sure the "Product" fields are set to the respective product (one should be set to Cholecap, the other should be set to Labrinone).
On the same Call Discussion section, make sure each section appears in order the product was selected. If Labrinone was selected first, then Labrinone should show up first (from top down) under Call Discussion section.
In Samples and Promotional Items section, select "QNASL Co-Pay Card", and change quantity to 2.
Call report should be saved using "Saved" button with a check for successful submission. As final step, the script should logout (found on drop down menu in upper right).
*/

package seleniumtest;

import java.util.ArrayList;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SeleniumTest {


    public static void main(String[] args) {
        
        //Gets the name of the operating system
        String operatingSystem = System.getProperty("os.name").toLowerCase();
        
        //Gets the proper driver from root directory which would allow us to do this test
        if(operatingSystem.contains("mac")) System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/chromedriver");
        else System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\chromedriver.exe");
        
        //Allows for driver to work on Google Chrome and opens Google Chrome up
        WebDriver driver = new ChromeDriver();
        
        /*
        Creating a WebDriverWait which will allow for wait functions. Variable will determine
        the duration of the wait before failing the test
        */
        long waitTime = 20;
        WebDriverWait wait = new WebDriverWait(driver, waitTime); 
        
        //The website that is being tested
        String url = "https://login.salesforce.com/";
        
        //Login information
        String username = "bb67@bb2.com";
        String password = "bugb1234";

        //Name of person required
        String name = "Adams, Bob";
        
        //The following creates an ArrayList of products that need to be selected
        ArrayList<String> productsToAdd = new ArrayList<>();
        productsToAdd.add("Cholecap");
        productsToAdd.add("Labrinone");
        
        
        //The following creates an ArrayList of Samples and Promotional Items that need to be selected
        ArrayList<Object> samplesAndPromotionalItems = new ArrayList<>();
        samplesAndPromotionalItems.add("QNASL Co-Pay Card");
        samplesAndPromotionalItems.add(2);
        
        //Initializing values into the Selenium Test
        IndividualTests test = new IndividualTests(driver, wait, url, username, password, name, productsToAdd, samplesAndPromotionalItems);
        
        //Run commands in the Selenium Test
        test.runTest();
        

    }
    
}