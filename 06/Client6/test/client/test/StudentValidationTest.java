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

public class StudentValidationTest {

    public StudentValidationTest() {
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
    public void testNullStudentId() throws EmptyStringException {
        String id = null;
        try {
            validateStudentId(id);
        } catch (StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
        fail("EmptyStringException");
    }

    @Test(expected = EmptyStringException.class)
    public void testEmptyStudentId() throws EmptyStringException {
        String id = "";
        try {
            validateStudentId(id);
        } catch (StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
        fail("EmptyStringException");
    }

    @Test(expected = StringFormatException.class)
    public void testNonDigitsInStudentId() throws StringFormatException {
        String id = "1a";
        try {
            validateStudentId(id);
        } catch (EmptyStringException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
        fail("StringFormatException");
    }

    @Test
    public void testOnlyDigitsInStudentId() {
        String id = "12345";
        try {
            validateStudentId(id);
        } catch (EmptyStringException | StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
    }

    @Test(expected = EmptyStringException.class)
    public void testNullStudentName() throws EmptyStringException {
        String name = null;
        try {
            validateStudentName(name);
        } catch (StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
        fail("EmptyStringException");
    }

    @Test(expected = EmptyStringException.class)
    public void testEmptyStudentName() throws EmptyStringException {
        String name = "";
        try {
            validateStudentName(name);
        } catch (StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
        fail("EmptyStringException");
    }

    @Test(expected = StringFormatException.class)
    public void testNonAlphabeticsInStudentName() throws StringFormatException {
        String name = "abc123";
        try {
            validateStudentName(name);
        } catch (EmptyStringException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
        fail("StringFormatException");
    }

    @Test
    public void testOnlyAlphabeticsInStudentName() {
        String name = "Ivanov";
        try {
            validateStudentName(name);
        } catch (EmptyStringException | StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
    }

    @Test
    public void testAlphabeticsAndSpacesInStudentName() {
        String name = "Ivanov Peter Sergeevich";
        try {
            validateStudentName(name);
        } catch (EmptyStringException | StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
    }

    @Test
    public void testAlphabeticsAndSpacesAndHyphensInStudentName() {
        String name = "Ivanova-Petrova Anna";
        try {
            validateStudentName(name);
        } catch (EmptyStringException | StringFormatException ex) {
            System.out.println("Unexpected " + ex.getClass().getSimpleName() + "!");
        }
    }
}
