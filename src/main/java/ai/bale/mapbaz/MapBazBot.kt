package ai.bale.mapbaz

import ai.bale.mapbaz.Constants.*
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

import java.util.*

import ai.bale.mapbaz.db.ConfigRepository
import ai.bale.mapbaz.db.RouteRepository
import ai.bale.mapbaz.db.UserRepository
import ai.bale.mapbaz.neshan.NeshanHandler
import ai.bale.mapbaz.records.Route
import ai.bale.mapbaz.records.User
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.Arrays




class MapBazBot(options: DefaultBotOptions) : TelegramLongPollingBot(options) {

    private var mainMenu = Arrays.asList(Constants.BUTTON_MAIN_ADD_ROUTE,
            Constants.BUTTON_MAIN_SHOW_LIVE_TERRAFIC, BUTTON_MAIN_GOLD_PANEL)
    var returnMenu = Arrays.asList(Constants.BUTTON_RETURN_TO_MAIN_MENU);
    var cancelMenu = Arrays.asList(Constants.BUTTON_CANCEL);


    private var conversations: HashMap<Long, Constants.ConversationState> = HashMap()
    private val neshanHandler = NeshanHandler()
    private val userRepository = UserRepository()
    private val configRepository = ConfigRepository()
    private val routeRepository = RouteRepository()
    private val sdf = SimpleDateFormat("HH:mm")
    private val currentUsersRoute =  HashMap<Long, Route>()
    private val scheduler : ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    init {
        scheduler.scheduleAtFixedRate(Runnable {

            val date = Date()
            val time = sdf.format(date)
            val routes = routeRepository.getRoutesByTime(time)

            for (route in routes) {
                showRouteMessage(route.userId, route.name!!)
            }

        }, 0, 1, TimeUnit.MINUTES)
    }

    override fun onUpdateReceived(update: Update) {
        //parseMessage(update);
    }

    private fun parseMessage(update: Update) {
        println("> pares update$update")

        if (update.hasMessage()) {
            val message = update.message
            val chatId = message.chatId
            val convState = conversations[chatId]

            if (convState == null) {
                // show main start
                val user = User(message.chatId, 0, message.from.firstName)
                try {
                    userRepository.create(user)
                } catch (e: Exception) {
                }
                sendMessageWithMarkUp(chatId!!, Constants.TEXT_START, mainMenu)
                conversations[chatId] = Constants.ConversationState.START
            }

            if(message.hasText() && message.text == Constants.START) {
                 showMainMenu(chatId)
            } else if (convState == Constants.ConversationState.START) {
                // check command that enetred
                if (message.hasText()) {
                    if (message.text == Constants.BUTTON_MAIN_ADD_ROUTE) {
                        showAddEditRout(chatId)
                    } else if (message.text == Constants.BUTTON_MAIN_SHOW_LIVE_TERRAFIC) {
                        if(routeRepository.getRoutesByUser(chatId).isEmpty()) {
                            sendMessageWithMarkUp(chatId, Constants.TEXT_SHOW_LIVE_TERAFFIC_WITH_NONE_ROUTE, Arrays.asList(Constants.BUTTON_ADD_ROUT_ADD))
                            conversations[chatId] = Constants.ConversationState.ADDEDIT_ROUTE_STEP1
                        } else {
                            showLiveTraffic(chatId)
                        }
                    } else if (message.text == Constants.BUTTON_MAIN_ADD_SCHAULE_TIME) {
                        showSchedule(chatId)
                    } else if (message.text == Constants.BUTTON_MAIN_GOLD_PANEL) {
                        showGoldPanel(chatId)
                    } else {
                        showMainMenu(chatId)
                    }
                }
            } else if (convState == Constants.ConversationState.ADDEDIT_ROUTE_STEP1) {
                if (message.hasText()) {
                    if (message.text == Constants.BUTTON_ADD_ROUT_ADD) {
                        showSetOrigin(chatId)
                    } else {
                        showMainMenu(chatId)
                    }
                }
            } else if (convState == Constants.ConversationState.ADD_ROUTE_STEP_SET_ORIGIN) {
                if (message.hasLocation()) {
                    println("> origin: " + message.location.toString())

                    var route = Route()
                    route.origin = "${message.location.latitude},${message.location.longitude}"
                    route.userId = message.chatId
                    currentUsersRoute[message.chatId] = route

                    showSetTime(chatId)
                } else {
                    sendMessage(chatId, TEXT_INVALID_MESSAGE)
                }
            } else if (convState == Constants.ConversationState.ADD_ROUTE_STEP_SET_TIME) {
                if (message.hasText()) {
                    println("> time: " + message.text)
                    var route = currentUsersRoute[message.chatId]
                    route!!.time = message.text
                    currentUsersRoute[message.chatId] = route

                    showSetDest(chatId)
                }
            } else if (convState == Constants.ConversationState.ADD_ROUTE_STEP_SET_DEST) {
                if (message.hasLocation()) {
                    println("> dest: " + message.location.toString())

                    var route = currentUsersRoute[message.chatId]
                    route!!.destination = "${message.location.latitude},${message.location.longitude}"
                    currentUsersRoute[message.chatId] = route

                    showSetName(chatId)
                } else {
                    sendMessage(chatId, TEXT_INVALID_MESSAGE)
                }
            } else if (convState == Constants.ConversationState.ADD_ROUTE_STEP_SET_NAME) {
                if (message.hasText()) {
                    println("> name: " + message.text)

                    var route = currentUsersRoute[message.chatId]
                    route!!.name = message.text
                    currentUsersRoute[message.chatId] = route
                    routeRepository.create(currentUsersRoute[message.chatId]!!)
                    currentUsersRoute.remove(message.chatId)

                    showSampleMessage(chatId, message.text)
                }
            } else if (convState == Constants.ConversationState.ADD_ROUTE_STEP_FINAL) {
                if (message.hasText()) {
                    if (message.text == Constants.BUTTON_ADD_ROUT_ADD) {
                        showSetOrigin(chatId)
                    } else {
                        showMainMenu(chatId)
                    }
                }
            } else if (convState == Constants.ConversationState.SHOW_LIVE_TERRAFIC) {
                if (message.hasText()) {
                    // check route and request to neshan then show message
                    println("> route: " + message.text)
                    showRouteMessage(chatId, message.text)
                }
            } else if (convState == Constants.ConversationState.SHOWED_GOLD_PANEL) {
                if (message.hasText()) {
                    // check route and request to neshan then show message
                    println("> money request: " + message.text)
                    showMoneyRequestMessage(chatId, message.text)
                }
            } else if (convState == Constants.ConversationState.SHOW_MONEY_REQUEST) {
                if (message.hasSuccessfulPayment()) {
                    System.out.println("> success " + message.getSuccessfulPayment().getOrderInfo());
                    //TODO add one month to user date
                    showSuccessfullPaymentResponse(chatId);
                } else {
                    showFailedResponse(chatId);
                }
            } else {
                showMainMenu(chatId)
            }

        }
    }

