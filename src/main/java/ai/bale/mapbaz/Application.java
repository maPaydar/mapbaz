package ai.bale.mapbaz;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Application {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            DefaultBotOptions options = new DefaultBotOptions() {
                @Override
                public String getBaseUrl() {
                    return "https://tapi.bale.ai/";
                }
            };
            botsApi.registerBot(new MapBazBot(options));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
