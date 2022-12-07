package Filters;

public class FilterAllMarks implements Filter {
    public String filter(String string) {
        string = string.replaceAll("[?!]", " ");
        return string;
    }
}