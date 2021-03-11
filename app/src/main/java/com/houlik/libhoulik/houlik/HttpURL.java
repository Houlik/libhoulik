package com.houlik.libhoulik.houlik;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 URLConnection与HttpURLConnection区别,都是抽象类，无法直接实例化对象, 主要通过URL的openConnection方法获得对象
 1.每次openConnection都将创建一个新的实例。
 2.openConnection可以在连接操作进行之前可对URLConnection或者HttPURLConnection实例的某些属性进行设置，如设置超时值等。
 3.getInputStream属于应用层的操作，都会调用connect操作。
    connectTimeout与ReaderTimeout并不相同。有可能在已连接的情况下，仍然Reader超时
 使用HttpURLConnection对象和Internet交互
    a.从Internet获取网页，发送请求,将网页以流的形式读回来.
        1)创建一个URL对象:URL url = new URL("http://www.houlik.cn");
        2)利用HttpURLConnection对象从网络中获取网页数据:HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        3)设置连接超时:conn.setConnectTimeout(6* 1000);
        4)对响应码进行判断:if (conn.getResponseCode() != 200) throw new RuntimeException("请求url失败");
        5)得到网络返回的输入流:InputStream is = conn.getInputStream();
        6)String result = readData(is, "GBK");
        conn.disconnect();
        总结:
        --我们必须要记得设置连接超时,如果网络不好,Android系统在超过默认时间会收回资源中断操作.
        --返回的响应码200,是成功.
        --利用ByteArrayOutputStream类,将得到的输入流写入内存.
        --在Android中对文件流的操作和JAVA SE上面是一样的.

    b.从Internet获取文件，利用HttpURLConnection对象,我们可以从网络中获取文件数据.
        1)创建URL对象,并将文件路径传入:URL url = new URL("http://photo.houlik.cn/20190614/Img12345678.jpg");
        2)创建HttpURLConnection对象,从网络中获取文件数据:HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        3)设置连接超时:conn.setConnectTimeout(6* 1000);
        4)对响应码进行判断:if (conn.getResponseCode() != 200) throw new RuntimeException("请求url失败");
        5)得到网络返回的输入流:InputStream is = conn.getInputStream();
        6)将得到的文件流写出:outStream.write(buffer, 0, len);
        总结:
        --在对大文件的操作时,要将文件写到SDCard上面,不要直接写到手机内存上.
        --操作大文件是,要一边从网络上读,一边要往SDCard上面写,减少手机内存的使用.
        --对文件流操作完,要记得及时关闭.

    c.向Internet发送请求参数
        1)将地址和参数存到byte数组中:byte[] data = params.toString().getBytes();
        2)创建URL对象:URL realUrl = new URL(requestUrl);
        3)通过HttpURLConnection对象,向网络地址发送请求:HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
        4)设置容许输出:conn.setDoOutput(true);
        5)设置不使用缓存:conn.setUseCaches(false);
        6)设置使用POST的方式发送:conn.setRequestMethod("POST");
        7)设置维持长连接:conn.setRequestProperty("Connection", "Keep-Alive");
        8)设置文件字符集:conn.setRequestProperty("Charset", "UTF-8");
        9)设置文件长度:conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        10)设置文件类型:conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        11)以流的方式输出.
        总结:
        --发送POST请求必须设置允许输出
        --不要使用缓存,容易出现问题.
        --在开始用HttpURLConnection对象的setRequestProperty()设置,就是生成HTML文件头.

    d.向Internet发送xml数据，XML格式是通信的标准语言,Android系统也可以通过发送XML文件传输数据.
        1)将生成的XML文件写入到byte数组中,并设置为UTF-8:byte[] xmlbyte = xml.toString().getBytes("UTF-8");
        2)创建URL对象,并指定地址和参数:URL url = new URL("http://houlik.cn/folder/processFile.do?method=readxml");
        3)获得链接:HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        4)设置连接超时:conn.setConnectTimeout(6* 1000);
        5)设置允许输出conn.setDoOutput(true);
        6)设置不使用缓存:conn.setUseCaches(false);
        7)设置以POST方式传输:conn.setRequestMethod("POST");
        8)维持长连接:conn.setRequestProperty("Connection", "Keep-Alive");
        9)设置字符集:conn.setRequestProperty("Charset", "UTF-8");
        10)设置文件的总长度:conn.setRequestProperty("Content-Length", String.valueOf(xmlbyte.length));
        11)设置文件类型:conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        12)以文件流的方式发送xml数据:outStream.write(xmlbyte);
        总结:
        --我们使用的是用HTML的方式传输文件,这个方式只能传输一般在5M一下的文件.
        --传输大文件不适合用HTML的方式,传输大文件我们要面向Socket编程.确保程序的稳定性.
**/

/**
 * @author Houlik
 * @since 15/06/2019
 *
 * 用途:通过HttpURLConnection连接获取网页
 */
public class HttpURL implements Runnable {
    private String strURL;
    private String params;
    private StringBuffer sb;
    private URL url;
    private HttpURLConnection httpURLConnection;
    private String requestType;
    private boolean doOutput;
    private boolean doInput;
    private boolean setUseCaches;
    private OnHttpURLResult onHttpURLResult;

    /**
     *
     * @param strURL 发送请求参数的网址
     * @param params 请求的参数 比如: http的属性 属性=值 (name=value)
     * @param requestType GET | POST
     * @param doOutput 传递参数
     * @param doInput 获取返回值
     * @param setUseCaches
     */
    public HttpURL(String strURL, String params,String requestType,boolean doOutput, boolean doInput, boolean setUseCaches){
        this.strURL = strURL;
        this.params = params;
        this.requestType = requestType;
        this.doOutput = doOutput;
        this.doInput = doInput;
        this.setUseCaches = setUseCaches;
    }

    @Override
    public void run() {
        try {
            url = new URL(strURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(requestType);
            httpURLConnection.setDoOutput(doOutput);
            httpURLConnection.setDoInput(doInput);
            httpURLConnection.setUseCaches(setUseCaches);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(params.getBytes());
            outputStream.flush();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            System.out.println(inputStream.read());
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"gbk");
            char[] chars = new char[1024];
            int len = 0;
            while((len = inputStreamReader.read(chars))!= -1){
                sb = new StringBuffer(new String(chars,0,len));
            }
            if(sb != null) {
                onHttpURLResult.getResult(sb);
            }
            httpURLConnection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface OnHttpURLResult{
        void getResult(StringBuffer stringBuffer);
    }

    public void setOnHttpURLResult(OnHttpURLResult onHttpURLResult){
        this.onHttpURLResult = onHttpURLResult;
    }
}


