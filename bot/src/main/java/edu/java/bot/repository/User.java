package edu.java.bot.repository;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class User {
    private final Long id;
    private final Set<URI> links = new HashSet<>();
    private UserState userState = UserState.REGULAR;
}
