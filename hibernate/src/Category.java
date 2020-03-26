import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int categoryID;

    private String name;

    @OneToMany
    @JoinColumn(name="CATEGORY_FK")
    private List<Product> products = new ArrayList<>();

    public Category(){}

    public Category(String name){
        this.name = name;
    }

    public void addProduct(Product product){
        this.products.add(product);
        product.setCategory(this);
    }

    public List<Product> getProducts() {
        return products;
    }

    public String toString(){
        return "Category name: " + this.name;
    }
}
