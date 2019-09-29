package utilities;

public class Util {

    public static String printException(Exception ex) {
        return "Some error occurred! " + ex.getLocalizedMessage();
    }
}
