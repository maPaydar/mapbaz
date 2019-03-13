package ai.bale.mapbaz;

import org.apache.shiro.session.Session;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

import java.io.Serializable;
import java.util.*;

public class MapBazBot extends TelegramLongPollingSessionBot {

    List<String> mainMenu = Arrays.asList(Constants.BUTTON_MAIN_ADD_ROUTE, Constants.BUTTON_MAIN_ADD_SCHAULE_TIME,
            Constants.BUTTON_MAIN_SHOW_LIVE_TERRAFIC);
    List<String> routMenu = Arrays.asList(Constants.BUTTON_ADD_ROUT_ADD);

    Long seqNumber = 31L;

    public MapBazBot(DefaultBotOptions options) {
        super(options);
    }

/*
    public void onUpdateReceived(Update update) {
        //parseMessage(update);
    }
*/

    @Override
    public void onUpdateReceived(Update update, Optional<Session> session) {
        parseMessage(update, session);
        System.out.println("onUpdateRecived");
    }

    private void parseMessage(Update update, Optional<Session> session) {

        if (update.hasMessage()) {

            Message message = update.getMessage();
            Long chatId = message.getChatId();

            if (message.hasText()) {
                String textMessage = message.getText();
                if (textMessage.equals(Constants.START)) {
                    System.out.println("/start" + update);
                    sendMessageWithMarkUp(chatId, Constants.TEXT_START, mainMenu);
                    session.get().setAttribute(session.get().getId(), Constants.START);
                } else if (textMessage.equals(Constants.BUTTON_MAIN_ADD_ROUTE)) {
                    session.get().setAttribute(session.get().getId(), Constants.BUTTON_MAIN_ADD_ROUTE);
                    sendMessageWithMarkUp(chatId, Constants.TEXT_HAS_ROUTE, loadUserMenu());
                } else if (textMessage.equals(Constants.BUTTON_MAIN_ADD_SCHAULE_TIME)) {
                    session.get().setAttribute(session.get().getId(), Constants.BUTTON_MAIN_ADD_SCHAULE_TIME);
                    sendMessageWithMarkUp(chatId, Constants.TEXT_ADD_SCHAULE_TIME, loadUserMenu());
                } else if (textMessage.equals(Constants.BUTTON_MAIN_SHOW_LIVE_TERRAFIC)) {
                    session.get().setAttribute(session.get().getId(), Constants.BUTTON_MAIN_SHOW_LIVE_TERRAFIC);
                    sendMessageWithMarkUp(chatId, Constants.TEXT_LIVE_TRAFFIC, loadUserMenu());
                }
            }

        }
        if (update.getMessage().hasLocation()) {

        }
    }

    private List<String> loadUserMenu() {
        List<String> userMenu =  Arrays.asList();
        //userMenu.addAll(routMenu);
        return userMenu;
    }


    private ReplyKeyboardMarkup getReplyKeyboardMarkUp(List<String> strings) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.addAll(strings);
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    void sendMessageWithMarkUp(long chat_id, String text, List<String> buttonList) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(text);

        message.setReplyMarkup(getReplyKeyboardMarkUp(buttonList));

        try {
            execute(message);
            System.out.println("Me: " + text);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(long chat_id, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(text);

        try {
            execute(message);
            System.out.println("Me: " + text);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /*public void onUpdatesReceived(List<Update> updates) {
        System.out.println(updates);
    }
    */

    public String getBotUsername() {
        return "mapbazbot";
    }

    public String getBotToken() {
        return Constants.TOKEN;
    }
}