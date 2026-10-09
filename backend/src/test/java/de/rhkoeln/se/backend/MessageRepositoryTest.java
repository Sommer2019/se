package de.rhkoeln.se.backend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

/** Testet das Zusammenspiel von Entity, Repository und schema.sql/data.sql auf einer In-Memory-H2. */
@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository repository;

    @Test
    void seedDataIsLoaded() {
        assertThat(repository.findAll())
                .extracting(Message::getText)
                .containsExactly("Hallo aus der Datenbank!", "Zweite Nachricht", "Dritte Nachricht");
    }

    @Test
    void saveAssignsNextIdAndTimestamp() {
        Message saved = repository.saveAndFlush(new Message("Neu"));

        assertThat(saved.getId()).isEqualTo(4L);
        assertThat(repository.findById(saved.getId())).get()
                .extracting(Message::getText).isEqualTo("Neu");
    }
}
