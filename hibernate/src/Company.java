import javax.persistence.*;

@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public abstract class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String company;
    private String street;
    private String city;
    private String zipCode;


    public Company(){}

    public Company(String company, String street, String city, String zipCode){
        this.company = company;
        this. street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    public int getId() {
        return id;
    }

    public String toString(){
        return "Company: " + this.company +"\nStreet: " + this.street + "\ncity: " + this.city + "\nzip code: "
                + this.zipCode + "\n";
    }

}
