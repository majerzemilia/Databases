import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name="SUPPLIER_FK")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name="CATEGORY_FK")
    private Category category;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    private Set<Invoice> invoices = new HashSet<>();

    //@ManyToMany(mappedBy = "products")
    //private Set<Order> orders = new HashSet<>();

    private String productName;
    private int unitsOnStock;

    public Product(){}
    public Product(String name, int unitsOnStock){
        this.productName = name;
        this.unitsOnStock = unitsOnStock;
    }

    public Product(String name, int unitsOnStock, Supplier supplier){
        this.productName = name;
        this.unitsOnStock = unitsOnStock;
        this.supplier = supplier;
    }

    public void setSupplier(Supplier supplier){
        this.supplier = supplier;
        if(!supplier.getSuppliedProducts().contains(this)) supplier.addSuppliedProduct(this);
    }

    public void setCategory(Category category){
        this.category = category;
        if(!category.getProducts().contains(this)) category.addProduct(this);
    }

    public void addInvoice(Invoice invoice, int quantity){
        this.invoices.add(invoice);
        if(!invoice.getProducts().contains(this)) invoice.addProduct(this, quantity);
    }

    public Category getCategory(){
        return this.category;
    }

    public void takeFromStock(int quantity){
        this.unitsOnStock -= quantity;
    }

    public void addToStock(int quantity){
        this.unitsOnStock += quantity;
    }

    public int getUnitsOnStock() {
        return unitsOnStock;
    }

    public int getProductId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String toString(){
        return "Product name: " + this.productName + ", " + "Units on stock: " + this.unitsOnStock
                + " " + this.getCategory().toString();
    }

    public String getInvoices(){
        String result="Invoices:\n";
        for(Invoice i: this.invoices)
            result += i.toString() + "\n";
        return result;
    }
}
