package de.rhkoeln.se.backend;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final MessageRepository repository;

    public HelloController(MessageRepository repository) {
        this.repository = repository;
    }

    /** Einfache Methode, die einen String zurückliefert (Aufgabe „BE-Projekt aufsetzen“). */
    @GetMapping("/message")
    public String message() {
        return "Hallo vom Backend!";
    }

    /** Liest alle Nachrichten aus der Datenbank (BE und DB verbinden). */
    @GetMapping("/messages")
    public List<Message> messages() {
        return repository.findAll();
    }

    /** Legt eine neue Nachricht in der Datenbank an. */
    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public Message create(@RequestBody Message message) {
        return repository.save(new Message(message.getText()));
    }
}
