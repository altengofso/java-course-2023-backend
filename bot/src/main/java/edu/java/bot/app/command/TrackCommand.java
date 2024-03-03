package edu.java.bot.app.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.app.models.user.User;
import edu.java.bot.app.models.user.UserState;
import edu.java.bot.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("track")
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private static final String COMMAND = "/track";
    private static final String DESCRIPTION = "добавить ссылку в отслеживание";
    private final static String AWAITING_TRACK_LINK = "введите ссылку для отслеживания";
    private final UserRepository userRepository;

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public synchronized SendMessage handle(long id) {
        User user = userRepository.findById(id);
        user.setUserState(UserState.AWAITING_TRACK_LINK);
        return new SendMessage(id, AWAITING_TRACK_LINK);
    }
}
