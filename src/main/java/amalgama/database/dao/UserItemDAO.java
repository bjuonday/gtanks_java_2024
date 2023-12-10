package amalgama.database.dao;

import amalgama.database.HibernateUtil;
import amalgama.database.User;
import amalgama.database.UserItem;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserItemDAO {
    public static UserItem getItem(Long id) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            return session.get(UserItem.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UserItem> getUserItems(User user) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            Query q = session.createQuery("from UserItem where user = ?1").setParameter(1, user);
            return q.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getUserItemIds(User user) {
        List<String> ids = new ArrayList<>();
        try (Session session = HibernateUtil.getFactory().openSession()) {
            Query q = session.createQuery("from UserItem where user = ?1").setParameter(1, user);
            for (var i : q.list())
                ids.add(((UserItem)i).getItemId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    public static UserItem getUserItem(User user, String id) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            Query q = session.createQuery("from UserItem where itemId = ?1 AND user = ?2")
                    .setParameter(1, id)
                    .setParameter(2, user);
            var list = q.list();
            if (!list.isEmpty())
                return (UserItem) list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addItem(UserItem item) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            session.beginTransaction();
            session.persist(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateItem(UserItem item) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            session.beginTransaction();
            session.merge(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteItem(UserItem item) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            session.beginTransaction();
            session.remove(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
