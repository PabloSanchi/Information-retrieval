package Filters;

public class FilterAsterisk implements Filter {
    public String filter(String string) {
        string = string.replaceAll("[*]", " ");
        return string;
    }
}
