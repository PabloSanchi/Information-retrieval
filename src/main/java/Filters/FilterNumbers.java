package Filters;

public class FilterNumbers implements Filter {
    public String filter(String string) {
        string = string.replaceAll("\\b\\d+\\b", " ");
        return string;
    }
}
