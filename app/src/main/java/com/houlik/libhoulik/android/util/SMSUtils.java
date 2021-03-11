package com.houlik.libhoulik.android.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.CursorWindow;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信工具
 * Created by Houlik on 6/2/2017.
 */

public class SMSUtils {

    private final String TAG = "SMSUtils : ";

    private static SMSUtils smsUtils = new SMSUtils();

    private SMSUtils(){}

    public static SMSUtils getInstance(){
        if(smsUtils == null){
            new SMSUtils();
        }
        return smsUtils;
    }

    /**
     * 调起系统发短信功能
     * context 当前activity, phoneNumber 接收者电话号码，message 发送信息
     * @param context
     * @param phoneNumber
     * @param message
     */
    public void smsto(Context context, String phoneNumber, String message){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
        intent.putExtra("SMSUtils", message);
        context.startActivity(intent);
    }

    /**
     * 信息是否发送成功
     * @param activity
     * @return
     */
    public PendingIntent sentSMSSuccess(final Context activity){
        String SENT_SMS_SUCCESS = "SENT SMSUtils SUCCESS";
        Intent intent = new Intent(SENT_SMS_SUCCESS);
        PendingIntent sentIntent = PendingIntent.getBroadcast(activity,0,intent,0);

        activity.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int result = getResultCode();
                switch (result){
                    case Activity.RESULT_OK :
                        Log.i(TAG, "发送成功");
                        break;
                    default:
                        Log.i(TAG,"发送失败");
                }

            }
        }, new IntentFilter(SENT_SMS_SUCCESS));

        return sentIntent;
    }

    /**
     * 信息是否接收成功
     * @param activity
     * @return
     */
    public PendingIntent receiveSMSSuccess(final Context activity){
        String RECEIVE_SMS_SUCCESS = "RECEIVE SMSUtils SUCCESS";
        Intent intent = new Intent(RECEIVE_SMS_SUCCESS);
        PendingIntent receiveIntent = PendingIntent.getBroadcast(activity,0,intent,0);

        activity.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG,"接收成功");
            }
        }, new IntentFilter(RECEIVE_SMS_SUCCESS));
        return receiveIntent;
    }

    /**
     * 直接发送群发信息
     * @param receivePhoneNumber
     * @param message
     * @param context
     */
    public void directSMS(String[] receivePhoneNumber,String[] message,Context context){
        for (int i = 0; i < receivePhoneNumber.length; i++) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(receivePhoneNumber[i], null, message[i], sentSMSSuccess(context), receiveSMSSuccess(context));
        }
    }

    /**
     * 直接发送信息
     * @param receivePhoneNumber 接收者手机号码,前缀必须加上 "+" 加 "国家号码"
     * @param sendPhoneNumber 发送者手机号码, 可以填 null
     * @param message 短信内容
     */
    public void directSingleSMS(String receivePhoneNumber, String sendPhoneNumber, String message){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(receivePhoneNumber,sendPhoneNumber,message,null, null);
    }

    /**
     * String number = cur.getString(cur.getColumnIndex("想获得的属性"))
     * sms主要结构：
     * _id：短信序号，如100
     * thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的
     * address：发件人地址，即手机号，如+8613811810000
     * person：发件人，如果发件人在通讯录中则为具体姓名，陌生人为null
     * date：日期，long型，如1256539465022，可以对日期显示格式进行设置
     * protocol：协议0SMS_RPOTO短信，1MMS_PROTO彩信
     * read：是否阅读0未读，-1已读
     * status：短信状态-1接收，0complete,64pending,128failed
     * type：短信类型1是接收到的，2是已发出
     * body：短信具体内容
     * service_center：短信服务中心号码编号，如+8613800755500
     */
    private Uri SMS_INBOX = Uri.parse("content://sms/");

    /**
     * 得到手机内所有的短信内容
     * @param context
     */
    public void getSmsFromPhone(Context context) {
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] {"_id", "address", "person","body", "date", "type" };
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            CursorWindow cursorWindow = new CursorWindow("read");
        }
        if (null == cur) {
            Log.i("Cursor","null");
            return;
        }
        while(cur.moveToNext()) {
            String phone = cur.getString(cur.getColumnIndex("address"));//手机号
            int number = cur.getColumnIndex("read");
            System.out.println("PHONE:"+phone + " NUMBER:"+number);
            if(number == 0) {
                String phoneNum = cur.getString(cur.getColumnIndex("address"));//手机号
                String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
                String body = cur.getString(cur.getColumnIndex("body"));//短信内容

                //至此就获得了短信的相关的内容, 以下是把短信加入map中，构建listview,非必要。
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("phoneNum", phoneNum);
                map.put("message", body);
//            list.add(map);
                System.out.println(map);
            }

        }
    }
}
