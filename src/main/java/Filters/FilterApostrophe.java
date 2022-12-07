package Filters;

public class FilterApostrophe implements Filter {
    public String filter(String string) {
        string = string.replaceAll("['\"]", " ");
        return string;
    }  
}
