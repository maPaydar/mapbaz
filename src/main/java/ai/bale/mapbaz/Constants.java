package ai.bale.mapbaz;

public class Constants {

    public static final String TOKEN = "1128527567:73c759f77a58f0293baae4b80858d2aa705a7eea";
    public static final String NESHAN_URL = "https://api.neshan.org/v2/direction";
    public static final String NESHAN_API_KEY = "service.ZFBPIPhHo6YFoVWwkVM5xjEgBZ3cKTTJzvyhkPZJ";
    public static final String START = "/start";
    public static final String ADD = "/addroute";
    public static final String EDIT_SCHEDULE_TIME = "/edit_schedule_time";
    public static final String SHOW_CURRENT_TRAFFIC = "/show_current_traffic";

    // messages template text

    public static final String TEXT_START = "سلام\n" +
            "من بازوی مپ\u200Cباز شما هستم و به شما کمک میکنم بهترین و کوتاه\u200Cترین مسیر با کمترین تلاش پیدا کنید. \n" +
            "برای شروع کافی است که مسیرهای پرتکراری که روزانه طی می کنید در مپ\u200Cباز تعریف کنید \n";

    public static final String TEXT_ADD_ROUTE = "";



    public static final String TEXT_HASNOT_ROUTE_ = "برای استفاده از امکانات من لازم است که حتما مسیری را تعریف کنید.\n" +
            "برای این کار کافی است که از لیست زیر دکمه اضافه کردن مسیر جدید را انتخاب کنید.\n";

    public static final String TEXT_HAS_ROUTE = "مسیرهایی که تا امروز تعریف کردید رو می تونید در لیست زیر ببینید.\n" +
            "برای افزودن یک مسیر جدید از دکمه اضافه کردن مسیر جدید استفاده کنید.\n" +
            "همچنین می تونید با انتخاب هر کدوم از مسیرهای قبلی از لیست زیر اونها رو ویرایش کنید.\n";


    public static final String TEXT_ADD_ROUT_STEP1 = "مبدا مسیرت را برام بفرست.\n" +
            "\tبرای فرستادن مبدا دکمه + را بزنید و موقعیت مدنظرت را برام بفرست.\n";
    public static final String TEXT_ADD_ROUT_STEP2 = "معمولا کی از اینجا حرکت میکنی؟ \n" +
            "مثال: ۱۳:۱۵\n";
    public static final String TEXT_ADD_ROUT_STEP3 = "مقصد مسیرت برام بفرست.\n";
    public static final String TEXT_ADD_ROUT_STEP4 = "اسم مسیرت را چی بزارم؟ \n" +
            "مثلا میتونی «خونه به شرکت» بزاری.\n";
    public static final String TEXT_ADD_ROUT_STEP5 = "من چند دقیقه قبل از حرکت از (اسم مسیر) بهت پیام میدم تا کمکت کنم که کوتاه\u200Cترین مسیرها را برات پیدا کنم.\n" +
            "نمونه پیامی که برات ارسال میکنم مثل عکس بالاست :)\n";

    public static final String TEXT_ADD_SCHAULE_TIME = "\n" +
            "من به شما کمک خواهم کرد که علاوه بر ارسال پیام بهترین مسیر یافت شده، با بررسی شرایطی ترافیکی در زمان تعیین شده در اطراف مبدا شما در صورت وقوع ترافیک سنگین حوالی مبدا شما، شما را از ترافیک این مسیرها مطلع و زودتر به شما اطلاع رسانی کنم.\n" +
            "برای این کار فقط کافی است که مسیر مورد نظر خود را از لیست زیر انتخاب کنید و تعیین کنید که در صورت وجود ترافیک تا چه محدوده زمانی ای از زمان تعیین شده در هر مسیر به شما اطلاع رسانی صورت گیرد.\n" +
            "به عنوان مثال در صورتی که در ساعت ۸:۰۰ در مسیر از منزل به محل کار شما اطلاع رسانی صورت می گیرد با تنظیم این این هشدار در بازه (نهایتا یک ساعت مانده به زمان تعیین شده) بررسی ها صورت گرفته و در صورت مشاهده ترافیک غیر عادی شما مطلع خواهید شد.\n";

    public static final String TEXT_LIVE_TRAFFIC = "برای مشاهده\u200Cی ترافیک الان هر کدوم از مسیرهات کافیه اون رو از لیست زیر انتخاب کنید. تا من کوتاه ترین مسیرها را برات پیدا کنم.\n";

    // BUTTONS
    public static final String BUTTON_MAIN_ADD_ROUTE = "اضافه و ویرایش مسیرها";
    public static final String BUTTON_MAIN_ADD_SCHAULE_TIME = "هشدار هوشمند ترافیک مسیرها";
    public static final String BUTTON_MAIN_SHOW_LIVE_TERRAFIC = "مشاهده ترافیک لحظه ای مسیرها";
    public static final String BUTTON_RETURN_TO_MAIN_MENU = "بازگشت به منوی اصلی";
    public static final String BUTTON_ADD_ROUT_ADD = "اضافه کردن مسیر جدید";

}
