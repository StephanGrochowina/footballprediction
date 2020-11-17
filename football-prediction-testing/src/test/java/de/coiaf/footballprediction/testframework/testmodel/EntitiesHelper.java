package de.coiaf.footballprediction.testframework.testmodel;

import org.junit.Assert;

import java.util.List;

import static org.junit.Assert.*;

public class EntitiesHelper {

    public static final String USER_FIRST_NAME = "Jeffrey";
    public static final String USER_LAST_NAME = "Lebowsky";
    public static final String USER_USERNAME = "ElDuderino";
    public static final String PHONE_HOME_NUMBER = "481 516-2342";
    public static final String PHONE_MOBILE_NUMBER = "108 555-6666";

    private EntitiesHelper() {
        throw new UnsupportedOperationException("this class is a helper");
    }

    public static void assertUser(User user) {
        assertNotNull(user);
        Assert.assertEquals(USER_FIRST_NAME, user.getFirstName());
        Assert.assertEquals(USER_LAST_NAME, user.getLastName());
        Assert.assertEquals(USER_USERNAME, user.getUsername());
    }

    public static void assertUserWithTelephone(User user) {
        assertUserWithTelephones(user, 1);
    }

    public static void assertUserWithTelephones(User user) {
        assertUserWithTelephones(user, 2);
    }

    public static void assertUserWithTelephones(User user, int size) {
        assertUser(user);
        List<Telephone> telephones = user.getTelephones();
        assertEquals( size, telephones.size() );
        Telephone homePhone = telephones.get(0);
        Assert.assertEquals( Telephone.Type.HOME, homePhone.getType() );
        Assert.assertEquals( PHONE_HOME_NUMBER, homePhone.getNumber() );
        if ( size == 2 ) {
            Telephone mobilePhone = telephones.get(1);
            Assert.assertEquals( Telephone.Type.MOBILE, mobilePhone.getType() );
            Assert.assertEquals( PHONE_MOBILE_NUMBER, mobilePhone.getNumber() );
        }
    }

    public static void assertUser(UserDto user) {
        assertNotNull(user);
        Assert.assertEquals(USER_FIRST_NAME, user.getFirstName());
        Assert.assertEquals(USER_LAST_NAME, user.getLastName());
        Assert.assertEquals(USER_USERNAME, user.getUsername());
        List<String> telephones = user.getTelephones();
        assertEquals( 2, telephones.size() );
        String homePhone = telephones.get(0);
        String mobilePhone = telephones.get(1);
        assertTrue( "home phone does not contain type", homePhone.contains(Telephone.Type.HOME.toString()) );
        assertTrue( "home phone does not contain number", homePhone.contains(PHONE_HOME_NUMBER) );
        assertTrue( "mobile phone does not contain type", mobilePhone.contains(Telephone.Type.MOBILE.toString()) );
        assertTrue( "mobile phone does not contain number", mobilePhone.contains(PHONE_MOBILE_NUMBER) );
    }

    public static User newUser() {
        User user = new User();
        user.setFirstName(USER_FIRST_NAME);
        user.setLastName(USER_LAST_NAME);
        user.setUsername(USER_USERNAME);
        return user;
    }

    public static User newUserWithTelephone() {
        return newUserWithTelephones(1);
    }

    public static User newUserWithTelephones() {
        return newUserWithTelephones(2);
    }

    public static User newUserWithTelephones(int size) {
        User user = newUser();
        Telephone homeNumber = new Telephone();
        homeNumber.setType(Telephone.Type.HOME);
        homeNumber.setNumber(PHONE_HOME_NUMBER);
        List<Telephone> telephones = user.getTelephones();
        telephones.add(homeNumber);
        if ( size == 2 ) {
            Telephone mobileNumber = new Telephone();
            mobileNumber.setType(Telephone.Type.MOBILE);
            mobileNumber.setNumber(PHONE_MOBILE_NUMBER);
            telephones.add(mobileNumber);
        }
        return user;
    }
}
