package org.server.database;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class MoveDAO {

    public void saveMove(Move move) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Rozpocznij transakcję
            transaction = session.beginTransaction();

            // Zapisz obiekt ruchu w bazie danych
            session.save(move);

            // Zatwierdź transakcję
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Metody do aktualizacji i odczytu ruchów mogą być dodane później
}

