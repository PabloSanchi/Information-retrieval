package Filters;

public class FilterAllBraces implements Filter {
    public String filter(String string) {
        string = string.replaceAll("[\\[\\]{}()]", " ");
        return string;
    }
}
