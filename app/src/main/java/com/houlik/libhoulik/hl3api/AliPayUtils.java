package com.houlik.libhoulik.hl3api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * AlipayClient alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
 *
 * 配置参数 | 示例值解释 | 获取方式/示例值
 *
 * URL | 支付宝网关（固定）| https://openapi.alipay.com/gateway.do
 *
 * APP_ID | APPID | 即创建应用后生成 获取见 第一步：创建应用并获取 APPID
 *
 * APP_PRIVATE_KEY | 开发者应用私钥，由开发者自己生成 | 获取见 第二步：配置应用 > 配置密钥
 *
 * FORMAT | 参数返回格式，只支持 json | json（固定）
 *
 * CHARSET | 请求和签名使用的字符编码格式，支持 GBK 和 UTF-8 | 开发者根据实际工程编码配置
 *
 * ALIPAY_PUBLIC_KEY | 支付宝公钥，由支付宝生成 | 获取详见 第二步：配置应用 > 配置密钥
 *
 * SIGN_TYPE | 商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2 | RSA2
 *
 * 1.交易查询接口 alipay.trade.query：
 *
 * AlipayClient   alipayClient   =   new   DefaultAlipayClient ( "https://openapi.alipay.com/gateway.do" ,  APP_ID ,  APP_PRIVATE_KEY ,  "json" ,  CHARSET ,  ALIPAY_PUBLIC_KEY ,  "RSA2" );  //获得初始化的AlipayClient
 *  AlipayTradeQueryRequest   request   =   new   AlipayTradeQueryRequest (); //创建API对应的request类
 *  request . setBizContent ( "{"   +
 *  "   \"out_trade_no\":\"20150320010101001\","   +
 *  "   \"trade_no\":\"2014112611001004680073956707\""   +
 *  "  }" ); //设置业务参数
 *  AlipayTradeQueryResponse   response   =   alipayClient . execute ( request ); //通过alipayClient调用API，获得对应的response类
 *  System . out . print ( response . getBody ());
 *  //根据response中的结果继续业务逻辑处理
 *
 *  参数名称 | 参数说明
 *
 * out_trade_no | 支付时传入的商户订单号，与 trade_no 必填一个
 *
 * trade_no | 支付时返回的支付宝交易号，与 out_trade_no 必填一个
 *
 * 关键出参：
 *
 * 参数名称 \ 参数说明
 *
 * trade_no | 支付宝28位交易号
 *
 * out_trade_no | 支付时传入的商户订单号
 *
 * trade_status | 交易当前状态
 *
 * 2.交易退款接口 alipay.trade.refund： 商户由于业务原因如金额错误、用户退货、对账不平等情况可能需要退款，退款的途径按照支付途径原路返回。支付渠道为花呗、余额等退款即时到账。银行卡的退款时间以银行退款时间为准，一般情况下2小时内可到账。可在商户门户(b.alipay.com)中退款；也可使用交易成功的商户订单号或支付宝交易号进行退款 ,
 *
 * AlipayClient   alipayClient   =   new   DefaultAlipayClient ( "https://openapi.alipay.com/gateway.do" ,  APP_ID ,  APP_PRIVATE_KEY ,  "json" ,  CHARSET ,  ALIPAY_PUBLIC_KEY ,  "RSA2" );  //获得初始化的AlipayClient
 *  AlipayTradeRefundRequest   request   =   new   AlipayTradeRefundRequest (); //创建API对应的request类
 *  request . setBizContent ( "{"   +
 *  "    \"out_trade_no\":\"20150320010101001\","   +
 *  "    \"trade_no\":\"2014112611001004680073956707\","   +
 *  "    \"out_request_no\":\"1000001\","   +
 *  "    \"refund_amount\":\"1\""   +
 *  "  }" ); //设置业务参数
 *  AlipayTradeRefundResponse   response   =   alipayClient . execute ( request ); //通过alipayClient调用API，获得对应的response类
 *  System . out . print ( response . getBody ());
 *  // 根据response中的结果继续业务逻辑处理
 *
 *  参数名称 | 参数说明
 *
 * out_trade_no | 支付时传入的商户订单号，与 trade_no 必填一个
 *
 * trade_no | 支付时返回的支付宝交易号，与 out_trade_no 必填一个
 *
 * out_request_no | 本次退款请求流水号，部分退款时必传
 *
 * refund_amount | 本次退款金额
 *
 * 关键出参：
 *
 * 参数名称 | 参数说明
 *
 * refund_fee | 该笔交易已退款的总金额
 *
 * 3.查询对账单下载地址接口alipay.data.dataservice.bill.downloadurl.query： 为了保障交易的正确性，支付宝提供了交易账单数据提供给商户对账，对账说明。
 *
 * AlipayClient   alipayClient   =   new   DefaultAlipayClient ( "https://openapi.alipay.com/gateway.do" ,  APP_ID ,  APP_PRIVATE_KEY ,  "json" ,  CHARSET ,  ALIPAY_PUBLIC_KEY ,  "RSA2" );  //获得初始化的AlipayClient
 *  AlipayDataDataserviceBillDownloadurlQueryRequest   request   =   new   AlipayDataDataserviceBillDownloadurlQueryRequest (); //创建API对应的request类
 *  request . setBizContent ( "{"   +
 *  "    \"bill_type\":\"trade\","   +
 *  "    \"bill_date\":\"2016-04-05\""   +
 *  "  }" ); //设置业务参数
 *  AlipayDataDataserviceBillDownloadurlQueryResponse   response   =   alipayClient . execute ( request );
 *  System . out . print ( response . getBody ());
 *  //根据response中的结果继续业务逻辑处理
 *
 *  参数名称 | 参数说明
 *
 * bill_type | 固定传入 trade
 *
 * bill_date | 需要下载的账单日期，最晚是当期日期的前一天
 *
 * 关键出参：
 *
 * 参数名称 | 参数说明
 *
 * bill_download_url | 账单文件下载地址，30秒有效
 *
 *
 * 下载账单文件：
 *
 * //将接口返回的对账单下载地址传入urlStr
 *  String   urlStr   =   "http://dwbillcenter.alipay.com/downloadBillFile.resource?bizType=X&userId=X&fileType=X&bizDates=X&downloadFileName=X&fileId=X" ;
 *  //指定希望保存的文件路径
 *  String   filePath   =   "/Users/fund_bill_20160405.csv" ;
 *  URL   url   =   null ;
 *  HttpURLConnection   httpUrlConnection   =   null ;
 *  InputStream   fis   =   null ;
 *  FileOutputStream   fos   =   null ;
 *  try  {
 *       url   =   new   URL ( urlStr );
 *       httpUrlConnection   =  ( HttpURLConnection )  url . openConnection ();
 *       httpUrlConnection . setConnectTimeout ( 5   *   1000 );
 *       httpUrlConnection . setDoInput ( true );
 *       httpUrlConnection . setDoOutput ( true );
 *       httpUrlConnection . setUseCaches ( false );
 *       httpUrlConnection . setRequestMethod ( "GET" );
 *       httpUrlConnection . setRequestProperty ( "Charsert" ,  "UTF-8" );
 *       httpUrlConnection . connect ();
 *       fis   =   httpUrlConnection . getInputStream ();
 *       byte []  temp   =   new   byte [ 1024 ];
 *       int   b ;
 *       fos   =   new   FileOutputStream ( new   File ( filePath ));
 *       while  (( b   =   fis . read ( temp ))  !=   - 1 ) {
 *           fos . write ( temp ,  0 ,  b );
 *           fos . flush ();
 *      }
 *  }  catch  ( MalformedURLException   e ) {
 *       e . printStackTrace ();
 *  }  catch  ( IOException   e ) {
 *       e . printStackTrace ();
 *  }  finally  {
 *       try  {
 *           fis . close ();
 *           fos . close ();
 *           httpUrlConnection . disconnect ();
 *      }  catch  ( NullPointerException   e ) {
 *           e . printStackTrace ();
 *      }  catch  ( IOException   e ) {
 *           e . printStackTrace ();
 *      }
 *  }
 *
 *
 * 在应用gradle中添加如下
 * allprojects {
 *     repositories {
 *
 *         // 添加下面的内容
 *         flatDir {
 *             dirs 'libs'
 *         }
 *
 *         // ... jcenter() 等其他仓库
 *     }
 * }
 */
