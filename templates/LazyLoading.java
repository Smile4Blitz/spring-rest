@Entity
public class Order {
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<Item> items; // Items worden pas geladen wanneer je order.getItems() aanroept
}

@Entity
public class Order {
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<Item> items; // Items worden altijd geladen bij het ophalen van een Order
}
