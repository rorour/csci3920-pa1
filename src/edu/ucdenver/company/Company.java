/** PA1
 * Raven O'Rourke & Lora Kalthoff
 *
 * Company Class:
 * Handles logging in users, product and category management, browsing/searching, customer orders, order report
 */
package edu.ucdenver.company;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;


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
    //              Create and Login Users
    //============================================================================
    /** Adds User to the users Arraylist
     * @param newUser takes user to add
     * @throws IllegalArgumentException if username or email already exists in the users Arraylist
     */
    synchronized public void addUser(User newUser) throws IllegalArgumentException {
        User tempUser = findUser(newUser.getDisplayName(), newUser.getEmail());
        if(tempUser == null){
            users.add(newUser);
        }
        else{
            throw new IllegalArgumentException("Username and/or email is already taken");
        }
    }

    /**Searches the users Arraylist for users where their name and/or email matches
     * @param name String : name of the User
     * @param email String : email of the User
     * @return User that matches name and email. returns null if there is no match
     */
    synchronized public User findUser(String name, String email){
        if(!users.isEmpty()){
            for(User u : users){
                if(u.getDisplayName().equals(name) || u.getEmail().equalsIgnoreCase(email)){
                    return u;
                }
            }
        }
        return null;
    }

    /** Logins the user into the database
     * @param email String : User's email address
     * @param password String : User's password
     * @return User with correct email and password
     * @throws IllegalArgumentException if email or password is not correct
     */
    synchronized public User loginUser(String email, String password) throws IllegalArgumentException{
        for(User user : this.users){
            if(user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)){
                return user;
            }
        }
        throw new IllegalArgumentException("Log in unsuccessful. Either email or password is incorrect");

    }


    //============================================================================
    //              Product Management
    //============================================================================

    /** Add Product to the catalog arraylist.
     * if there are no items in category, product will be assigned a default category
     * @param p Product
     */
    public synchronized void addProduct(Product p){
        if (p.getCategories().size() == 0){
            p.addCategory(this.defaultCategory);
        }
        this.catalog.add(p);
    }

    /** Remove product from catalog
     * @param p Product
     */
    public synchronized void removeProduct(Product p){
        catalog.removeIf(product -> product.equals(p));
    }

    /** Adds a Category to the Product, removes default category if another has been added
     * @param product Product type
     * @param category Category type
     */
    synchronized public void addCategoryToProduct(Product product, Category category){
        for (Product p : catalog){
            if (product.equals(p)){
                p.addCategory(category);
                if (p.getCategories().size() > 1){
                    p.removeCategory(defaultCategory);
                }
            }
        }
    }

    /** Removes specified Category from a product
     * @param product Product type
     * @param category Category type
     */
    synchronized public void removeCategoryFromProduct(Product product, Category category){
        for (Product p : catalog){
            if (product.equals(p)){
                p.removeCategory(category);
                if (p.getCategories().size() == 0){
                    p.addCategory(defaultCategory);
                }
            }
        }
    }

    //============================================================================
    //              Product Category Management
    //============================================================================


    /** Adds category to categories Arraylist
     * @param category Category type
     */
    synchronized public void addCategory(Category category){this.categories.add(category);}

    //TODO: we can delete this since we're not using it + no implementation
    //public Category findCategory(String c){return null;} //for checking to see if it already exists in list

    /** Removes a category and sets any product under that category to the default category
     * @param categoryToRemove Category
     */
    synchronized public void removeCategory(Category categoryToRemove){
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
    }

    /** Sets the default category
     * @param defaultCategory Category
     */
    synchronized public void setDefaultCategory(Category defaultCategory) {
        this.defaultCategory = defaultCategory;
    }
    //============================================================================
    //              Browsing and Searching
    //============================================================================

    /**searches both product name and description
     * @param str String : used to match name and description of a product
     * @return ArrayList of all matching products
     */
    synchronized public ArrayList<Product> searchProducts(String str){
        ArrayList<Product> matchingProducts = new ArrayList<>();
        //todo make this search method more precise so it doesn't need a full string
        for (Product p : catalog){
            if (p.getName().contains(str) || p.getDescription().contains(str)){
                matchingProducts.add(p);
            }
        }
        return matchingProducts;
    }

    /** Searches catalog for all products under matching Category
     * @param category Category
     * @return ArrayList of matching products
     */
    synchronized public ArrayList<Product> browseCategory(Category category){
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
    //              Product Details
    //============================================================================
    //TODO: we can delete this
    synchronized public String showProductDetails(Product p){return "";} //shows all product information: use of toString will make this easy


    //============================================================================
    //              Order Management
    //============================================================================

    /** Creates an empty Open Order for a customer
     * @param customer Customer
     * @throws IllegalArgumentException if customer already has an open order
     */
    synchronized public void createEmptyOrder(Customer customer) throws IllegalArgumentException {
        int orderNumber = createNewOrderNum();
        if(!customer.openOrderExists()){
            customer.createOpenOrder(orderNumber);
        } else {
            throw new IllegalArgumentException("Open order already exists");
        }
    }

    /** Add a Product to the Open Order for the Customer. Any duplicate order will be added to list
     * @param customer Customer
     * @param product Product
     * @throws IllegalArgumentException if trying to add products to an order that does not exist
     */
    synchronized public void addProductToOrder(Customer customer, Product product) throws IllegalArgumentException{
        if(customer.openOrderExists()){
            customer.getOpenOrder().addProduct(product);
        }
        else {
            throw new IllegalArgumentException("Open order does not exist");
        }
    }

    /** Remove product from customer's Open Order. Will remove all instances of particular product from order
     * @param customer Customer
     * @param product Product
     * @throws IllegalArgumentException if trying to add products to an order that does not exist
     */
    synchronized public void removeProductFromOrder(Customer customer, Product product) throws IllegalArgumentException{
        if(customer.openOrderExists()) {
            customer.getOpenOrder().removeProduct(product);
        }
        else {
            throw new IllegalArgumentException("Open order does not exist");
        }
    }

    /** Lists all products (removes duplicates) in a customer's open order
     * @param customer Customer
     * @return Arraylist of all products the order
     */
    //TODO: Thinking about returning a map and have duplicate orders be counted. eg "ThisBook" x2
    synchronized public String listOrderProducts(Customer customer){
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

    synchronized public ArrayList<Order> listCustomerFinalizedProducts(Customer customer){
        return customer.getFinalizedOrders();
    }

    /** Finalizes the customer's order. Once finalized they won't be able to add/remove products to this order
     * @param customer Customer
     * @throws IllegalArgumentException if trying to finalize an order that does not exist
     */
    synchronized public void finalizeOrder(Customer customer) throws IllegalArgumentException{
        if(customer.openOrderExists()){
            Order tempOrder = customer.getOpenOrder();
            tempOrder.finalizeOrder();
            orders.add(tempOrder);
            customer.finalizeOpenOrder();
        }
        else{
            throw new IllegalArgumentException("Open Order does not exist");
        }
    }

    /** Cancels the customer's current order. The order is deleted off the system.
     * @param customer Customer
     */
    synchronized public void cancelOrder(Customer customer){
        if(customer.openOrderExists()){
            customer.cancelOrder();
        }
    }

    /** Increments the order number each time an order is being made
     * @return int
     */
    synchronized private static int createNewOrderNum(){
        return orderNumbers++;
    }


    //============================================================================
    //              Order Report
    //============================================================================

    /** Lists all of the customers past (finalized) orders
     * @param customer Customer
     * @return ArrayList of Order(s)
     */
    synchronized public ArrayList<Order> listOrderReport(Customer customer){
       if(!customer.getFinalizedOrders().isEmpty()){
           return customer.getFinalizedOrders();
       }
       else{
           return null;
       }
    }

    /** Returns the order list between two specified dates
     * @param startDate LocalDate
     * @param endDate LocalDate
     * @return ArrayList of Order(s) between specified dates
     * @throws IllegalArgumentException if assigned endDate occurs before startDate
     */

    synchronized public ArrayList<Order> listOrdersByDate(LocalDate startDate, LocalDate endDate) throws IllegalArgumentException{
        ArrayList<Order> rangeOrderList = new ArrayList<>();
        if(startDate.isAfter(endDate) || endDate.isBefore(startDate)){
            throw new IllegalArgumentException("Assigned Start date is after End date");
        }
        if(!orders.isEmpty()){
            //should be already sorted based on how orders are finalized by current date
            for(Order o : this.orders){
                if((o.getFinalizedDate().isEqual(startDate) || o.getFinalizedDate().isAfter(startDate))
                        && (o.getFinalizedDate().isEqual(endDate) || o.getFinalizedDate().isBefore(endDate))){
                    rangeOrderList.add(o);

                }
            }
            return rangeOrderList;
        } else {
            return null;
        }

    }

    /** Checks to see if the customer has an open order
     * @param c Customer
     * @return true if Open Order exists, false if not
     */
    synchronized public boolean hasOpenOrder(Customer c){
        return c.openOrderExists();
    }

    //TODO: delete this when finalizing project. Was for testing purposes only.
    synchronized public void testFinalizedOrders(){
        Order temp = null;
        for(int i = 1; i < 13; i++){
            temp = new Order(createNewOrderNum());
            temp.finalizeOrder();
            temp.setFinalizedDate(LocalDate.of(2020,i,1));
            this.orders.add(temp);
        }

    }


    synchronized public ArrayList<User> getUsers(){
        return this.users;
    }

    synchronized public ArrayList<Order> getOrders(){
        return this.orders;
    }
    synchronized public ArrayList<Product> getCatalog(){
        return this.catalog;
    }
    synchronized public ArrayList<Category> getCategories(){
        return this.categories;
    }

    synchronized public String getName(){
        return this.name;
    }



}
