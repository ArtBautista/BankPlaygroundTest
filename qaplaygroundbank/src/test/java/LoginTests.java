import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTests;

public class LoginTests extends BaseTests {
    
    //TC-002
    @Test
    public void testLogin(){
        loginPage.logIntoApplication("standard_user", "bank_sauce");
    }

    //TC003
    @Test 
    public void testInavalidPassword() {
        loginPage.logIntoApplication("standard_user", "wrong_password");
        String actualMessage = loginPage.getErrorMessage();
        System.err.println(actualMessage);
        Assert.assertTrue(actualMessage.contains("The username or password you entered is incorrect."));
    }

    //TC004
   @Test 
    public void testInavildUsername() {
        loginPage.logIntoApplication("wrong_user", "bank_sauce");
        loginPage.clickLoginButton();
        String actualMessage = loginPage.getErrorMessage();
        System.err.println(actualMessage);
        Assert.assertTrue(actualMessage.contains("The username or password you entered is incorrect."));
    }

    //TC004
   @Test 
    public void testEmptyFields() {
        loginPage.clickLoginButton();
        String actualMessage = loginPage.getErrorMessage();
        System.err.println(actualMessage);
        Assert.assertTrue(actualMessage.contains("Please enter your username."));
    }

}