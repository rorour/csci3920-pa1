/** PA1
 * Raven O'Rourke & Lora Kalthoff
 *
 * Product Abstract class:
 * Parent class for Book, Home, Electronic, Phone and Computer
 */

package edu.ucdenver.company;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
//TODO: Fix the abstract: As of now, cannot access methods in derived classes
public abstract class Product implements Serializable {
    private String name;
    private String id;
    private String brand;
    private String description;
    private LocalDate incorporatedDate;
    private ArrayList<Category> categories;
    //todo figure out how to add: private Image image;

    public Product(String name, String id, String brand, String description,
                   LocalDate incorporatedDate){
        this.name = name;
        this.id = id;
        this.brand = brand;
        this.description = description;
        this.incorporatedDate = incorporatedDate;
        this.categories = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getBrand(){
        return this.brand;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public LocalDate getIncorporatedDate(){
        return this.incorporatedDate;
    }
    public void setIncorporatedDate(LocalDate date){
        this.incorporatedDate = date;
    }
    public void addCategory(Category c){
        this.categories.add(c);

    }


    public abstract String productType();




    /** Removes category if equaled to parameter
     * @param c Category type
     */
    public void removeCategory(Category c){
        this.categories.removeIf(category -> category.equals(c));
    }
    public ArrayList<Category> getCategories(){
        return this.categories;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Product: ");
        sb.append(this.name + " ");
        sb.append(this.id + " ");
        sb.append(this.brand + " ");
        sb.append(this.getIncorporatedDate());
        sb.append("\n\t" + this.getDescription() + "\n");
        return sb.toString();
    }
    //@Override
    public int compareTo(Product p2){
        ProductComparator pc = new ProductComparator();
        return pc.compare(this, p2);
    }

    //@Override
    //for now, equals determines only whether product name & product id are the same
    public boolean equals(Product p2){
        if (this.compareTo(p2) == 0){
            return true;
        } else
            return false;
    }

}