    private fun showSuccessfullPaymentResponse(chatId: Long?) {
        sendMessageWithMarkUp(chatId!!, "پرداخت با موفقیت انجام شد.", returnMenu)
        conversations[chatId] = Constants.ConversationState.START
    }

    private fun showFailedResponse(chatId: Long?) {
        sendMessageWithMarkUp(chatId!!, "پرداخت انجام نشد، لطفا مجددا تست نمایید.", returnMenu)
        conversations[chatId] = Constants.ConversationState.START
    }

    private fun showMoneyRequestMessage(chatId: Long?, text: String) {
        sendMoneyRequest(chatId!!, text)
        conversations[chatId] = Constants.ConversationState.SHOW_MONEY_REQUEST
    }

    private fun showRouteMessage(chatId: Long?, routeName: String) {
        val route = routeRepository.getRouteByName(routeName)
        val neshanRoutes = neshanHandler.getRoutes(route!!.origin!!, route.destination!!, true, true)
        val distance = neshanRoutes.routes[0].legs[0].distance
        val duration = neshanRoutes.routes[0].legs[0].duration
        val summary = neshanRoutes.routes[0].legs[0].summary

        val links = "\n[بازکردن مسیر در گوگل‌مپ]" +
                "(https://www.google.com/maps/dir/${route.origin}/${route.destination})" +
                "\n[بازکردن مسیر در ویز]" +
                "(https://www.waze.com/livemap?ll=${route.destination}&from=${route.origin}&at=now)"
        sendMessageWithMarkUp(chatId!!, Constants.TEXT_FOUND_ROUTE.replace("{1}", summary).replace("{2}", duration.text) + "\n${links}", Arrays.asList(Constants.BUTTON_RETURN_TO_MAIN_MENU))
        conversations[chatId] = Constants.ConversationState.SHOWED_ALL_ROUTES
    }

    private fun showSampleMessage(chatId: Long?, routeName: String) {
        sendMessageWithMarkUp(chatId!!, Constants.TEXT_ADD_ROUT_STEP5.replace("{1}", routeName), Arrays.asList(Constants.BUTTON_ADD_ROUT_ADD, Constants.BUTTON_RETURN_TO_MAIN_MENU))
        conversations[chatId] = Constants.ConversationState.ADD_ROUTE_STEP_FINAL
    }

