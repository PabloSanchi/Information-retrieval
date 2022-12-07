package Filters;

public class FilterSpaces implements Filter {
    public String filter(String string) {
        string = string.replaceAll("\\s\\s+", " ");
        return string;
    }
}
