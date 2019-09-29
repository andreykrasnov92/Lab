package controller;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class DBController {

    public static final String GROUP_TABLE_NAME = "Gruppa", STUDENT_TABLE_NAME = "Student";

    public static final Set<Integer> lockedGroups = new HashSet<>(),
            lockedStudents = new HashSet<>();

    static {
        Locale.setDefault(Locale.ENGLISH);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+4"));
    }
}
