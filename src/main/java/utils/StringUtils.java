package utils;

public class StringUtils {

    public static final String HTTP_MESSAGE_DELIMITER = "\n";

    public static String getPath(String message){
        return message.split(" ")[1];
    }

    public static String getVerb(String message){
        return message.split(" ")[0];
    }
}
