package de.rhkoeln.se.backend;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * BE und DB verbinden: liest und schreibt Daten direkt über den EntityManager,
 * ohne die ganze Anwendung zu starten. Konfiguration: META-INF/persistence.xml.
 */
class MessagePersistenceTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private Message message;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("integration-test"); // Name aus persistence.xml
        em = emf.createEntityManager();
        em.getTransaction().begin();
        message = new Message("Eduard");
        em.persist(message);
        em.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    void persistedMessageCanBeReadBack() {
        em.clear(); // Cache leeren, damit wirklich aus der DB gelesen wird
        Message found = em.find(Message.class, message.getId());

        assertThat(found.getText()).isEqualTo("Eduard");
        assertThat(found.getCreatedAt()).isNotNull();
    }

    @Test
    void queryReturnsAllMessages() {
        em.getTransaction().begin();
        em.persist(new Message("Zweite"));
        em.getTransaction().commit();

        List<String> texts = em.createQuery("select m.text from Message m order by m.id", String.class)
                .getResultList();

        assertThat(texts).containsExactly("Eduard", "Zweite");
    }

    @Test
    void updateIsWrittenToDatabase() {
        em.getTransaction().begin();
        message.setText("Geändert");
        em.getTransaction().commit();
        em.clear();

        assertThat(em.find(Message.class, message.getId()).getText()).isEqualTo("Geändert");
    }
}
