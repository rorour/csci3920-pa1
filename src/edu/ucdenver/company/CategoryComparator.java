package edu.ucdenver.company;

import java.util.Comparator;

public class CategoryComparator implements Comparator<Category> {

    @Override
    //CategoryComparator only compares Category names
    public int compare(Category o1, Category o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
