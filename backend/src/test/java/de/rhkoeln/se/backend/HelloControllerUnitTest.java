package de.rhkoeln.se.backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Reiner Unit-Test ohne Spring: das Repository wird durch einen Mock ersetzt. */
@ExtendWith(MockitoExtension.class)
class HelloControllerUnitTest {

    @Mock
    private MessageRepository repository;

    @InjectMocks
    private HelloController controller;

    @Test
    void helloReturnsFixedString() {
        assertThat(controller.message()).isEqualTo("Hallo vom Backend!");
    }

    @Test
    void messagesReturnsEverythingFromRepository() {
        List<Message> stored = List.of(new Message("a"), new Message("b"));
        when(repository.findAll()).thenReturn(stored);

        assertThat(controller.messages()).isSameAs(stored);
    }

    @Test
    void createSavesOnlyTheText() {
        when(repository.save(any(Message.class))).thenAnswer(call -> call.getArgument(0));

        Message result = controller.create(new Message("Neu"));

        ArgumentCaptor<Message> saved = ArgumentCaptor.forClass(Message.class);
        verify(repository).save(saved.capture());
        assertThat(saved.getValue().getText()).isEqualTo("Neu");
        assertThat(saved.getValue().getId()).isNull();
        assertThat(result.getText()).isEqualTo("Neu");
    }
}
