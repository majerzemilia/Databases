import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static SessionFactory sessionFactory = null;
    /*public static void main(String[] args) {
        System.out.println("Podaj nazwe produktu i aktualny stan magazynowy");
        Scanner inputScanner = new Scanner(System.in);
        String prodName = inputScanner.nextLine();
        int unitsOnStock = inputScanner.nextInt();
        Product prod = new Product("mleko", 20);
        Product prod2 = new Product("jogurt", 15);
        Product prod3 = new Product("kefir", 15);
        Product prod4 = new Product("jabłka", 17);
        sessionFactory = getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        /*session.save(prod);
        session.save(prod2);
        session.save(prod3);
        session.save(prod4);
        Product product = session.get(Product.class, 1);
        Product product2 = session.get(Product.class, 2);
        Product product3 = session.get(Product.class, 3);
        Product product4 = session.get(Product.class, 4);
        Category c = new Category("nabiał");
        session.save(c);
        c.addProduct(product);
        c.addProduct(product2);
        c.addProduct(product3);
        Category c1 = new Category("owoce");
        session.save(c1);
        c1.addProduct(product4);

        Invoice i1 = new Invoice(1, 0);
        session.save(i1);
        Invoice i2 = new Invoice(2, 0);
        session.save(i2);
        Invoice i3 = new Invoice(3, 0);
        session.save(i3);
        i1.addProduct(product, 5);
        i2.addProduct(product, 2);
        i1.addProduct(product2, 3);
        i1.addProduct(product4, 2);
        i3.addProduct(product3, 5);
        i3.addProduct(product2, 3);
        i3.addProduct(product, 1);


        Supplier s = new Supplier("FIRMA1", "Majowa", "Kraków");
        session.save(s);
        s.addSuppliedProduct(product);
        s.addSuppliedProduct(product2);
        s.addSuppliedProduct(product3);
        tx.commit();
        session.close();
    }*/

    public static void main(String[] args){
        EntityManagerFactory emf =  Persistence.
                createEntityManagerFactory("derby");
        EntityManager em = emf.createEntityManager();
        EntityTransaction etx = em.getTransaction();
        etx.begin();

        /*Order o = new Order();
        em.persist(o);*/
        //Customer c = em.find(Customer.class, 24);
        //Order o = em.find(Order.class, 101);
        //c.addOrder(o);
        /*Product p = em.find(Product.class, 6);
        Product p1 = em.find(Product.class, 7);
        o.addProduct(p, 2);
        o.addProduct(p1, 3);*/
        etx.commit();
        em.close();
    }

    private static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            sessionFactory = configuration.configure().buildSessionFactory();
        }
        return sessionFactory;
    }
}