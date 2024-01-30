package org.server.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class GameDAO {

    public void saveGame(Game game) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Rozpocznij transakcję
            transaction = session.beginTransaction();

            // Zapisz obiekt gry w bazie danych
            session.save(game);

            // Zatwierdź transakcję
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateGame(Game game) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Rozpocznij transakcję
            transaction = session.beginTransaction();

            // Aktualizuj obiekt gry w bazie danych
            session.update(game);

            // Zatwierdź transakcję
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Game getGameById(Long gameId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Pobierz grę o podanym ID
            return session.get(Game.class, gameId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Game> getAllGames() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Game> query = session.createQuery("from Game", Game.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // inne metody do aktualizacji i odczytu gry mogą być dodane później
}

