package Filters;

public class FilterAllDotsAndCommas implements Filter {
    public String filter(String string) {
        string = string.replaceAll("[.:,;]", " ");
        return string;
    }
}
