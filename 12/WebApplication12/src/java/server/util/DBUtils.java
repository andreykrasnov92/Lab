package server.util;

import java.util.Locale;
import java.util.TimeZone;

public class DBUtils {

    public static final String GROUP_TABLE_NAME = "Gruppa", STUDENT_TABLE_NAME = "Student";

    static {
        Locale.setDefault(Locale.ENGLISH);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+4"));
    }
}
