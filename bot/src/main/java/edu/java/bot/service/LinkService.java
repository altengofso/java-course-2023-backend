package edu.java.bot.service;

import edu.java.bot.models.user.User;
import edu.java.bot.models.user.UserState;
import edu.java.bot.utils.UriValidator;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class LinkService {
    private static final String SUCCESS_TRACK = "ссылка успешно добавлена в отслеживание";
    private static final String SUCCESS_UNTRACK = "ссылка успешно удалена из отслеживания";
    private static final String FAIL = "произошла ошибка, ссылка не поддерживается";
    private static final String TRACK_LOG_ERROR =
        "ошибка добавления ссылки %s в отслеживание пользователя %d. Ссылка не поддерживается";
    private static final String UNTRACK_LOG_ERROR =
        "ошибка удаления ссылки %s из отслеживания пользователя %d. Ссылка не поддерживается";
    private static final String LINK_PARSE_LOG_ERROR = "ошибка парсинга ссылки %s, предоставленной пользователем %d";
    private static final String MULTIPLE_ATTEMPT_ERROR =
        "ошибка многократной отправки ссылки для изменения отслеживания";
    private final UriValidator uriValidator;

    public synchronized String trackLink(User user, String link) {
        if (!user.getUserState().equals(UserState.AWAITING_TRACK_LINK)) {
            log.error(user.getId() + " " + MULTIPLE_ATTEMPT_ERROR);
            return MULTIPLE_ATTEMPT_ERROR;
        }
        user.setUserState(UserState.REGULAR);
        try {
            URI uri = new URI(link);
            if (uriValidator.isValidUri(uri)) {
                user.getLinks().add(uri);
                return SUCCESS_TRACK;
            }
            log.error(TRACK_LOG_ERROR.formatted(link, user.getId()));
            return FAIL;
        } catch (URISyntaxException e) {
            log.error(LINK_PARSE_LOG_ERROR.formatted(link, user.getId()));
            return FAIL;
        }
    }

    public synchronized String untrackLink(User user, String link) {
        if (!user.getUserState().equals(UserState.AWAITING_UNTRACK_LINK)) {
            log.error(user.getId() + " " + MULTIPLE_ATTEMPT_ERROR);
            return MULTIPLE_ATTEMPT_ERROR;
        }
        user.setUserState(UserState.REGULAR);
        try {
            URI uri = new URI(link);
            if (user.getLinks().contains(uri)) {
                user.getLinks().remove(uri);
                return SUCCESS_UNTRACK;
            }
            log.error(UNTRACK_LOG_ERROR.formatted(link, user.getId()));
            return FAIL;
        } catch (URISyntaxException e) {
            log.error(LINK_PARSE_LOG_ERROR.formatted(link, user.getId()));
            return FAIL;
        }
    }
}
