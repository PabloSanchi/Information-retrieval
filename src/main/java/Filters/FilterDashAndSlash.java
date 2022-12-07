package Filters;

public class FilterDashAndSlash implements Filter {
    public String filter(String string) {
        string = string.replaceAll("\\s-\\s", " ");
        string = string.replaceAll("/", " ");
        return string;
    }
}
