package amalgama.database.dao;

import amalgama.database.HibernateUtil;
import amalgama.database.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UserDAO {
    public static User getUser(Long id) {
        User user = null;
        try (Session session = HibernateUtil.getFactory().openSession()) {
            user = session.get(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getUser(String login) {
        User user = null;
        try (Session session = HibernateUtil.getFactory().openSession()) {
            Query q = session.createQuery("from User where login = ?1")
                    .setParameter(1, login);
            List<User> rows = q.list();
            user = (rows.size() > 0 ? rows.get(0) : null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getUser(String login, String password) {
        User user = null;
        try (Session session = HibernateUtil.getFactory().openSession()) {
            Query q = session.createQuery("from User where login = ?1 and password = MD5(?2)")
                    .setParameter(1, login)
                    .setParameter(2, password);
            List<User> rows = q.list();
            if (!rows.isEmpty()) {
                user = rows.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void addUser(User newUser) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            session.beginTransaction();
            session.persist(newUser);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateUser(User user) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(User user) {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
