package org.server.database;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

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

    public List<Move> getMovesByGameId(Long gameId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Move where game.id = :gameId", Move.class)
                    .setParameter("gameId", gameId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    // Metody do aktualizacji i odczytu ruchów mogą być dodane później
}

