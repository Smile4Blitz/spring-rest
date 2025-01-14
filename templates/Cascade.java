import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);

        Order order = em.find(Order.class, 1L);
        order.removeItem(order.getItems().get(0)); // Verwijdert een item
    }
}

public class CascadePersistExample {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Order order = new Order();
        order.setCustomerName("John Doe");

        Item item1 = new Item();
        item1.setProductName("Laptop");
        item1.setPrice(1000);

        Item item2 = new Item();
        item2.setProductName("Mouse");
        item2.setPrice(20);

        order.addItem(item1); // Voeg items toe aan de order
        order.addItem(item2);

        em.persist(order); // Dankzij CascadeType.PERSIST worden items ook opgeslagen
        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}

public class CascadeRemoveExample {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Order order = em.find(Order.class, 1L); // Laad een bestaande order
        em.remove(order); // Dankzij CascadeType.REMOVE worden de items ook verwijderd

        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}

public class CascadeMergeExample {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Order detachedOrder = new Order();
        detachedOrder.setId(1L); // Stel een bestaande ID in
        detachedOrder.setCustomerName("Updated Customer");

        Item item = new Item();
        item.setId(1L); // Stel een bestaande ID in
        item.setProductName("Updated Laptop");
        item.setPrice(1200);
        detachedOrder.addItem(item);

        em.merge(detachedOrder); // Dankzij CascadeType.MERGE worden de items ook geüpdatet
        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}


@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) // Cascade toegepast
    private List<Item> items = new ArrayList<>();

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        items.add(item);
        item.setOrder(this); // Zorg ervoor dat de associatie aan beide kanten consistent is
    }

    public void removeItem(Item item) {
        items.remove(item);
        item.setOrder(null);
    }
}

import jakarta.persistence.*;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private double price;

    @ManyToOne
    private Order order;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
