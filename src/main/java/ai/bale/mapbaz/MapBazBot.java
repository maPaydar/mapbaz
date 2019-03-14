package ai.bale.mapbaz;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

import static ai.bale.mapbaz.Constants.BUTTIN_BUY_GOLDEN_PANEL;
import static ai.bale.mapbaz.Constants.BUTTON_MAIN_GOLD_PANEL;

public class MapBazBot extends TelegramLongPollingBot {

    List<String> mainMenu = Arrays.asList(Constants.BUTTON_MAIN_ADD_ROUTE,Constants.BUTTON_MAIN_ADD_SCHAULE_TIME,
            Constants.BUTTON_MAIN_SHOW_LIVE_TERRAFIC,BUTTON_MAIN_GOLD_PANEL);

    HashMap<Long, Constants.ConversationState> conversations = new HashMap();


    public MapBazBot(DefaultBotOptions options) {
        super(options);
    }

    public void onUpdateReceived(Update update) {
        //parseMessage(update);
    }

    private void parseMessage(Update update) {
        System.out.println("> pares update" + update);

        if(update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            Constants.ConversationState convState = conversations.get(chatId);

            if(convState == null) {
                // show main start
                showMainMenu(chatId);
            } else if(convState.equals(Constants.ConversationState.START)) {
                // check command that enetred
                if(message.hasText()) {
                    if(message.getText().equals(Constants.BUTTON_MAIN_ADD_ROUTE)) {
                        showAddEditRout(chatId);
                    } else if(message.getText().equals(Constants.BUTTON_MAIN_SHOW_LIVE_TERRAFIC)) {
                        showLiveTraffic(chatId);
                    } else if(message.getText().equals(Constants.BUTTON_MAIN_ADD_SCHAULE_TIME)) {
                        showSchedule(chatId);
                    } else if(message.getText().equals(Constants.BUTTON_MAIN_GOLD_PANEL)){
                        showGoldPanel(chatId);
                    }
                    else {
                        showMainMenu(chatId);
                    }
                }
            } else if(convState.equals(Constants.ConversationState.ADDEDIT_ROUTE_STEP1)) {
                if(message.hasText()) {
                    if(message.getText().equals(Constants.BUTTON_ADD_ROUT_ADD)) {
                        showSetOrigin(chatId);
                    } else {
                        showMainMenu(chatId);
                    }
                }
            } else if(convState.equals(Constants.ConversationState.ADD_ROUTE_STEP_SET_ORIGIN)) {
                if(message.hasLocation()) {
                    System.out.println("> origin: " + message.getLocation().toString());



                    showSetTime(chatId);
                }
            } else if(convState.equals(Constants.ConversationState.ADD_ROUTE_STEP_SET_TIME)) {
                if(message.hasText()) {
                    System.out.println("> time: " +  message.getText());
                    showSetDest(chatId);
                }
            } else if(convState.equals(Constants.ConversationState.ADD_ROUTE_STEP_SET_DEST)) {
                if(message.hasLocation()) {
                    System.out.println("> dest: " + message.getLocation().toString());
                    showSetName(chatId);
                }
            } else if(convState.equals(Constants.ConversationState.ADD_ROUTE_STEP_SET_NAME)) {
                if(message.hasText()) {
                    System.out.println("> name: " + message.getText());
                    showSampleMessage(chatId);
                }
            } else if(convState.equals(Constants.ConversationState.ADD_ROUTE_STEP_FINAL)) {
                if(message.hasText()) {
                    if(message.getText().equals(Constants.BUTTON_ADD_ROUT_ADD)) {
                        showSetOrigin(chatId);
                    } else {
                        showMainMenu(chatId);
                    }
                }
            } else if(convState.equals(Constants.ConversationState.SHOW_LIVE_TERRAFIC)) {
                if(message.hasText()) {
                    // check route and request to neshan then show message
                    System.out.println("> route: " + message.getText());
                    showRouteMessage(chatId, 600L, "خونه");
                }
            } else if(convState.equals(Constants.ConversationState.SHOWED_GOLD_PANEL)) {
                if (message.hasText()) {
                    // check route and request to neshan then show message
                    System.out.println("> money request: " + message.getText());
                    showMoneyRequestMessage(chatId, message.getText());
                }
            } else if(convState.equals(Constants.ConversationState.SHOW_MONEY_REQUEST)) {

            } else {
                showMainMenu(chatId);
            }

        }
    }

    private void showMoneyRequestMessage(Long chatId, String text) {
        sendMoneyRequest(chatId , text);
        conversations.put(chatId, Constants.ConversationState.SHOW_MONEY_REQUEST);
    }

