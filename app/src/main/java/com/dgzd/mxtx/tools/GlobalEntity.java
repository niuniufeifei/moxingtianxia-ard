package com.dgzd.mxtx.tools;


public class GlobalEntity {

    public static int PULL_UP = 1;
    public static int PULL_DOWN = 2;

    public static final int POPWINDOW_RESULT_OK = 1;

    public static final int POPWINDOW_RESULT_CODE = 100;
    public static final int POPWINDOW_TAKE_PHOTO = 101;
    public static final int POPWINDOW_PICK_PHOTO = 102;
    public static final int POPWINDOW_CANCEL = 103;

    public static final int POPWINDOW_PAYMENT_RESULT_CODE = 104;
    public static final int POPWINDOW_PAYMENT_ALIPAY = 105;
    public static final int POPWINDOW_PAYMENT_WECHAT = 106;
    public static final int POPWINDOW_PAYMENT_CANCEL = 107;

    public static final int IDENTY_REQUESET_CODE = 110;
    public static final int IDENTY_FRONT_SIDE = 111;
    public static final int IDENTY_REVERSE_SIDE = 112;

    public static final int FRONT_PREVIEW = 120;
    public static final int REVERSE_PREVIEW = 121;
    public static final int PREVIEW_DELETE = 122;

    public static final int REQUEST_LOGIN = 130;
    public static final int REQUEST_SHOPPING_CART_LOGIN = 131;
    public static final int REQUEST_LOGIN_FAILED = 132;

    public static final int MODIFY_USER_INFO_NICKNAME = 140;
    public static final int MODIFY_USER_INFO_ADDRESS = 142;
    public static final int MODIFY_USER_INFO_CELL_PHONE = 141;
    public static final int MODIFY_USER_INFO_SUCCESS = 143;
    public static final int MODIFY_USER_INFO_FAILED = 144;


    public static final int MODIFY_SHOP_INFO_NAME = 150;

    public static final int GET_USER_ORDER_UNPAID_INDEX = 30;
    public static final int GET_USER_ORDER_UNFILLED_GOODS_INDEX = 70;

    //	public static final String MOTO_TRIP_CONTACT_PERSON = "contactPerson";
//	public static final String MOTO_TRIP_CONTACT_MOBILE = "contactMobile";
//	public static final String MOTO_TRIP_ENTER_FEE = "enterFee";
    public static final String MOTO_TRIP_ACTIVITY_ID = "activityId";
    public static final String MOTO_TRIP_ACTIVITY_DETAIL = "activityDetail";

    public static final String USER_NICK_NAME = "userNickName";
    //  public static String requestPath = "http://192.168.0.115:8081/";
    public static String requestPath = "http://www.51mxtx.com:8081/";
    public static String HomePageRequestUrl = requestPath + "api/Home/Index";
    public static String HomePageSearchUrl = requestPath + "api/Product?pageSize=%d&pageIndex=%d&name=%s";
    public static String getProvinceUrl = requestPath + "api/Address/GetProvince";
    public static String getCityUrl = requestPath + "api/Address/GetCities?provinceId=%d";
    public static String getAreaUrl = requestPath + "api/Address/GetAreas?cityId=%d";

    public static String commodityListInfoUrl = requestPath + "api/Product/Index?pageSize=10&pageIndex=%d&name=%s";
    public static String commodityListInfo2Url = requestPath + "api/Product/Index?pageSize=10&pageIndex=%d&name=%s&categoryid=%d";
    public static String commodityDetailUrl = requestPath + "api/Product/GetProductById?pId=%d";
    public static String commodityCommentUrl = requestPath + "api/Product/GetCommentList?pId=%d";


    public static String VideoListInfoUrl = requestPath + "api/Video/SearchVideo?keyword=%s&sortColumn=0&sortDirection=%d&pageIndex=%d";
    public static String ActivityListInfoUrl = requestPath + "api/Activity/SearchActivity?keyword=%s&pageIndex=%d";
    public static String GetMyActivityListInfoUrl = requestPath + "api/Activity/GetMyActivity?userid=%d&page=%d";
    public static String ActivityRegisterInfoUrl = requestPath + "api/Activity/GetActivity?activityid=%d&page=%d&userid=%d";
    public static String ActivityRegisterUrl = requestPath + "api/Activity/ActivityRegister";
    public static String ActivityDetailUrl = requestPath + "api/Activity/GetActivityProtocol";
    public static String ActivityProtocolUrl = requestPath + "api/Activity/GetActivityProtocol";


    public static String UserGetMyOrderUrl = requestPath + "api/Order/GetMyOrder?userid=%d&page=%d&orderState=%d";
    public static String WeixinPayUrl = requestPath + "api/Payment/WeiXinPay";

    public static String LoginUrl = requestPath + "api/User/Login?accountName=%s&password=%s";

    public static String EventRefundUrl = requestPath + "";//TODO
    public static String OrderRefundUrl = requestPath + "api/Order/ReturnOrderProduct";
    public static String EventCommitUrl = requestPath + "api/Activity/AddActivityComment";
    public static String EventRegisterUrl = requestPath + "api/Activity/ActivityRegister";

    public static String VedioUrl = requestPath + "api/Video/GetMyVideo?userid=%d&pageIndex=%d";
    public static String AddVedioCommentUrl = requestPath + "api/Video/AddVideoComment";
    public static String GetVedioDetailUrl = requestPath + "api/Video/GetVideo?vid=%d&page=%d";
    public static String ReplyVideoCommentUrl = requestPath + "api/Video/ReplyComment";

    public static String VedioDeleteUrl = requestPath + "api/Video/DeleteVideo";

    public static String MyEventUrl = requestPath + "api/Activity/GetMyActivity?userid=%d&page=%d";
    /**
     * 获取TOKEN----URL
     */
    public static String GET_TOKEN_APIS = requestPath + "api/RongCloud/GetToken?";
    /**
     * 获取好友列表
     */
    public static String GET_FRIENDSLISTS_APIS = requestPath + "api/RongCloud/FindFriends?";
    /**
     * 添加好友
     */
    public static String GET_ADDFRIEND_APIS = requestPath + "api/RongCloud/RequestContact?";
    /**
     * 获取用户信息
     */
    public static String GET_USERINFO_APIS = requestPath + "api/User/GetUserInfoByMemberId?";
    /**
     * 同意好友请求
     */
    public static String GET_AGREEAPPLY_APIS = requestPath + "api/RongCloud/AcceptContact?";

    public static String QQThridPartRegisterUrl = requestPath + "api/User/ThridPartRegister";

    public static String SenMessageUrl = requestPath + "api/User/SendMessage";
    public static String RegisterUrl = requestPath + "api/User/Register";
    public static String ResetPwdUrl = requestPath + "api/User/ResetPwd";

    public static String UserCancleOrderUrl = requestPath + "api/Order/CancelOrder";
    public static String UsersubmitOrderUrl = requestPath + "api/Order/CompleteOrder";

    public static String ModifyUserInfoUrl = requestPath + "api/User/EditUser";

    public static String GetShoppingCartUrl = requestPath + "api/Cart/Index?userId=%d";
    public static String AddProductToShoppingCartUrl = requestPath + "api/Cart/AddProduct";
    public static String SetShoppingCartCheckedStatusUrl = requestPath + "api/Cart/Cancelorselectcartitem?selectedCartItems={selectedCartItems}&userId=%d";
    public static String ShoppingCartConfirmOrder = requestPath + "api/Order/Index?selectedCartItemLists=%s&userId=%d";
    public static String ModifyStoreNameUrl = requestPath + " api/Shop/EditStore";


}
