package amalgama.database.dao;

import amalgama.database.HibernateUtil;
import amalgama.database.User;
import amalgama.database.UserMount;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class UserMountDAO {
    public static UserMount getMount(Long id) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            return session.get(UserMount.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserMount getMount(User user) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            Query q = session.createQuery("from UserMount where user = ?1")
                    .setParameter(1, user);
            var list = q.list();
            if (!list.isEmpty())
                return (UserMount) list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addMount(UserMount mount) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            session.beginTransaction();
            session.persist(mount);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateMount(UserMount mount) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            session.beginTransaction();
            session.merge(mount);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeMount(UserMount mount) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            session.beginTransaction();
            session.remove(mount);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
