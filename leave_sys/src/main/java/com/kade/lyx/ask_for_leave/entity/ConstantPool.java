package com.kade.lyx.ask_for_leave.entity;

/**
 * Created by lyx on 2016/10/14 0014.
 */

public class ConstantPool {

    public static final String INIT_URL = "http://192.168.0.254:8070/Dispatch.aspx";//初始化服务器地址
    public static final String UPDATA_URL = "http://eccard.eicp.net:8091/Dispatch.aspx";//软件更新服务器地址
//    public static final String URL = "http://eccard.eicp.net:8091/Dispatch.aspx";  //外网端口
//  public static final String URL = "eccard.cdcardzn.com:8086/Dispatch.aspx";  //可能的备用地址

    public static final String RESULT_OK = String.valueOf(0);
    public static final String PARAM_NAME = "param";
    public static final String SP_ID = "stu_id";
    public static final String LOGIN_ID = "login_id";
    public static final String DEVICE_NO = "device_no";//设备自编号
    public static final String SP_ID_INFO = "uid";
    public static final String SP_NAME = "user_name";
    public static final String SP_PASS = "user_pass";
    public static final String UID = "id";
    public static final String RESULT_FAILED = "failed";

    //**************************param配置*******************//
    public static final String APP_NAME = "ask_for_leave";//APP名字标识 用于APP检查更新
    public static final String COMMUNICATION_SECRET_KEY = "7c3303b3d7910da7";//通讯秘钥
    public static final String APP_ID = "5100000003";//appid
    public static final String INIT_APP_ID = "5100000003";//appid
    public static final String INIT_COMMUNICATION_SECRET_KEY = "7c3303b3d7910da7";//通讯秘钥
    public static final int RANDOM_NUMBER_SUM = 2;//传参随机数个数
}