    private void showRouteMessage(Long chatId, Long duration, String dest) {
        // Todo show google map and waze or neshan links
        // todo pares duration to human
        sendMessageWithMarkUp(chatId, Constants.TEXT_FOUND_ROUTE.replace("{1}", duration.toString()).replace("{2}", dest), Arrays.asList(Constants.BUTTON_RETURN_TO_MAIN_MENU));
        conversations.put(chatId, Constants.ConversationState.SHOWED_ALL_ROUTES);
    }

    private void showSampleMessage(Long chatId) {
        sendMessageWithMarkUp(chatId, Constants.TEXT_ADD_ROUT_STEP5, Arrays.asList(Constants.BUTTON_ADD_ROUT_ADD, Constants.BUTTON_RETURN_TO_MAIN_MENU));
        conversations.put(chatId, Constants.ConversationState.ADD_ROUTE_STEP_FINAL);
    }

    private void showSetName(Long chatId) {
        sendMessage(chatId, Constants.TEXT_ADD_ROUT_STEP4);
        conversations.put(chatId, Constants.ConversationState.ADD_ROUTE_STEP_SET_NAME);
    }

    private void showSetOrigin(Long chatId) {
        sendMessage(chatId, Constants.TEXT_ADD_ROUT_STEP1);
        conversations.put(chatId, Constants.ConversationState.ADD_ROUTE_STEP_SET_ORIGIN);
    }

    private void showSetDest(Long chatId) {
        sendMessage(chatId, Constants.TEXT_ADD_ROUT_STEP3);
        conversations.put(chatId, Constants.ConversationState.ADD_ROUTE_STEP_SET_DEST);
    }

    private void showSetTime(Long chatId) {
        sendMessage(chatId, Constants.TEXT_ADD_ROUT_STEP2);
        conversations.put(chatId, Constants.ConversationState.ADD_ROUTE_STEP_SET_TIME);
    }

    private void showSchedule(Long chatId) {

    }

    private void showGoldPanel(Long chatId) {
        List<String> fakeRoutesList = Arrays.asList(BUTTIN_BUY_GOLDEN_PANEL , Constants.BUTTON_RETURN_TO_MAIN_MENU);
        sendMessageWithMarkUp(chatId, Constants.TEXT_GOLD_PANEL, fakeRoutesList);
        conversations.put(chatId, Constants.ConversationState.SHOWED_GOLD_PANEL);
    }

    private void showLiveTraffic(Long chatId) {
        List<String> fakeRoutesList = Arrays.asList("از خونه به شرکت", "از دانشگاه به باشگاه", "از باشگاه به خونه");
        sendMessageWithMarkUp(chatId, Constants.TEXT_LIVE_TRAFFIC, fakeRoutesList);
        conversations.put(chatId, Constants.ConversationState.SHOW_LIVE_TERRAFIC);
    }

    private void showAddEditRout(Long chatId) {
        // Todo check if has routes show routes
        sendMessageWithMarkUp(chatId,Constants.TEXT_HASNOT_ROUTE_, Arrays.asList(Constants.BUTTON_ADD_ROUT_ADD));
        conversations.put(chatId, Constants.ConversationState.ADDEDIT_ROUTE_STEP1);
    }

    private void showMainMenu(Long chatId) {
        sendMessageWithMarkUp(chatId, Constants.TEXT_START, mainMenu);
        conversations.put(chatId, Constants.ConversationState.START);
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
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    void sendMoneyRequest(long chat_id, String text) {
        SendInvoice sendInvoice = new SendInvoice();
        sendInvoice.setChatId((int) chat_id);
        sendInvoice.setTitle(text);

        LabeledPrice labeledPrice = new LabeledPrice();
        labeledPrice.setAmount(1);
        labeledPrice.setLabel("خرید اکانت طلایی");

        List<LabeledPrice> labeledPriceList = new ArrayList<>();
        labeledPriceList.add(labeledPrice);
        sendInvoice.setPrices(labeledPriceList);
        sendInvoice.setDescription("با پرداخت این مبلغ شما به مدت یک ماه می توانید به صورت نامحدود از امکانات من استفاده نمایید.");
        sendInvoice.setPayload("test");
        sendInvoice.setProviderToken("6037997231464034");
        sendInvoice.setStartParameter("test");
        sendInvoice.setCurrency("IRR");


        try {
            execute(sendInvoice);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdatesReceived(List<Update> updates) {
        // todo check last seq
        parseMessage(updates.get(updates.size() - 1));
    }

    public String getBotUsername() {
        return "mapbazbot";
    }

    public String getBotToken() {
        return Constants.TOKEN;
    }
}