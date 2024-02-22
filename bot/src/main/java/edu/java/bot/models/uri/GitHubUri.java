package edu.java.bot.models.uri;

import org.springframework.stereotype.Component;

@Component
public class GitHubUri extends ValidUri {
    public GitHubUri() {
        host = "github.com";
    }
}
