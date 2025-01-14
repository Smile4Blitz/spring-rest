import jakarta.persistence.*;
import java.util.List;

public class LazyLoadingExample {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        
        // Maak een Order en Items aan
        Order order = new Order();
        order.setCustomerName("John Doe");
        em.persist(order);

        Item item1 = new Item();
        item1.setProductName("Laptop");
        item1.setPrice(1200.00);
        item1.setOrder(order);
        em.persist(item1);

        Item item2 = new Item();
        item2.setProductName("Mouse");
        item2.setPrice(25.00);
        item2.setOrder(order);
        em.persist(item2);

        em.getTransaction().commit();

        // Laad de Order zonder de Items direct te laden
        Order fetchedOrder = em.find(Order.class, order.getId());
        System.out.println("Order geladen: " + fetchedOrder.getCustomerName());

        // Toegang tot items (triggert lazy loading)
        System.out.println("Items:");
        for (Item item : fetchedOrder.getItems()) {
            System.out.println("- " + item.getProductName() + " (€" + item.getPrice() + ")");
        }
    }
}

public class EagerLoadingExample {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        
        // Laad een Item en controleer of de gerelateerde Order eager wordt geladen
        Item fetchedItem = em.find(Item.class, 1L); // Voorbeeld: laad item met ID 1
        System.out.println("Item geladen: " + fetchedItem.getProductName());

        // Toegang tot de Order (eager loading betekent dat dit al geladen is)
        Order relatedOrder = fetchedItem.getOrder();
        System.out.println("Gerelateerde order: " + relatedOrder.getCustomerName());

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

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY) // Lazy loading
    private List<Item> items;

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
}

import jakarta.persistence.*;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private double price;

    @ManyToOne(fetch = FetchType.EAGER) // Eager loading
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
