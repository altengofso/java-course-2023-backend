package edu.java.bot.models.user;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class User {
    private final long id;
    private final Set<URI> links = new HashSet<>();
    private UserState userState = UserState.REGULAR;

    public String getLinksList() {
        return this.getLinks().stream().map(URI::toString).collect(Collectors.joining("\n"));
    }
}
