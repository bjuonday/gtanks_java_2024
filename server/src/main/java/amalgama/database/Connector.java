package amalgama.database;

import org.hibernate.Session;

public class Connector {
    public static void checkConnection() {
        try (Session session = HibernateUtil.getFactory().openSession()) {
            boolean ok = session.isConnected() && session.isOpen();
            System.out.println("[Database] Connection db: " + (ok ? "OK" : "FAILURE"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
