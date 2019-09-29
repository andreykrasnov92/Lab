package server.util;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import shared.exceptions.DataUpdatingException;

public class DBUtils {

    public static final String GROUP_TABLE_NAME = "Gruppa", STUDENT_TABLE_NAME = "Student";
    public static final Set<Integer> LOCKED_GROUPS = new HashSet<>(), LOCKED_STUDENTS = new HashSet<>();

    static {
        Locale.setDefault(Locale.ENGLISH);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+4"));
    }

    public static void lockGroup(int groupId) throws DataUpdatingException {
        if (LOCKED_GROUPS.contains(groupId)) {
            throw new DataUpdatingException("This group is updated now by another client!");
        } else {
            LOCKED_GROUPS.add(groupId);
        }
    }

    public static void unlockGroup(int groupId) throws DataUpdatingException {
        if (LOCKED_GROUPS.contains(groupId)) {
            LOCKED_GROUPS.remove(groupId);
        } else {
            throw new DataUpdatingException("You cannot update group until you've locked it.");
        }
    }

    public static void lockStudent(int studentId) throws DataUpdatingException {
        if (LOCKED_STUDENTS.contains(studentId)) {
            throw new DataUpdatingException("This student is updated now by another client!");
        } else {
            LOCKED_STUDENTS.add(studentId);
        }
    }

    public static void unlockStudent(int studentId) throws DataUpdatingException {
        if (LOCKED_STUDENTS.contains(studentId)) {
            LOCKED_STUDENTS.remove(studentId);
        } else {
            throw new DataUpdatingException("You cannot update student until you've locked it.");
        }
    }
}
