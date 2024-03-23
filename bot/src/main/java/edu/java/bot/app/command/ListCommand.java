package edu.java.bot.app.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.scrapperclient.ScrapperApiClient;
import edu.java.bot.scrapperclient.models.ListLinksResponse;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component("list")
@RequiredArgsConstructor
public class ListCommand implements Command {
    private static final String COMMAND = "/list";
    private static final String DESCRIPTION = "показать список отслеживаемых ссылок";
    private static final String NO_LINKS = "нет отслеживаемых ссылок";

    private final ScrapperApiClient scrapperApiClient;

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    @SneakyThrows
    public SendMessage handle(long id) {
        ListLinksResponse listLinksResponse = scrapperApiClient.getAllLinks(id);
        if (listLinksResponse.links().isEmpty()) {
            return new SendMessage(id, NO_LINKS);
        }
        return new SendMessage(
            id,
            listLinksResponse.links()
                .stream()
                .map(linkResponse -> linkResponse.url().toString())
                .collect(Collectors.joining("\n"))
        );
    }
}
