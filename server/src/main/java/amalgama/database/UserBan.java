package amalgama.database;

import jakarta.persistence.*;

@Entity
@Table(name = "gt_users_bans")
public class UserBan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_id")
    private long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "ban_user")
    private User user;

    @Column(name = "ban_reason")
    private String reason;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "ban_admin")
    private User admin;

    @Column(name = "ban_start")
    private long start;

    @Column(name = "ban_length")
    private long length;

    @Column(name = "ban_enable")
    private long isActive;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getIsActive() {
        return isActive;
    }

    public void setIsActive(long isActive) {
        this.isActive = isActive;
    }
}
