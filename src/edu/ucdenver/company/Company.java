package edu.ucdenver.company;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;



/**TODO: Add implementation to Company methods
 * Note: Only admins have access to product management
 */
public class Company implements Serializable {
    private ArrayList<User> users;
    private ArrayList<Product> catalog;
    private ArrayList<Category> categories;
    private ArrayList<Order> orders;
    private Category defaultCategory;
    private static int orderNumbers; //increments each time an order is made by various customers

    private String name;

    public Company(String name) {
        this.name = name;
        users = new ArrayList<>();
        catalog = new ArrayList<>();
        categories = new ArrayList<>();
        orders = new ArrayList<>();
        defaultCategory = new Category("Default", "001", "Default category.");
        categories.add(defaultCategory);
        orderNumbers = 0;

    }

    //============================================================================
    // 1 & 2. Create and Login Users
    public void addCustomer(String name, String email, String password) throws IllegalArgumentException {
        User tempUser = findUser(name, email);
        if(tempUser == null){
            users.add(new Customer(name, email, password));
        }
        else{
            throw new IllegalArgumentException("Username and/or email is already taken");
        }

    }

    public void addAdmin(String name, String email, String password) throws IllegalArgumentException{
        User tempUser = findUser(name, email);
        if(tempUser == null){
            users.add(new Administrator(name, email, password));
        }
        else{
            throw new IllegalArgumentException("Username and/or email is already taken");
        }
    }

