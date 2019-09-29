package shared.util;

import shared.exceptions.EmptyStringException;
import shared.exceptions.StringFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern GROUP_NAME_PATTERN
            = Pattern.compile("[A-Za-z0-9- ]*");
    private static final Pattern STUDENT_NAME_PATTERN
            = Pattern.compile("[A-Z][a-z-]*[[ ][A-Z][a-z-]*]*");

    public static int validateGroupId(String groupId) throws EmptyStringException, StringFormatException {
        if (groupId == null) {
            throw new EmptyStringException("Empty group id!");
        }
        String id = groupId.replace(" ", "");
        if (id.isEmpty()) {
            throw new EmptyStringException("Empty group id!");
        }
        try {
            int idValue = Integer.parseInt(groupId);
            if (idValue <= 0) {
                throw new StringFormatException("Non-positive group id: " + groupId + "!");
            }
            return idValue;
        } catch (NumberFormatException ex) {
            throw new StringFormatException("Incorrect format of group id: " + groupId + "!");
        }
    }

    public static String validateGroupName(String groupName) throws EmptyStringException, StringFormatException {
        if (groupName == null) {
            throw new EmptyStringException("Empty group name!");
        }
        String name = groupName.replace("-", "").replace(" ", "");
        if (name.isEmpty()) {
            throw new EmptyStringException("Empty group name!");
        }
        Matcher matcher = GROUP_NAME_PATTERN.matcher(name);
        if (matcher.matches()) {
            return groupName;
        } else {
            throw new StringFormatException("Incorrect format of group name: " + groupName + "!");
        }
    }

    public static int validateStudentId(String studentId) throws EmptyStringException, StringFormatException {
        if (studentId == null) {
            throw new EmptyStringException("Empty student id!");
        }
        String id = studentId.replace(" ", "");
        if (id.isEmpty()) {
            throw new EmptyStringException("Empty student id!");
        }
        try {
            int idValue = Integer.parseInt(studentId);
            if (idValue <= 0) {
                throw new StringFormatException("Non-positive student id: " + studentId + "!");
            }
            return idValue;
        } catch (NumberFormatException ex) {
            throw new StringFormatException("Incorrect format of student id: " + studentId + "!");
        }
    }

    public static String validateStudentName(String studentName) throws EmptyStringException, StringFormatException {
        if (studentName == null) {
            throw new EmptyStringException("Empty student name!");
        }
        String name = studentName.replace("-", "").replace(" ", "");
        if (name.isEmpty()) {
            throw new EmptyStringException("Empty student name!");
        }
        Matcher matcher = STUDENT_NAME_PATTERN.matcher(name);
        if (matcher.matches()) {
            return studentName;
        } else {
            throw new StringFormatException("Incorrect format of student name: " + studentName + "!");
        }
    }

    public static String printException(Exception ex) {
        ex.printStackTrace();
        return "Some error occurred! " + ex.getLocalizedMessage();
    }
}
