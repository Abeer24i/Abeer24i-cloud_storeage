package com.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.time.Instant;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private Instant wait;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-button")));
        WebElement buttonSignUp = driver.findElement(By.id("submit-button"));
        buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up! Please continue to the"));
        WebElement buttonLogIN = driver.findElement(By.id("login-link"));
        buttonLogIN.click();
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-button")));
        WebElement loginButton = driver.findElement(By.id("submit-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }


    @Test
    public void testUnauthorizedUserAccess() {
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
    }

    @Test
    public void testUserSignupLoginAndLogout() {
        doMockSignUp("FirstName", "LastName", "testUser", "password123");
        doLogIn("testUser", "password123");

        Assertions.assertEquals("Home", driver.getTitle());

        WebElement logoutButton = driver.findElement(By.id("logoutDiv")).findElement(By.tagName("button"));
        logoutButton.click();

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }


// Note Creation, Viewing, Editing, and Deletion Tests

    public void doNoteCreation(String title, String description) throws InterruptedException {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 15);

        WebElement navNotesTab = driver.findElement(By.id("nav-notes-tab"));
        navNotesTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNoteButton")));

        WebElement addNoteButton = driver.findElement(By.id("addNoteButton"));
        addNoteButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));

        Thread.sleep(1000);

        WebElement noteTitle = driver.findElement(By.id("note-title"));
        WebElement noteDescription = driver.findElement(By.id("note-description"));

        noteTitle.sendKeys(title);
        noteDescription.sendKeys(description);

        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-primary")));

        WebElement saveChangesButton = driver.findElement(By.cssSelector("button.btn.btn-primary"));
        saveChangesButton.click();

        WebElement noteTitleElement = driver.findElement(By.id("result"));
        Assertions.assertNotNull(noteTitleElement);
    }

    @Test
    public void testNoteCreation() throws InterruptedException {
        doMockSignUp("Note", "Creation", "noteCreator", "password123");
        doLogIn("noteCreator", "password123");

        doNoteCreation("Test Note", "This is a test note");
    }

    @Test
    public void testNoteEditing() throws InterruptedException {
        doMockSignUp("Note", "Editing", "noteEditor", "password123");
        doLogIn("noteEditor", "password123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 15);

        doNoteCreation("Test Note", "This is a test note");

        WebElement homeButton = driver.findElement(By.id("homeButton"));
        homeButton.click();

        WebElement navNotesTab = driver.findElement(By.id("nav-notes-tab"));
        navNotesTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editButton")));

        WebElement editButton = driver.findElement(By.id("editButton"));
        editButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));

        WebElement noteTitle = driver.findElement(By.id("note-title"));
        WebElement noteDescription = driver.findElement(By.id("note-description"));

        noteTitle.sendKeys("Edited Note Title");
        noteDescription.sendKeys("This is an edited test note");


        WebElement saveChangesButton = driver.findElement(By.cssSelector("button.btn.btn-primary"));
        saveChangesButton.click();

        WebElement editedNoteTitleElement = driver.findElement(By.id("result"));
        Assertions.assertNotNull(editedNoteTitleElement);
    }

    @Test
    public void testNoteDeletion() throws InterruptedException {
        doMockSignUp("Note", "Deletion", "noteDeleter", "password123");
        doLogIn("noteDeleter", "password123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

        doNoteCreation("Test Note", "This is a test note");

        WebElement homeButton = driver.findElement(By.id("homeButton"));
        homeButton.click();


        WebElement navNotesTab = driver.findElement(By.id("nav-notes-tab"));
        navNotesTab.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteButton")));

        WebElement deleteButton = driver.findElement(By.id("deleteButton"));
        deleteButton.click();

        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//th[text()='Delete Note']")));
        List<WebElement> noteTitleElements = driver.findElements(By.xpath("//th[text()='Delete Note']"));
        Assertions.assertTrue(noteTitleElements.isEmpty());
    }


// Credential Creation, Viewing, Editing, and Deletion Tests

    public void doCredentialCreation(String url, String userName, String password) throws InterruptedException {

        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

        WebElement addCredentialButton = driver.findElement(By.id("nav-credentials-tab"));
        addCredentialButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("New_Credential")));

        WebElement addNewCredentialButton = driver.findElement(By.id("New_Credential"));
        addNewCredentialButton.click();

        Thread.sleep(1000);

        WebElement credentialUrl = driver.findElement(By.id("credential-url"));
        WebElement credentialUsername = driver.findElement(By.id("credential-username"));
        WebElement credentialPassword = driver.findElement(By.id("credential-password"));

        credentialUrl.sendKeys(url);
        credentialUsername.sendKeys(userName);
        credentialPassword.sendKeys(password);

        WebElement saveChangesButton = driver.findElement(By.id("saveChangeCredential"));
        saveChangesButton.click();

        WebElement credentialUrlElement = driver.findElement(By.id("result"));
        Assertions.assertNotNull(credentialUrlElement);

    }


    @Test
    public void testCredentialCreation() throws InterruptedException {
        doMockSignUp("Credential", "Creation", "credentialCreator", "password123");
        doLogIn("credentialCreator", "password123");

        doCredentialCreation("http://example.com", "user123", "password123");

    }

    @Test
    public void testCredentialEditing() throws InterruptedException {
        doMockSignUp("Credential", "Editing", "credentialEditor", "password123");
        doLogIn("credentialEditor", "password123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

        doCredentialCreation("http://example.com", "user123", "password123");

        WebElement homeButton = driver.findElement(By.id("homeButton"));
        homeButton.click();

        WebElement addCredentialButton2 = driver.findElement(By.id("nav-credentials-tab"));
        addCredentialButton2.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editCredential")));

        WebElement editButton = driver.findElement(By.id("editCredential"));
        editButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));

        WebElement credentialUrl2 = driver.findElement(By.id("credential-url"));
        WebElement credentialUsername2 = driver.findElement(By.id("credential-username"));
        WebElement credentialPassword2 = driver.findElement(By.id("credential-password"));

        credentialUrl2.clear();
        credentialUrl2.sendKeys("http://newexample.com");

        credentialUsername2.clear();
        credentialUsername2.sendKeys("user321");

        credentialPassword2.clear();
        credentialPassword2.sendKeys("password321");

        WebElement saveChangesButton2 = driver.findElement(By.id("saveChangeCredential"));
        saveChangesButton2.click();

        WebElement editedCredentialUrlElement = driver.findElement(By.id("result"));
        Assertions.assertNotNull(editedCredentialUrlElement);
    }

    @Test
    public void testCredentialDeletion() throws InterruptedException {
        doMockSignUp("Credential", "Deletion", "credentialDeleter", "password123");
        doLogIn("credentialDeleter", "password123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);

        doCredentialCreation("http://example.com", "user123", "password123");

        WebElement homeButton = driver.findElement(By.id("homeButton"));
        homeButton.click();

        WebElement addCredentialButton2 = driver.findElement(By.id("nav-credentials-tab"));
        addCredentialButton2.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteCredential")));

        WebElement deleteButton = driver.findElement(By.id("deleteCredential"));
        deleteButton.click();

        List<WebElement> credentialUrlElements = driver.findElements(By.xpath("credentialTable"));
        Assertions.assertTrue(credentialUrlElements.isEmpty());
    }


}
