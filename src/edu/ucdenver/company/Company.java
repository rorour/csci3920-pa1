package edu.ucdenver.company;

import java.time.LocalDate;
import java.util.ArrayList;


/**TODO: Add implementation to Company methods
 * Note: Only admins have access to product management
 */
public class Company {
    private ArrayList<User> users;
    private ArrayList<Product> catalog;
    private ArrayList<Category> categories;
    private ArrayList<Order> orders;

    private String name;

    public Company(String name) {
        this.name = name;
        users = new ArrayList<>();
        catalog = new ArrayList<>();
        categories = new ArrayList<>();
    }

    //============================================================================
    // 1 & 2. Create and Login Users
    public void addUser(String name, String email, String password){}
    public User findUser(String name, String email){return null;} //for checking if user doesn't already exist in list
    public void loginUser(String email, String password){} //should we make this a boolean?


    //============================================================================
    // 3. Product Management
    public void addProduct(){}
    public void removeProduct(){}
    public void addCategoryFromProduct(Product product){}
    public void removeCategoryFromProduct(Product product){}

    //============================================================================
    // 4. Product Category Management
    public void addCategory(Category category){}
    public Category findCategory(String c){return null;} //for checking to see if it already exists in list
    public void removeCategory(Category category){
        //note: needs to search all products that have this category and set it to default category for each product
    }

    //============================================================================
    // 5. Browsing/Searching
    public Product searchProduct(String str){return null;} //searches both product name and description
    public String browseCategory(Category category){return "";} //lists all products within a category

    //============================================================================
    // 6. Get Product details
    public String showProductDetails(Product p){return "";} //shows all product information: use of toString will make this easy

    //============================================================================
    // 7. Order Management
    // Note: parameters should be changed once we figure out how classes communicate with each other

    public void createEmptyOrder(Customer customer){
        String orderNumber = "TempOrder#";
        customer.createOpenOrder(orderNumber); //user can only have one open order at a time
        //need to throw an exception if openOrder already exists
    }
    
    //add product to order. will add product once, if customer requires the product more than once, will add multiple times
    public void addProductToOrder(Customer customer, Product product){}
    //remove product from order. Will remove all instances of particular product from order
    public String listOrders(Customer customer){return "";} //list orders w/out repetition
    public void finalizeOrder(Customer customer){}//once order is finalized, customer cannot add/remove products from it
    public void cancelOrder(Customer customer){} //if order is open, just delete it from the system.

    //============================================================================
    // 8. Order Report
    public String listFinalizedOrders(Customer customer){return "";}
    public String listOrdersByDate(LocalDate date){return "";}

    //============================================================================
    // 9. is for apps, but I'm putting it here as a reminder:
    // Terminate server and save. Admin option



    public ArrayList<User> getUsers(){
        return this.users;
    }
    public ArrayList<Product> getCatalog(){
        return this.catalog;
    }
    public ArrayList<Category> getCategory(){
        return this.categories;
    }

    public String getName(){
        return this.name;
    }




}
