package keni.handyandy.Config;

/**
 * Created by Keni on 06.06.2016.
 */
public class Config
{
    // Ссылки на пхп скрипты
    public static final String LOGIN_URL = "http://woworion.com/handyandy/login.php";
    public static final String URL_GET_ENGINEER = "http://woworion.com/handyandy/getEngineer.php?username=";
    public static final String URL_GET_ALL_APPS = "http://woworion.com/handyandy/getAllApps.php";
    public static final String URL_GET_MY_APPS = "http://woworion.com/handyandy/getMyApps.php?engineer=";
    public static final String URL_GET_APP = "http://woworion.com/handyandy/getApp.php?id=";
    public static final String URL_UPDATE_SELECT_APP = "http://woworion.com/handyandy/updateSelectApp.php";
    public static final String URL_UPDATE_FINISH_APP = "http://woworion.com/handyandy/updateFinishApp.php";

    // Отправляемые переменные
    public static final String KEY_APP_ID = "id";
    public static final String KEY_APP_COMMENT = "comment";
    public static final String KEY_ENGINEER = "engineer";

    // Получаемые переменные
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_APP_ID = "id";
    public static final String TAG_APP_TITLE = "title";
    public static final String TAG_APP_DESCRIPTION = "description";
    public static final String TAG_APP_CATEGORY = "category";
    public static final String TAG_APP_CLIENTNAME = "client_name";
    public static final String TAG_APP_CLIENTADDRESS = "client_address";
    public static final String TAG_APP_CLIENTPHONE = "client_phone";
    public static final String TAG_APP_DATE = "date";
    public static final String TAG_ENGINEER_BALANCE = "balance";
    public static final String TAG_ENGINEER_FULL_NAME = "full_name";

    public static String ENGINEER;
    public static String KEY_ENGINEER_BALANCE;
    public static String KEY_ENGINEER_FULL_NAME;

    public static final String APP_ID = "app_id";



}
