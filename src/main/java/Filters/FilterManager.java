package Filters;
import java.util.ArrayList;

public class FilterManager {
    private ArrayList<Filter> filters = new ArrayList<Filter>();

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public String execute(String str){
        String result = str;
        for (Filter filter : filters){
            result = filter.filter(result);
        }
        return result;
    }
}