public class AliPayUtils {

    private final int SDK_PAY_FLAG = 1;
    private Activity activity;
    private OnPayAction onPayAction;

    public AliPayUtils(Activity activity){
        this.activity = activity;
    }


    /**
     * 支付宝回调
     * boolean isShowPayLoading
     * 用户在商户app内部点击付款，是否需要一个 loading 做为在钱包唤起之前的过渡，这个值设置为 true，
     * 将会在调用 pay 接口的时候直接唤起一个 loading，直到唤起H5支付页面或者唤起外部的钱包付款页面 loading 才消失。（
     * 建议将该值设置为 true，优化点击付款到支付唤起支付页面的过渡过程。）
     *
     * orderInfo 示例如下，参数说明见"请求参数说明"，orderInfo 的获取必须来源于服务端：
     * app_id=2015052600090779&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22seller_id%22%3A%22%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.02%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22314VYGIAGG7ZOYY%22%7D&charset=utf-8&method=alipay.trade.app.pay&sign_type=RSA2&timestamp=2016-08-15%2012%3A12%3A15&version=1.0&sign=MsbylYkCzlfYLy9PeRwUUIg9nZPeN9SfXPNavUCroGKR5Kqvx0nEnd3eRmKxJuthNUx4ERCXe552EV9PfwexqW%2B1wbKOdYtDIb4%2B7PL3Pc94RZL0zKaWcaY3tSL89%2FuAVUsQuFqEJdhIukuKygrXucvejOUgTCfoUdwTi7z%2BZzQ%3D
     *
     * @param orderInfo 订单信息-从后台获取 app支付请求参数字符串，主要包含商户的订单信息，key=value形式，以&连接。
     * @param onPayAction 支付成功与否操作
     */
    public void getAlipayInfo(OnPayAction onPayAction, final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付结果获取和处理
     * 调用 pay 方法支付后，将通过2种途径获得支付结果：
     *
     * 同步返回
     * private Handler mHandler = new Handler() {
     * 		public void handleMessage(Message msg) {
     * 			Result result = new Result((String) msg.obj);
     * 			Toast.makeText(DemoActivity.this, result.getResult(),
     * 						Toast.LENGTH_LONG).show();
     *                };* 	};
     *
     * 异步通知
     * 商户需要提供一个 http 协议的接口，包含在请求支付的入参中，其 key 对应 notify_url。
     * 支付宝服务器在支付完成后，会以 POST 方式调用 notify_url 传输数据。
     *
     * 获取当前开发包版本号
     * 调用 PayTask 对象的 getVersion() 方法查询。
     * 代码示例：
     *
     * PayTask payTask = new PayTask(activity);
     * String version = payTask.getVersion();
     *
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            Map<String, String> result = (Map<String, String>) msg.obj;
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = result.get("result");
            String resultStatus = result.get("resultStatus");
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                Toast.makeText(activity, "支付成功: " + result, Toast.LENGTH_SHORT).show();
                onPayAction.payResultAction(true);
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(activity, "支付失败: " + result, Toast.LENGTH_SHORT).show();
                onPayAction.payResultAction(false);
            }

        }
    };

    public interface OnPayAction{
        void payResultAction(boolean isSuccess);
    }

}
