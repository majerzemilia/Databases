import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class OrderDetailsPK implements Serializable {

    @Column(name="ORDER_ID")
    private int order;
    @Column(name="PRODUCT_ID")
    private int product;

    public OrderDetailsPK(){}

    public OrderDetailsPK(Order order, Product product){
        this.order = order.getOrderID();
        this.product = product.getProductId();
    }
}
