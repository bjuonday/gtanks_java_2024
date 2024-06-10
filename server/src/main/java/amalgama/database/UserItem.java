package amalgama.database;

import jakarta.persistence.*;

@Entity
@Table(name = "gt_users_items")
public class UserItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "garage_id")
    private long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "item_user")
    private User user;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_count")
    private int count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
