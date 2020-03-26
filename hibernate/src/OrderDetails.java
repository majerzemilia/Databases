import javax.persistence.*;

@Entity
public class OrderDetails {

    @EmbeddedId
    private OrderDetailsPK id;

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    private Product product;

    private int quantity;

    public OrderDetails(){}

    public OrderDetails(Order order, Product product, int quantity){
        this.product = product;
        this.quantity = quantity;
        this.id = new OrderDetailsPK(order, product);
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public String toString(){
        return "Product name: " + this.product.getProductName() + ", quantity: " + this.quantity;
    }
}
