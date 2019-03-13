package ai.bale.mapbaz;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapBazBot extends TelegramLongPollingBot {

    List<String> mainMenu = Arrays.asList(Constants.BUTTON_MAIN_ADD_ROUTE,Constants.BUTTON_MAIN_ADD_SCHAULE_TIME,
            Constants.BUTTON_MAIN_SHOW_LIVE_TERRAFIC);

    Long seqNumber = 31L;

    public MapBazBot(DefaultBotOptions options) {
        super(options);
    }

    public void onUpdateReceived(Update update) {
        //parseMessage(update);
    }

    private void parseMessage(Update update) {

        if(update.hasMessage()) {

            Message message = update.getMessage();
            Long chatId = message.getChatId();

            if(message.hasText()) {
                String textMessage = message.getText();
                if(textMessage.equals(Constants.START)) {
                    System.out.println("/start" + update);
                    sendMessageWithMarkUp(chatId, Constants.TEXT_START, mainMenu);
                } else if (textMessage.equals(Constants.ADD)) {
                    //System.out.println("/addroute" + update);
                    //sendMessageWithMarkUp(chatId,);
                }
            }
            if(update.getMessage().hasLocation()) {

            }
        }
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

    void sendMessageWithMarkUp(long chat_id, String text ,List<String> buttonList) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(text);

        message.setReplyMarkup(getReplyKeyboardMarkUp(buttonList));

        try{
            execute(message);
            System.out.println("Me: " + text);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    void sendMessage(long chat_id, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(text);

        try{
            execute(message);
            System.out.println("Me: " + text);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public void onUpdatesReceived(List<Update> updates) {
        for(Update update : updates) {
            if (update.getUpdateId() > seqNumber /*&& update.getMessage().isUserMessage()*/) {
                parseMessage(update);
            }
        }
    }

    public String getBotUsername() {
        return "mapbazbot";
    }

    public String getBotToken() {
        return Constants.TOKEN;
    }
}