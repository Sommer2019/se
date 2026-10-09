package de.rhkoeln.se.backend;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** Testet nur die Web-Schicht (Routing, JSON, CORS), ohne Datenbank. */
@WebMvcTest(HelloController.class)
class HelloControllerWebTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MessageRepository repository;

    @Test
    void helloReturnsPlainString() throws Exception {
        mvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hallo vom Backend!"));
    }

    @Test
    void messagesAreSerializedAsJson() throws Exception {
        when(repository.findAll()).thenReturn(List.of(new Message("Eins"), new Message("Zwei")));

        mvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].text").value("Zwei"));
    }

    @Test
    void postReturnsCreated() throws Exception {
        when(repository.save(any(Message.class))).thenAnswer(call -> call.getArgument(0));

        mvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Neu\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("Neu"));
    }

    @Test
    void corsAllowsViteDevServer() throws Exception {
        mvc.perform(options("/api/messages")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    }

    @Test
    void corsRejectsOtherOrigins() throws Exception {
        mvc.perform(options("/api/messages")
                        .header("Origin", "http://evil.example")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden());
    }
}