    //Checks to see if user already exists in database. return null if empty
    public User findUser(String name, String email){
        if(!users.isEmpty()){
            for(User u : users){
                if(u.getDisplayName().equals(name) || u.getEmail().equalsIgnoreCase(email)){
                    return u;
                }
            }
        }
        return null;
    }
    //returns user if logged in properly.
    public User loginUser(String email, String password) throws IllegalArgumentException{
        for(User user : this.users){
            if(user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)){
                return user;
            }
        }
        throw new IllegalArgumentException("Log in unsuccessful. Either email or password is incorrect");

    }


    //============================================================================
    // 3. Product Management
    //add new product to category
    public void addProduct(Product p){
        if (p.getCategories().size() == 0){
            p.addCategory(this.defaultCategory);
        }
        this.catalog.add(p);
    }
    public void removeProduct(Product p){
        catalog.removeIf(product -> product.equals(p));
    }
    //todo i don't think we need these since product has its add/removecategory functions
    public void addCategoryFromProduct(Product product){ }
    public void removeCategoryFromProduct(Product product){}

    //============================================================================
    // 4. Product Category Management
    public void addCategory(Category category){this.categories.add(category);}
    public Category findCategory(String c){return null;} //for checking to see if it already exists in list
    public void removeCategory(Category categoryToRemove){
        for (Category c : this.categories){
            if (c.equals(categoryToRemove)){
                //found desired category
                //for products in categoryToRemove in catalog, remove cTR from product's category array
                for (Product p : browseCategory(categoryToRemove)){
                    p.removeCategory(categoryToRemove);
                    //if product no longer has a category, assign default
                    if (p.getCategories().size() == 0){
                        p.addCategory(defaultCategory);
                    }
                }
                //remove cTR from company's categories
                categories.remove(c);
                //break because the category was already found
                break;
            }
        }
        //note: needs to search all products that have this category and set it to default category for each product
    }

    //============================================================================
    // 5. Browsing/Searching
    //searches both product name and description
    public ArrayList<Product> searchProducts(String str){
        ArrayList<Product> matchingProducts = new ArrayList<>();
        //todo make this search method more precise so it doesn't need a full string
        for (Product p : catalog){
            if (p.getName().contains(str) || p.getDescription().contains(str)){
                matchingProducts.add(p);
            }
        }
        return matchingProducts;
    }

    //returns all products in category
    public ArrayList<Product> browseCategory(Category category){
        ArrayList<Product> productsInCategory = new ArrayList<>();
        for (Product p : catalog){
            for (Category c : p.getCategories()){
                if(c.equals(category)){
                    productsInCategory.add(p);
                    break;
                }
            }
        }
        return productsInCategory;}

    //============================================================================
    // 6. Get Product details
    public String showProductDetails(Product p){return "";} //shows all product information: use of toString will make this easy

    //============================================================================
    // 7. Order Management
    public void createEmptyOrder(Customer customer) throws IllegalArgumentException {
        int orderNumber = createNewOrderNum();
        if(!customer.openOrderExists()){
            customer.createOpenOrder(orderNumber);
        } else {
            throw new IllegalArgumentException("Open order already exists");
        }
    }
    //add product to order. will add product once, if customer requires the product more than once, will add multiple times
    public void addProductToOrder(Customer customer, Product product) throws IllegalArgumentException{
        if(customer.openOrderExists()){
            customer.getOpenOrder().addProduct(product);
        }
        else {
            throw new IllegalArgumentException("Open order does not exist");
        }
    }

    //remove product from order. Will remove all instances of particular product from order
    public void removeProductFromOrder(Customer customer, Product product) throws IllegalArgumentException{
        if(customer.openOrderExists()) {
            customer.getOpenOrder().removeProduct(product);
        }
        else {
            throw new IllegalArgumentException("Open order does not exist");
        }
    }

    public String listOrderProducts(Customer customer){
        ArrayList<Product> tempProducts = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if(customer.openOrderExists()){
            //makes new tempProduct list without duplicates
            for(Product p : customer.getOpenOrder().getProducts()){
                if(!tempProducts.contains(p)){
                    tempProducts.add(p);
                }
            }
            //Now makes the string of products
            for(Product p : tempProducts){
                sb.append(p);
            }

        }
        return sb.toString();
    }

    public void finalizeOrder(Customer customer) throws IllegalArgumentException{
        if(customer.openOrderExists()){
            Order tempOrder = customer.getOpenOrder();
            tempOrder.finalizeOrder();
            orders.add(tempOrder);
            customer.finalizeOpenOrder();
        }
        else{
            throw new IllegalArgumentException("Open Order does not exist");
        }
    }//once order is finalized, customer cannot add/remove products from it

    public void cancelOrder(Customer customer){
        if(customer.openOrderExists()){
            customer.cancelOrder();
        }
    } //if order is open, just delete it from the system.

    private static int createNewOrderNum(){
        return orderNumbers++;
    }


    //============================================================================
    // 8. Order Report
    public String listOrderReport(Customer customer) throws IllegalArgumentException{
       if(!customer.getFinalizedOrders().isEmpty()){
           StringBuilder sb = new StringBuilder();
           for(Order orders : customer.getFinalizedOrders()){
               sb.append(orders + "\n");
           }
           return sb.toString();
       }
       else {
           throw new IllegalArgumentException("There are no finalized orders");
       }
    }
    //TODO: Right now it prints after and before specified dates (basically > and <, but not >= and <= )
    public String listOrdersByDate(LocalDate startDate, LocalDate endDate) throws IllegalArgumentException{
        //ArrayList<Order> rangeOrderList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if(!orders.isEmpty()){
            for(Order o : orders){
                if(o.getFinalizedDate().isAfter(startDate) && o.getFinalizedDate().isBefore(endDate)){
                    sb.append(o + "\n");
                }
            }
            return sb.toString();
        } else {
            throw new IllegalArgumentException("There are no finalized orders between " + startDate + " and " + endDate);
        }

    }
    //TODO: delete this when finalizing project. Was for testing purposes only.
    public void testFinalizedOrders(){
        Order temp = null;
        for(int i = 1; i < 13; i++){
            temp = new Order(createNewOrderNum());
            temp.finalizeOrder();
            temp.setFinalizedDate(LocalDate.of(2020,i,1));
            this.orders.add(temp);
        }

    }

    //============================================================================
    // 9. is for apps, but I'm putting it here as a reminder:
    // Terminate server and save. Admin option



    public ArrayList<User> getUsers(){
        return this.users;
    }
    public ArrayList<Order> getOrders(){
        return this.orders;
    }
    public ArrayList<Product> getCatalog(){
        return this.catalog;
    }
    public ArrayList<Category> getCategories(){
        return this.categories;
    }

    public String getName(){
        return this.name;
    }




}
