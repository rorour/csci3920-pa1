package edu.ucdenver.company;

import java.util.Comparator;

public class ProductComparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        //compare by name (this will sort alphabetically for most catalog purposes)
        int namesEqual = o1.getName().compareTo(o2.getName());
        if (namesEqual != 0){
            return namesEqual;
        } else {
            //compare by id (this will confirm match when checking if products are equal)
            return o1.getId().compareTo(o2.getId());
        }
    }
}
