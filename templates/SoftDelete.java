@Entity
@SQLDelete(sql = "UPDATE table_name SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Example {
    private boolean deleted = false;
}
