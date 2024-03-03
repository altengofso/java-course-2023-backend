package edu.java.bot.app.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.app.models.user.User;
import edu.java.bot.app.repository.UserRepository;
import edu.java.bot.app.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NonSlashCommand {
    private static final String FAIL = "неподдерживаемая команда";
    private final UserRepository userRepository;
    private final LinkService linkService;

    public SendMessage handle(long id, String message) {
        User user = userRepository.findById(id);
        return new SendMessage(id, switch (user.getUserState()) {
            case AWAITING_TRACK_LINK -> linkService.trackLink(user, message);
            case AWAITING_UNTRACK_LINK -> linkService.untrackLink(user, message);
            case REGULAR -> FAIL;
        });
    }
}
