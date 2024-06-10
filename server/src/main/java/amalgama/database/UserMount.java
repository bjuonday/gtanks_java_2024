package amalgama.database;

import jakarta.persistence.*;

@Entity
@Table(name = "gt_users_mount")
public class UserMount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mount_id")
    private long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "mount_user")
    private User user;

    @Column(name = "mount_weapon")
    private String weaponId;

    @Column(name = "mount_armor")
    private String armorId;

    @Column(name = "mount_color")
    private String colorId;

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

    public String getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(String weaponId) {
        this.weaponId = weaponId;
    }

    public String getArmorId() {
        return armorId;
    }

    public void setArmorId(String armorId) {
        this.armorId = armorId;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }
}
