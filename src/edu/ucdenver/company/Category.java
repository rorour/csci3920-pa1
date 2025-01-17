/** PA1
 * Raven O'Rourke & Lora Kalthoff
 *
 * Category class:
 */
package edu.ucdenver.company;

import java.io.Serializable;

public class Category implements Serializable {
    private String name;
    private String id;
    private String description;

    public Category(String name, String id, String description){
        this.name = name;
        this.id = id;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param c2 takes second category to compare to calling category.
     * @returns comparison of category names for alphabetizing.
     */
    public int compareTo(Category c2){
        CategoryComparator cc = new CategoryComparator();
        return cc.compare(this, c2);
    }

    /**
     * @param c2 takes second category to compare to calling category.
     * @returns true if categories have same name; false otherwise
     */
    public boolean equals(Category c2){
        if (this.compareTo(c2) == 0){
            return true;
        } else
            return false;
    }

    @Override
    public String toString(){
        return name;
    }
}
