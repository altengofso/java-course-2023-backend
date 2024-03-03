package edu.java.bot.app.models.uri;

import org.springframework.stereotype.Component;

@Component
public class StackOverflowUri extends ValidUri {
    public StackOverflowUri() {
        host = "stackoverflow.com";
    }
}
