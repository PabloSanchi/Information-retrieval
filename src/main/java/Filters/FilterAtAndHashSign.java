package Filters;

public class FilterAtAndHashSign implements Filter {
    public String filter(String string) {
        string = string.replaceAll("[@#$%+]", " ");
        return string;
    }
}
