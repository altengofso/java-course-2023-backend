package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.processor.URIProcessor;
import edu.java.bot.repository.User;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.repository.UserState;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkService {
    public final static String AWAITING_TRACK_LINK = "введите ссылку для отслеживания";
    public static final String SUCCESS_TRACK = "ссылка успешно добавлена в отслеживание";
    public static final String SUCCESS_UNTRACK = "ссылка успешно удалена из отслеживания";
    public static final String FAIL = "произошла ошибка, ссылка не поддерживается";
    public static final String NO_LINKS = "Нет отслеживаемых ссылок";
    private final UserRepository userRepository;
    private final URIProcessor uriProcessor;

    public String getAllLinks(Update update) {
        User user = userRepository.findById(update.message().chat().id());
        String result = user.getLinks().stream().map(URI::toString).collect(Collectors.joining("\n"));
        return result.isEmpty() ? NO_LINKS : result;
    }

    public String trackLink(Update update) {
        User user = userRepository.findById(update.message().chat().id());
        if (user.getUserState().equals(UserState.REGULAR)) {
            user.setUserState(UserState.AWAITING_TRACK_LINK);
            return AWAITING_TRACK_LINK;
        }
        user.setUserState(UserState.REGULAR);
        try {
            URI uri = new URI(update.message().text());
            if (uriProcessor.isValidURI(uri)) {
                user.getLinks().add(uri);
                return SUCCESS_TRACK;
            }
            return FAIL;
        } catch (URISyntaxException e) {
            return FAIL;
        }
    }

    public String untrackLink(Update update) {
        User user = userRepository.findById(update.message().chat().id());
        if (user.getUserState().equals(UserState.REGULAR)) {
            String links = getAllLinks(update);
            if (!links.equals(NO_LINKS)) {
                user.setUserState(UserState.AWAITING_UNTRACK_LINK);
            }
            return links;
        }
        user.setUserState(UserState.REGULAR);
        try {
            URI uri = new URI(update.message().text());
            if (uriProcessor.isValidURI(uri)) {
                user.getLinks().remove(uri);
                return SUCCESS_UNTRACK;
            }
            return FAIL;
        } catch (URISyntaxException e) {
            return FAIL;
        }
    }
}
