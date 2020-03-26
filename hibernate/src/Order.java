import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="\"ORDER\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name="CUSTOMER_FK")
    private Customer customer;

    @OneToMany(mappedBy = "id.order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<OrderDetails> orderDetails = new HashSet<>();

    public Order() {
    }

    public Order(Customer c) {
        this.setCustomer(c);
    }

    public int getOrderID() {
        return this.id;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {

        this.customer = customer;
        if(!customer.getOrders().contains(this)) customer.addOrder(this);
    }

    public void addProduct(Product product, int quantity) {
        if(product.getUnitsOnStock() - quantity < 0){
            returnErrorMessage("Not enough units on stock. Cannot add product.");
            return;
        }
        OrderDetails od = new OrderDetails(this, product, quantity);
        product.takeFromStock(quantity);
        this.orderDetails.add(od);
    }

    public Set<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void returnErrorMessage(String message){
        System.out.println(message);
    }

    public String toString(){
        String result="Order ID: " + this.id + "\n";
        for(OrderDetails od: this.orderDetails) result += od.toString() +"\n";
        return result;
    }
}
