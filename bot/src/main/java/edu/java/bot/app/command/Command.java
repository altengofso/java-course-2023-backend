package edu.java.bot.app.command;

import com.pengrad.telegrambot.request.SendMessage;

public interface Command {
    String command();

    String description();

    SendMessage handle(long id);
}
