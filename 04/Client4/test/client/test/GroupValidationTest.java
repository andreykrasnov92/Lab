package client.test;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import shared.exceptions.EmptyStringException;
import shared.exceptions.StringFormatException;
import static shared.util.Utils.*;

public class GroupValidationTest {

    public GroupValidationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(expected = EmptyStringException.class)
    public void testNullGroupId() throws EmptyStringException {
        String id = null;
        try {
            validateGroupId(id);
        } catch (StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
        fail("EmptyStringException");
    }

    @Test(expected = EmptyStringException.class)
    public void testEmptyGroupId() throws EmptyStringException {
        String id = "";
        try {
            validateGroupId(id);
        } catch (StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
        fail("EmptyStringException");
    }

    @Test(expected = StringFormatException.class)
    public void testNonDigitsInGroupId() throws StringFormatException {
        String id = "1a";
        try {
            validateGroupId(id);
        } catch (EmptyStringException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
        fail("StringFormatException");
    }

    @Test(expected = StringFormatException.class)
    public void testNonPositiveGroupId() throws StringFormatException {
        String id = "1a";
        try {
            validateGroupId(id);
        } catch (EmptyStringException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
        fail("StringFormatException");
    }

    @Test
    public void testOnlyDigitsInGroupId() {
        String id = "67890";
        try {
            validateGroupId(id);
        } catch (EmptyStringException | StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
    }

    @Test(expected = EmptyStringException.class)
    public void testNullGroupName() throws EmptyStringException {
        String name = null;
        try {
            validateGroupName(name);
        } catch (StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
        fail("EmptyStringException");
    }

    @Test(expected = EmptyStringException.class)
    public void testEmptyGroupName() throws EmptyStringException {
        String name = "";
        try {
            validateGroupName(name);
        } catch (StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
        fail("EmptyStringException");
    }

    @Test
    public void testOnlyDigitsInGroupName() {
        String name = "4999";
        try {
            validateGroupName(name);
        } catch (EmptyStringException | StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
    }

    @Test
    public void testOnlyAlphabeticsInGroupName() {
        String name = "magic";
        try {
            validateGroupName(name);
        } catch (EmptyStringException | StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
    }

    @Test
    public void testDigitsAndAlphabeticsAndSpacesInGroupName() {
        String name = "6327 magic";
        try {
            validateGroupName(name);
        } catch (EmptyStringException | StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
    }

    @Test
    public void testDigitsAndAlphabeticsAndSpacesAndHyphensInGroupName() {
        String name = "6327-magic 666";
        try {
            validateGroupName(name);
        } catch (EmptyStringException | StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
    }
}
