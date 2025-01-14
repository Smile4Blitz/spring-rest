import jakarta.persistence.*;

public class DetachedExample {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        EntityManager em = emf.createEntityManager();

        // 1. Entity is in managed state
        em.getTransaction().begin();
        Product product = new Product();
        product.setName("Laptop");
        em.persist(product);  // Product is now managed
        em.getTransaction().commit();

        // 2. Explicit detach
        em.detach(product); // Product is now in a detached state
        System.out.println("Explicitly detached: " + em.contains(product)); // false

        // 3. Implicit detach (closing persistence context)
        em.close(); // Persistence context is closed, product becomes detached

        // 4. Reattaching with merge
        em = emf.createEntityManager(); // New persistence context
        em.getTransaction().begin();
        Product mergedProduct = em.merge(product); // Reattaches and becomes managed again
        mergedProduct.setName("Updated Laptop");
        em.getTransaction().commit();

        System.out.println("Managed again: " + em.contains(mergedProduct)); // true

        em.close();
        emf.close();
    }
}

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Getters and setters
}
