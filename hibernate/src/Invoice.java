import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int invoiceNumber;
    private int quantity;

    @ManyToMany(mappedBy = "invoices", cascade = {CascadeType.PERSIST})
    private List<Product> products = new ArrayList<>();

    public Invoice(){}
    public Invoice(int invoiceNumber, int quantity) {
        this.invoiceNumber = invoiceNumber;
        this.quantity = quantity;
    }

    public void addProduct(Product p, int quantity) {
        if(p.getUnitsOnStock() - quantity < 0) return;
        p.takeFromStock(quantity);
        this.products.add(p);
        p.addInvoice(this, quantity);
        this.quantity += quantity;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String toString(){
        return "Invoice nr: " + this.invoiceNumber;
    }

    public String soldProducts(){
        String result = "Sold products:" + "\n";
        for(Product p: this.products)
            result += p.toString() + "\n";
        return result;
    }
}

