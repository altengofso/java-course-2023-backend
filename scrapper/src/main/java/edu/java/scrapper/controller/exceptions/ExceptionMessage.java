package edu.java.scrapper.controller.exceptions;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessage {
    public static final String LINK_CONFLICT_MESSAGE = "Ссылка %s уже добавлена";
    public static final String TGCHAT_CONFLICT_MESSAGE = "Чат с id %d уже зарегистрирован";
    public static final String LINK_NOTFOUND_MESSAGE = "Ссылка не найдена";
    public static final String TGCHAT_NOTFOUND_MESSAGE = "Чат не существует";
}
