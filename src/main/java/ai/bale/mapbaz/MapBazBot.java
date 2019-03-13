package ai.bale.mapbaz;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class MapBazBot extends TelegramLongPollingBot {

    public MapBazBot(DefaultBotOptions options) {
        super(options);
    }

    public void onUpdateReceived(Update update) {
        System.out.println("on update recv" + update);
    }

    public void onUpdatesReceived(List<Update> updates) {
        System.out.println("on updates recv" + updates);
    }

    public String getBotUsername() {
        return "mapbazbot";
    }

    public String getBotToken() {
        return Constants.TOKEN;
    }
}