    private fun showSetName(chatId: Long?) {
        sendMessage(chatId!!, Constants.TEXT_ADD_ROUT_STEP4)
        conversations[chatId] = Constants.ConversationState.ADD_ROUTE_STEP_SET_NAME
    }

    private fun showSetOrigin(chatId: Long?) {
        sendMessage(chatId!!, Constants.TEXT_ADD_ROUT_STEP1)
        conversations[chatId] = Constants.ConversationState.ADD_ROUTE_STEP_SET_ORIGIN
    }

    private fun showSetDest(chatId: Long?) {
        sendMessage(chatId!!, Constants.TEXT_ADD_ROUT_STEP3)
        conversations[chatId] = Constants.ConversationState.ADD_ROUTE_STEP_SET_DEST
    }

    private fun showSetTime(chatId: Long?) {
        sendMessage(chatId!!, Constants.TEXT_ADD_ROUT_STEP2)
        conversations[chatId] = Constants.ConversationState.ADD_ROUTE_STEP_SET_TIME
    }

    private fun showSchedule(chatId: Long?) {

    }

    private fun showGoldPanel(chatId: Long?) {
        val fakeRoutesList = Arrays.asList(BUTTIN_BUY_GOLDEN_PANEL, Constants.BUTTON_RETURN_TO_MAIN_MENU)
        sendMessageWithMarkUp(chatId!!, Constants.TEXT_GOLD_PANEL, fakeRoutesList)
        conversations[chatId] = Constants.ConversationState.SHOWED_GOLD_PANEL
    }

    private fun showLiveTraffic(chatId: Long?) {
        val routesList = routeRepository.getRoutesByUser(chatId!!)
        sendMessageWithMarkUp(chatId!!, Constants.TEXT_LIVE_TRAFFIC, routesList.mapNotNull { it.name })
        conversations[chatId] = Constants.ConversationState.SHOW_LIVE_TERRAFIC
    }

    private fun showAddEditRout(chatId: Long?) {
        // Todo check if has routes show routes
        sendMessageWithMarkUp(chatId!!, Constants.TEXT_HASNOT_ROUTE_, Arrays.asList(Constants.BUTTON_ADD_ROUT_ADD))
        conversations[chatId] = Constants.ConversationState.ADDEDIT_ROUTE_STEP1
    }

    private fun showMainMenu(chatId: Long?) {
        sendMessageWithMarkUp(chatId!!, Constants.TEXT_MAIN_MENU, mainMenu)
        conversations[chatId] = Constants.ConversationState.START
    }


    private fun getReplyKeyboardMarkUp(strings: List<String>): ReplyKeyboardMarkup {
        val replyKeyboardMarkup = ReplyKeyboardMarkup()
        replyKeyboardMarkup.selective = true
        replyKeyboardMarkup.resizeKeyboard = true

        val keyboard = ArrayList<KeyboardRow>()
        val keyboardFirstRow = KeyboardRow()
        keyboardFirstRow.addAll(strings)
        keyboard.add(keyboardFirstRow)
        replyKeyboardMarkup.keyboard = keyboard
        return replyKeyboardMarkup
    }

    internal fun sendMessageWithMarkUp(chat_id: Long, text: String, buttonList: List<String>) {
        val message = SendMessage()
        message.setChatId(chat_id)
        message.text = text
        message.replyMarkup = getReplyKeyboardMarkUp(buttonList)

        try {
            execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }

    }

    internal fun sendMessage(chat_id: Long, text: String) {
        val message = SendMessage()
        message.setChatId(chat_id)
        message.text = text

        try {
            execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }

    }

    internal fun sendMoneyRequest(chat_id: Long, text: String) {
        val sendInvoice = SendInvoice()
        sendInvoice.chatId = chat_id.toInt()
        sendInvoice.title = text

        val labeledPrice = LabeledPrice()
        labeledPrice.amount = 1
        labeledPrice.label = "خرید اکانت طلایی"

        val labeledPriceList = ArrayList<LabeledPrice>()
        labeledPriceList.add(labeledPrice)
        sendInvoice.prices = labeledPriceList
        sendInvoice.description = "با پرداخت این مبلغ شما به مدت یک ماه می توانید به صورت نامحدود از امکانات من استفاده نمایید."
        sendInvoice.payload = "test"
        sendInvoice.providerToken = "6037997231464034"
        sendInvoice.startParameter = "test"
        sendInvoice.currency = "IRR"


        try {
            execute(sendInvoice)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }

    }

    override fun onUpdatesReceived(updates: List<Update>) {
        // todo check last seq
        parseMessage(updates[updates.size - 1])
    }

    override fun getBotUsername(): String {
        return "mapbazbot"
    }

    override fun getBotToken(): String {
        return Constants.TOKEN
    }
}