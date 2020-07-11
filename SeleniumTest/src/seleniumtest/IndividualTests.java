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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class IndividualTests {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private WebElement element;
    private Select select;
    private String url;
    private String userId;
    private String password;
    private String name;
    private ArrayList<String> productsToAdd;
    private ArrayList<Object> samplesAndPromotionalItems;
    private int ordering;
    
    IndividualTests(WebDriver driver, WebDriverWait wait, String url, String userId, String password, String name, 
            ArrayList<String> productsToAdd, ArrayList<Object> samplesAndPromotionalItems)
    {
        setDriver(driver);
        setWait(wait);
        setUrl(url);
        setUserId(userId);
        setPassword(password);
        setName(name);
        setProductsToAdd(productsToAdd);
        setSamplesAndPromotionalItems(samplesAndPromotionalItems);
    }
    
    public void runTest()
    {
        
        goToWebsite();
        checkIfWebsiteIsLoaded();
        enterUsername();
        enterPassword();
        clickLogIn();
        checkIfLoggedIn();
        clickMyAccounts();
        checkIfYouAreInMyAccounts();
        makingSureNameIsFirstNameLastName();
        enterName();
        clickTheGoButton();
        checkIfNameHasBeenSearched();
        clickOnName();
        checkIfInPersonsAccount();
        clickOnRecordACall();
        checkIfOnNewCallReportPage();
        changeRecordTypeToMassAddPromoCall();
        checkIfOnNewMassAddPromoCallPage();
        selectProducts();
        selectSamplesAndPromotionalItemsAndQuantity();
        clickSave();
        checkIfSaveWasSuccessful();
        clickLogOut();
        checkIfLogoutWasSuccessful();
        
    }
    
    
    //Checks if the URL contains "login.salesforce" in it to ensure that you have logged out
    public void checkIfLogoutWasSuccessful()
    {
        System.out.println("Checking if log out was successful...");
        
        try
        {
            //gives time to let page load before determining if the logout failed
            wait.until(ExpectedConditions.urlContains("login.salesforce"));
        }
        catch(Exception e)
        {
            System.out.println("Failed to successfully log out");
        }
        
        System.out.println("You have successfully logged out");
    }
    
    
    //Clicks logout button
    public void clickLogOut()
    {
        System.out.println("Clicking logout button...");
        
        try
        {
            //clicks the tab of the name with the id "userNavLabel" and then selects the Logout option
            driver.findElement(By.id("userNavLabel")).click();
            driver.findElement(By.linkText("Logout")).click();
        }
        catch(Exception e)
        {
            System.out.println("Failed to click logout button");
        }
        
        System.out.println("Logout button has been clicked");
    }
    
    
    //Checks to see if the option to print page is available. This option is only avaliable if the save was successful.
    public void checkIfSaveWasSuccessful()
    {
        System.out.println("Checking if save was successful...");
        
        try
        {
            //checks if the option to print the page is available
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@ng-click='pageCtrl.printURL(pageCtrl.metadata.printURL)']")));
        }
        catch(Exception e)
        {
            System.out.println("Save was unsuccessful");
            System.exit(0);
        }
        
        System.out.println("Save was successful");
    }
    
    
    //Clicks save button
    public void clickSave()
    {
        System.out.println("Clicking 'save' button...");
        
        try
        {
            //clicks the save button
            driver.findElement(By.xpath("//input[@type='button' and @ng-hide='meta.hide' and @name='Save']")).click();
        }
        catch(Exception e)
        {
            System.out.println("Failed to click the 'save' button");
            System.exit(0);
        }
        
        System.out.println("Save button has been successfully clicked");
    }
    
    
    //Goes through the samples and promotional items list and selects them and inputs it's quantity
    public void selectSamplesAndPromotionalItemsAndQuantity()
    {
        
        for(int i = 0; i < samplesAndPromotionalItems.size(); i++)
        {
            System.out.println(String.format("Selecting %s...", samplesAndPromotionalItems.get(i)));
            
            try
            {
                //clicks on items from the ArrayList samplesAndPromotionalItems
                driver.findElement(By.xpath(String.format("//table[@name='call2_sample_vod__c']//tbody//tr//td//table//tbody//tr//td//label[normalize-space()='%s']//preceding-sibling::input", samplesAndPromotionalItems.get(i)))).click();
            }
            catch(Exception e)
            {
                System.out.println(String.format("Unable to select %s", samplesAndPromotionalItems.get(i)));
                System.exit(0);
            }
            
            //clears the quantity text field and then inserts the user selected quantity
            driver.findElement(By.xpath(String.format("//span[contains(text(),'%s')]//parent::span//parent::td//following-sibling::td//span//span[@ng-switch-when=\"Quantity_vod__c\"]//input", samplesAndPromotionalItems.get(i)))).clear();
            driver.findElement(By.xpath(String.format("//span[contains(text(),'%s')]//parent::span//parent::td//following-sibling::td//span//span[@ng-switch-when=\"Quantity_vod__c\"]//input", samplesAndPromotionalItems.get(i)))).sendKeys("" + samplesAndPromotionalItems.get(++i));
            
        }

    }
    
    /*
    Checks that newly selected products are on the bottom of the list. Every time an item is selected, all above items will
    have that item's xpath's "selected" equal to "selected". Keeping track of this occurence size will ensure that newly selected items are on the bottom.
    */
    public void checkIfProductSectionOrderIsCorrect(int i)
    {
        //Gets the number of times an item of the following xpath occurs
        ordering = driver.findElements(By.xpath(String.format("//option[@label='%s' and @selected='selected']", productsToAdd.get(i)))).size();
        
        //If the number of times an item's xpath occurs is equal to the expected value, then they are in the right order
        if(ordering == i + 1)
        {
            System.out.println("Products are in order");
        }
        else
        {
            System.out.println("Products are not in order");
            System.exit(0);
        }
    }
    
    
    //Checks if the item selected is displaying in the product section
    public void checkIfProductIsSelectedCorrectly(String productDropDownMenu, String gettingFollowingSibling, int i)
    {
        //this string is an xpath to the product section list
        productDropDownMenu = "//select[@ng-change='updateProduct(discussion)']";
            
            for(int a = i; a > 0; a--)
            {
                /*
                gettingFollowingSibling is an xpath that goes from one part of the list to the one below it.
                this loop will add it a certain amount of times until the right location is reached
                */
                productDropDownMenu += gettingFollowingSibling;
            }
            
            //Checks if the element shown in the drop down menu is the same as the one that was input from the ArrayList productsToAdd
            element = driver.findElement(By.xpath(productDropDownMenu));
            select = new Select(element);
            
            //If the text being displayed in the drop down menu is equal to what is expected, then it is displaying the correct product
            if(select.getFirstSelectedOption().getText().equals(productsToAdd.get(i)))
            {
                System.out.println("The product field is properly set to " + productsToAdd.get(i));
            }
            else
            {
                System.out.println("The product field is incorrectly set.");
                System.exit(0);
            }
    }
    
    
    //Selects the products from the ArrayList
    public void selectProducts()
    {
        //Creating string that will search in another method to see if items are in the correct order
        String productDropDownMenu = new String();
        
        //Every time you go down the list of products sections, the following xpath is added to the String productDropDownMenu
        String gettingFollowingSibling = "//parent::span//parent::div//parent::span//parent::div//parent::div//parent::div//parent::div//parent::td//parent::tr//following-sibling::tr//td//div//div[@class='dataCol']//span//div//span//select";
        
        for(int i = 0; i < productsToAdd.size(); i++)
        {
            System.out.println(String.format("Selecting %s...", productsToAdd.get(i)));
            
            try
            {
                //clicks on the box by the product name
                driver.findElement(By.xpath(String.format("//input[@class='checkbox-indent' and @type='checkbox' and @name='%s']", productsToAdd.get(i)))).click();
            }
            catch(Exception e)
            {
                System.out.println(String.format("Unable to select %s", productsToAdd.get(i)));
                System.exit(0);
            }
            
            System.out.println(String.format("%s has been selected", productsToAdd.get(i)));
            
            //this method checks if the products are selected correctly
            checkIfProductIsSelectedCorrectly(productDropDownMenu, gettingFollowingSibling, i);
            
            //this method checks if the ordering of the products are consistent with how it was input
            checkIfProductSectionOrderIsCorrect(i);

        }

    }
    
    //Checks if the title of the page next to the title icon is "New Mass Add Promo Call"
    public void checkIfOnNewMassAddPromoCallPage()
    {
        System.out.println("Checking if the New Mass Add Promo Call page has been accessed...");
        
        try
        {
            //Waits and checks if the title is 'New Mass Add Promo Call' before determining if it failed
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@class='pageTitleIcon']//following-sibling::h2[text()='New Mass Add Promo Call']")));
        }
        catch(Exception e)
        {
            System.out.println("Failed to access the New Mass Add Promo Call");
            System.exit(0);
        }
        
        System.out.println("New Mass Add Promo Call page has been successfully accessed");
    }
    
    
    //Changes Record Type to Mass Add Promo Call
    public void changeRecordTypeToMassAddPromoCall()
    {
        System.out.println("Changing Record Type to Mass Add Promo Call...");
        
        try
        {
            //waits until the page has been loaded enough to precede
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("RecordTypeId")));
        }
        catch(Exception e)
        {
            System.out.println("System was unable to change Record Type to Mass Add Promo Call");
            System.exit(0);
        }
        
        //selects "Mass Add Promo Call" from the drop down menu
        try
        {
            element = driver.findElement(By.id("RecordTypeId"));
            select = new Select(element);
            select.selectByVisibleText("Mass Add Promo Call");
        }
        catch(Exception e)
        {
            System.out.println("Unable to select 'Mass Add Promo Call' from the drop down menu");
            System.exit(0);
        }
        
        System.out.println("Record Type has been successfully changed to Mass Add Promo Call");
    }
    
    
    //Checks if the title of the page next to the title icon is "New Call Report"
    public void checkIfOnNewCallReportPage()
    {
        System.out.println("Checking if the New Call Report page has been accessed...");
        
        try
        {
            //waits for the page to load before determing if it wasn't able to load
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@class='pageTitleIcon']//following-sibling::h2[text()='New Call Report']")));
        }
        catch(Exception e)
        {
            System.out.println("Failed to access the New Call Report page");
            System.exit(0);
        }
        
        System.out.println("New Call Report page has been successfully accessed");
    }
    
    
    //Clicks on the Record a Call button
    public void clickOnRecordACall()
    {
        System.out.println("Clicking 'Record a Call'...");
        
        try
        {
            //waits for "Record a Call" button to be avaliable before clicking it or determining if it doesn't exist
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Record a Call' and @class='btn' and @type='button']")));
        }
        catch(Exception e)
        {
            System.out.println("Unable to click 'Record a Call");
            System.exit(0);
        }
        
        driver.findElement(By.xpath("//input[@value='Record a Call' and @class='btn' and @type='button']")).click();
        System.out.println("'Record a Call' has been clicked");
    }
    
    
    //Checks if the person's name is beside the "Name" category to ensure that it is the correct page
    public void checkIfInPersonsAccount()
    {
        try
        {
            //checks if the Name section contains "Bob Adams"
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//div[contains(text(), '%s')]//parent::td//preceding-sibling::td[text()='Name']", name))));
        }
        catch(Exception e)
        {
            System.out.println("Failed to load the account's page");
            System.exit(0);
        }
        
        System.out.println("Account page has been successfully loaded");
    }
    
    
    //Clicks on the person's name if found. If not, it will send user an error message
    public void clickOnName()
    {
        try
        {
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText(name)));
        }
        catch(Exception e)
        {
            System.out.println("Failed to find the name");
            System.exit(0);
        }
        
        driver.findElement(By.xpath(String.format("//a[text()='%s']",name))).click();
        System.out.println("Clicked on name...");
    }
    
    /*
    Checks to see if the url contains "salesforce.com/search/SearchResults". 
    If it does, then the page has been successfully accessed.
    */
    public void checkIfNameHasBeenSearched()
    {
        try
        {
            /*
            checks if the curernt URL has "salesforce.com/search/SearchResults" in it. there is a
            wait condition that allows the browser/internet to load before coming to
            a conclusion that something has failed if it does not land on the 
            correct URL
            */
            wait.until(ExpectedConditions.urlContains("salesforce.com/search/SearchResults"));
        }
        catch(Exception e)
        {
            System.out.println("Failed to search for name");
            System.exit(0);
        }
        
        System.out.println("Name has been searched for");
    }
    
    
    //Clicks the Go! button
    void clickTheGoButton()
    {
        System.out.println("Clicking the Go! button...");
        
        try
        {
            //Clicks the Go! button
            driver.findElement(By.xpath("//input[@value=' Go! ' and @class='btn' and @name='search' and @title='Go!' and @type='submit']")).click();
        }
        catch(Exception e)
        {
            System.out.println("Search button has failed");
            System.exit(0);
        }

        System.out.println("The Go! button has been clicked");
    }
    
    
    //Enters the person's name into the search bar
    public void enterName()
    {
        System.out.println("Entering name...");
        
        try
        {
            //checks and waits until the text input field is avaliable to allow for input of string
            wait.until(ExpectedConditions.elementToBeClickable(By.id("sbstr")));
        }
        catch(Exception e)
        {
            System.out.println("Name has failed to be entered");
            System.exit(0);
        }
        
        driver.findElement(By.id("sbstr")).sendKeys(name);
        System.out.println("Name has successfully been entered");
    }
    
    /*
    The search function on the website requires a user's first name to come before the last name. The following if-statement
    will correct the user's name if it is in "(last name), (first name)" format by flipping the string after the comma.
    */
    void makingSureNameIsFirstNameLastName()
    {
        if(name.contains(",")) name = name.substring(name.lastIndexOf(",") + 2) + " " + name.substring(0, name.lastIndexOf(","));
    }
    
    
    //Checks to see if the url contains "salesforce.com/servlet". If it does, then the page has been successfully accessed.
    public void checkIfYouAreInMyAccounts()
    {
        try
        {
            /*
            checks if the curernt URL has "salesforce.com/servlet" in it. there is a
            wait condition that allows the browser/internet to load before coming to
            a conclusion that something has failed if it does not land on the 
            correct URL
            */
            wait.until(ExpectedConditions.urlContains("salesforce.com/servlet"));
        }
        catch(Exception e)
        {
            System.out.println("Failed to reach My Accounts");
            System.exit(0);
        }
        
        System.out.println("My Accounts has been reached");
    }
    
    //Clicks on the My Accounts tab.
    public void clickMyAccounts()
    {
        System.out.println("Clicking the My Accounts tab...");
        
        try
        {
            //The My Accounts tab has a unique id which will be clicked
            driver.findElement(By.id("01rU0000000YP6A_Tab")).click();
        }
        catch(Exception e)
        {
            System.out.println("Failed to click the My Accounts tab");
        }
        
        System.out.println("The My Accounts tab has been successfully clicked");
    }
    
    /*
    Checks if the URL contains "salesforce.com/home" which would tell us the login was
    successful because if it wasn't, it would stay in the login page
    */
    public void checkIfLoggedIn()
    {
        try
        {
            /*
            checks if the curernt URL has "salesforce.com/home" in it. there is a
            wait condition that allows the browser/internet to load before coming to
            a conclusion that something has failed if it does not land on the 
            correct URL
            */
            wait.until(ExpectedConditions.urlContains("salesforce.com/home"));
        }
        catch(Exception e)
        {
            System.out.println("System has failed to log in. Check your username, password, and internet connection");
            System.exit(0);
        }
        
        System.out.println("Login was successful");
    }
    
    //Clicks the Log In button
    public void clickLogIn()
    {
        System.out.println("Logging in...");
        driver.findElement(By.id("Login")).click();
    }
    
    /*
    Enters password into text field for "password". If this fails, an error
    message will be sent to the console
    */
    public void enterPassword()
    {
        System.out.println("Entering password...");
        
        try
        {
            //Sends the String password to the text field
            driver.findElement(By.id("password")).sendKeys(password);
        }
        catch(Exception e)
        {
            System.out.println("Password has failed to be entered");
            System.exit(0);
        }
        
        System.out.println("Password has been entered");
    }
    
    /*
    Enters username into text field for "username". If this fails, an error
    message will be sent to the console
    */
    public void enterUsername()
    {
        System.out.println("Entering UserID...");
        
        try
        {
            //Sends the String userId to the text field
            driver.findElement(By.id("username")).sendKeys(userId);
        }
        catch(Exception e)
        {
            System.out.println("UserID has failed to be entered");
            System.exit(0);
        }
        
        System.out.println("UserID has been entered");
    }
    
    /*
    Checks of the id's of "username", "password", and "login" are clickable. This will allow
    for the username, password, and login button to be accessed.
    */
    public void checkIfWebsiteIsLoaded()
    {
        try
        {
            //checks if buttons are clickable, and therefore accessable
            wait.until(ExpectedConditions.elementToBeClickable(By.id("username")));
            wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
            wait.until(ExpectedConditions.elementToBeClickable(By.id("Login")));
        }
        catch(Exception e)
        {
            System.out.println("FAILED. Unable to access website.");
            System.exit(0);
        }
        
        System.out.println("Website has been successfully accessed");
    }
    
    //Accesses the url
    public void goToWebsite()
    {
        System.out.println("Accessing website. Please wait...");
        driver.get(url);
    }
    
    /*
    Getter and setters methods to initialize private variables and to protect
    variables from unexpected access
    */
    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public void setWait(WebDriverWait wait) {
        this.wait = wait;
    }  

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getProductsToAdd() {
        return productsToAdd;
    }

    public void setProductsToAdd(ArrayList<String> productsToAdd) {
        this.productsToAdd = productsToAdd;
    }

    public ArrayList<Object> getSamplesAndPromotionalItems() {
        return samplesAndPromotionalItems;
    }

    public void setSamplesAndPromotionalItems(ArrayList<Object> samplesAndPromotionalItems) {
        this.samplesAndPromotionalItems = samplesAndPromotionalItems;
    }

    
    
}