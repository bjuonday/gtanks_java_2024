package amalgama.database.dao;

import amalgama.database.Group;
import amalgama.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class GroupDAO {
    public static Group getGroup(String name) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            Query q = session.createQuery("from Group where name = ?1")
                    .setParameter(1, name);
            List<Group> rows = q.list();
            if (!rows.isEmpty())
                return rows.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Group getGroup(Long id) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            return session.get(Group.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
