import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Supplier extends Company{

    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;*/

    private String bankAccountNumber;

    @OneToMany
    @JoinColumn(name="SUPPLIER_FK")
    private Set<Product> suppliedProducts = new HashSet<>();

    public Supplier(){
    }

    public Supplier(String company, String street, String city, String zipCode, String bankAccountNumber){
        super(company, street, city, zipCode);
        this.bankAccountNumber = bankAccountNumber;
    }

    public void addSuppliedProduct(Product product){
        this.suppliedProducts.add(product);
        product.setSupplier(this);
    }

    public Set<Product> getSuppliedProducts() {
        return suppliedProducts;
    }

    public String toString(){
        return super.toString() + "bank account number: " + this.bankAccountNumber +"\n";
    }
}
