import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer extends Company {

    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;*/

    private double discount;

    @OneToMany
    @JoinColumn(name="CUSTOMER_FK")
    private List<Order> orders = new ArrayList<>();

    public Customer(){}
    public Customer(String company, String street, String city, String zipCode){
        super(company, street, city, zipCode);
    }

    public Customer(String company, String street, String city, String zipCode, double discount){
        super(company, street, city, zipCode);
        this.discount = discount;
    }

    public double getDiscount() {
        return this.discount;
    }

    public List<Order> getOrders(){
        return this.orders;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void addOrder(Order order){
        this.orders.add(order);
        order.setCustomer(this);
    }

    public String toString(){
        return super.toString() + "discount: " + this.discount + "\n";
    }
}
