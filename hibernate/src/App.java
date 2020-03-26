import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class App {

    private EntityManager em;
    private Customer customer;
    private Order order;
    private Map<Integer, Consumer<Scanner>> menuOptions;
    private Map<Integer, Consumer<Scanner>> logMenu;

    public App(){
        EntityManagerFactory emf =  Persistence.
                createEntityManagerFactory("derby");
        this.em = emf.createEntityManager();
        this.logMenu = createLogMenu();
        this.menuOptions = createMenu();
    }

    private Map<Integer, Consumer<Scanner>> createLogMenu(){
        Map<Integer, Consumer<Scanner>> result = new HashMap<>();
        result.put(1, this::logIn);
        result.put(2, this::signUp);
        result.put(3, this::exit);
        return result;
    }

    private Map<Integer, Consumer<Scanner>> createMenu(){
        Map<Integer, Consumer<Scanner>> result = new HashMap<>();
        result.put(1, this::showMenu);
        result.put(2, this::listCategories);
        result.put(3, this::listProducts);
        result.put(4, this::createNewOrder);
        result.put(5, this::addProductToOrder);
        result.put(6, this::finishOrder);
        result.put(7, this::listOrders);
        result.put(8, this::deleteOrder);
        result.put(9, this::exit);
        return result;
    }

    public void logIn(Scanner scanner){
        printMessage("Enter name of your company");
        String customerCompany = scanner.nextLine();
        EntityTransaction etx = em.getTransaction();
        etx.begin();
        TypedQuery<Customer> q = em.createQuery("from Customer as c where c.company=:companyName", Customer.class);
        q.setParameter("companyName", customerCompany);
        Customer c = q.getSingleResult();
        if(c == null){
            returnErrorMessage("No such customer in database!");
            return;
        }
        this.customer = q.getSingleResult();
        etx.commit();
        showMenu(scanner);
    }

    public void signUp(Scanner scanner){
        printMessage("Enter name of your company, address: street, city and zipCode");
        String company = scanner.nextLine();
        String street = scanner.nextLine();
        String city = scanner.nextLine();
        String zipCode = scanner.nextLine();
        Customer c = new Customer(company, street, city, zipCode);
        EntityTransaction etx = em.getTransaction();
        etx.begin();
        em.persist(c);
        TypedQuery<Customer> q = em.createQuery("from Customer as c where c.company=:companyName", Customer.class);
        q.setParameter("companyName", company);
        this.customer = q.getSingleResult();
        etx.commit();
        showMenu(scanner);
    }

    public void createNewOrder(Scanner scanner){
        EntityTransaction etx = em.getTransaction();
        etx.begin();
        Order order = new Order(this.customer);
        em.persist(order);
        etx.commit();
        etx = em.getTransaction();
        etx.begin();
        TypedQuery<Order> q = em.createQuery("from Order as o where o.customer.id = :id order by o.id desc",
                Order.class);
        q.setParameter("id", this.customer.getId());
        this.order = q.getResultList().get(0);
        etx.commit();
        printMessage("Ready to add products to your order!");
        waitForOption(scanner);
    }

    public void addProductToOrder(Scanner scanner){
        printMessage("Enter name of product and quantity");
        String name = scanner.nextLine();
        int quantity = scanner.nextInt();
        scanner.nextLine();
        EntityTransaction etx = em.getTransaction();
        etx.begin();
        TypedQuery<Product> p = em.createQuery("from Product as p where p.productName = :name", Product.class);
        p.setParameter("name", name);
        Product product = p.getSingleResult();
        if(product == null){
            returnErrorMessage("No such product!");
            etx.commit();
            waitForOption(scanner);
        }
        this.order.addProduct(product, quantity);
        etx.commit();
        printMessage("Product successfully added to order");
        waitForOption(scanner);
    }

    public void finishOrder(Scanner scanner){
        EntityTransaction etx = em.getTransaction();
        etx.begin();
        TypedQuery<Order> q = em.createQuery("from Order as o where o.id = :id", Order.class);
        q.setParameter("id", this.order.getOrderID());
        Order o = q.getSingleResult();
        etx.commit();
        if(o.getOrderDetails().isEmpty()){
            returnErrorMessage("Cannot finish empty order!");
            waitForOption(scanner);
        }
        this.order = null;
        printMessage("Order successfully finished");
        waitForOption(scanner);
    }

    public void listCategories(Scanner scanner){
        EntityTransaction etx = em.getTransaction();
        etx.begin();
        TypedQuery<Category> q = em.createQuery("from Category as c", Category.class);
        List<Category> categories = q.getResultList();
        for(Category c: categories) System.out.println(c.toString());
        etx.commit();
        waitForOption(scanner);
    }

    public void listProducts(Scanner scanner){
        printMessage("Enter name of category to see its products");
        String categoryName = scanner.nextLine();
        EntityTransaction etx = em.getTransaction();
        etx.begin();
        TypedQuery<Product> q = em.createQuery("from Product as p where p.category.name = :name", Product.class);
        q.setParameter("name", categoryName);
        List<Product> products = q.getResultList();
        if(products.isEmpty()){
            returnErrorMessage("No such category!");
            etx.commit();
            waitForOption(scanner);
        }
        for(Product p: products) System.out.println(p.toString());
        etx.commit();
        waitForOption(scanner);
    }

    public void listOrders(Scanner scanner){
        EntityTransaction etx = em.getTransaction();
        etx.begin();
        TypedQuery<Order> q = em.createQuery("from Order as o where o.customer.id = :id order by o.id",
                Order.class);
        q.setParameter("id", this.customer.getId());
        List<Order> orders = q.getResultList();
        if(orders.isEmpty()){
            returnErrorMessage("No orders");
            etx.commit();
            waitForOption(scanner);
        }
        for(Order o: orders){
            System.out.println(o.toString() + "\n");
        }
        etx.commit();
        waitForOption(scanner);
    }

    public void deleteOrder(Scanner scanner){
        printMessage("Enter ID of order you want to delete");
        int orderid = scanner.nextInt();
        scanner.nextLine();
        EntityTransaction etx = em.getTransaction();
        etx.begin();
        TypedQuery<Order> q = em.createQuery("from Order as o where o.id = :id", Order.class);
        q.setParameter("id", orderid);
        Order o = q.getSingleResult();
        if(o == null){
            returnErrorMessage("No such order!");
            etx.commit();
            waitForOption(scanner);
        }
        for(OrderDetails od: o.getOrderDetails()){
            od.getProduct().addToStock(od.getQuantity());
        }
        em.remove(o);
        etx.commit();
        printMessage("Order successfully deleted");
        waitForOption(scanner);
    }

    public void showLogMenu(Scanner scanner){
        printMessage("To log in, enter 1 \nTo sign up, enter 2 \nTo exit, enter 3");
        int choice = scanner.nextInt();
        scanner.nextLine();
        logMenu.get(choice).accept(scanner);
    }

    public void waitForOption(Scanner scanner){
        int choice = scanner.nextInt();
        scanner.nextLine();
        menuOptions.get(choice).accept(scanner);
    }

    public void showMenu(Scanner scanner){
        printMessage("To see menu, enter 1 \nTo see categories, enter 2 \nTo see products, enter 3 \n" +
                "To create new order, enter 4 \nTo add product to order, enter 5 \nTo finish order, enter 6 \n" +
                "To list your orders, enter 7 \nTo delete order, enter 8 \nTo exit, enter 9");
        waitForOption(scanner);
    }

    public void exit(Scanner scanner){
        em.close();
        System.exit(0);
    }

    public static void main(String[] args){
        EntityManagerFactory emf =  Persistence.
                createEntityManagerFactory("derby");
        EntityManager em = emf.createEntityManager();
        App app = new App();
        Scanner scanner = new Scanner(System.in);
        app.showLogMenu(scanner);
    }

    public void returnErrorMessage(String message){
        System.out.println(message);
    }
    public void printMessage(String message){
        System.out.println(message);
    }
}
