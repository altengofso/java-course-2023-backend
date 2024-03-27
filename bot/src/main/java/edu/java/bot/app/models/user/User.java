package edu.java.bot.app.models.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class User {
    private final long id;
    private UserState userState = UserState.REGULAR;
}
