package ca.jrvs.apps.practice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc {

    @Override
    public boolean matchJpeg(String filename) {
        //any character till end in jpg or jpeg
        String regex = "^.*\\.(jpg|jpeg)$";
        // ignore case
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(filename);
        // return True if found
        return matcher.find();
    }

    @Override
    public boolean matchIp(String ip) {
        // any 3 digits ranging from 000-999 4 times separated by dots
        String regex = "\\d{3}\\.\\d{3}\\.\\d{3}\\.\\d{3}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ip);
        return matcher.find();
    }

    @Override
    public boolean isEmptyLine(String line) {
        String regex = "^\\s*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }
}
