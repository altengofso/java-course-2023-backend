package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NullCommand {
    public static final String FAIL = "неподдерживаемая команда";
    private final UserRepository userRepository;
    private final LinkService linkService;

    public SendMessage handle(Update update) {
        Long id = update.message().chat().id();
        User user = userRepository.findById(id);
        return new SendMessage(id, switch (user.getUserState()) {
            case AWAITING_TRACK_LINK -> linkService.trackLink(update);
            case AWAITING_UNTRACK_LINK -> linkService.untrackLink(update);
            case REGULAR -> FAIL;
        });
    }
}
