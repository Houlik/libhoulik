package com.houlik.libhoulik.android.util;

import android.content.Context;
import android.text.TextUtils;
import com.lidroid.xutils.http.HttpCache;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.SyncHttpHandler;
import com.lidroid.xutils.http.callback.HttpRedirectHandler;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.DefaultSSLSocketFactory;
import com.lidroid.xutils.http.client.RetryHandler;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.entity.GZipDecompressingEntity;
import com.lidroid.xutils.task.PriorityExecutor;
import com.lidroid.xutils.util.OtherUtils;
import java.io.File;
import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * 网络协议工具
 */
public class HttpUtils {

    public static final HttpCache sHttpCache = new HttpCache();
    private final DefaultHttpClient httpClient;
    private final HttpContext httpContext;
    private HttpRedirectHandler httpRedirectHandler;
    private String responseTextCharset;
    private long currentRequestExpiry;
    //private static final int DEFAULT_CONN_TIMEOUT = 15000;
    //private static final int DEFAULT_RETRY_TIMES = 3;
    //private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    //private static final String ENCODING_GZIP = "gzip";
    //private static final int DEFAULT_POOL_SIZE = 3;
    private static final PriorityExecutor EXECUTOR = new PriorityExecutor(3);

    public HttpUtils() {
        this(15000, (String)null);
    }

    public HttpUtils(int connTimeout) {
        this(connTimeout, (String)null);
    }

    public HttpUtils(String userAgent) {
        this(15000, userAgent);
    }

    public HttpUtils(int connTimeout, String userAgent) {
        this.httpContext = new BasicHttpContext();
        this.responseTextCharset = "UTF-8";
        this.currentRequestExpiry = HttpCache.getDefaultExpiryTime();
        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setTimeout(params, (long)connTimeout);
        HttpConnectionParams.setSoTimeout(params, connTimeout);
        HttpConnectionParams.setConnectionTimeout(params, connTimeout);
        if (TextUtils.isEmpty(userAgent)) {
            userAgent = OtherUtils.getUserAgent((Context)null);
        }

        HttpProtocolParams.setUserAgent(params, userAgent);
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(10));
        ConnManagerParams.setMaxTotalConnections(params, 10);
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", DefaultSSLSocketFactory.getSocketFactory(), 443));
        this.httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, schemeRegistry), params);
        this.httpClient.setHttpRequestRetryHandler(new RetryHandler(3));
        this.httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                if (!httpRequest.containsHeader("Accept-Encoding")) {
                    httpRequest.addHeader("Accept-Encoding", "text/plain");
                }

            }
        });
        this.httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    Header encoding = entity.getContentEncoding();
                    if (encoding != null) {
                        HeaderElement[] var8;
                        int var7 = (var8 = encoding.getElements()).length;

                        for(int var6 = 0; var6 < var7; ++var6) {
                            HeaderElement element = var8[var6];
                            if (element.getName().equalsIgnoreCase("gzip")) {
                                response.setEntity(new GZipDecompressingEntity(response.getEntity()));
                                return;
                            }
                        }
                    }

                }
            }
        });
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public HttpUtils configResponseTextCharset(String charSet) {
        if (!TextUtils.isEmpty(charSet)) {
            this.responseTextCharset = charSet;
        }

        return this;
    }

    public HttpUtils configHttpRedirectHandler(HttpRedirectHandler httpRedirectHandler) {
        this.httpRedirectHandler = httpRedirectHandler;
        return this;
    }

    public HttpUtils configHttpCacheSize(int httpCacheSize) {
        sHttpCache.setCacheSize(httpCacheSize);
        return this;
    }

    public HttpUtils configDefaultHttpCacheExpiry(long defaultExpiry) {
        HttpCache.setDefaultExpiryTime(defaultExpiry);
        this.currentRequestExpiry = HttpCache.getDefaultExpiryTime();
        return this;
    }

    public HttpUtils configCurrentHttpCacheExpiry(long currRequestExpiry) {
        this.currentRequestExpiry = currRequestExpiry;
        return this;
    }

    public HttpUtils configCookieStore(CookieStore cookieStore) {
        this.httpContext.setAttribute("http.cookie-store", cookieStore);
        return this;
    }

    public HttpUtils configUserAgent(String userAgent) {
        HttpProtocolParams.setUserAgent(this.httpClient.getParams(), userAgent);
        return this;
    }

    public HttpUtils configTimeout(int timeout) {
        HttpParams httpParams = this.httpClient.getParams();
        ConnManagerParams.setTimeout(httpParams, (long)timeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
        return this;
    }

    public HttpUtils configSoTimeout(int timeout) {
        HttpParams httpParams = this.httpClient.getParams();
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        return this;
    }

    public HttpUtils configRegisterScheme(Scheme scheme) {
        this.httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
        return this;
    }

    public HttpUtils configSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        Scheme scheme = new Scheme("https", sslSocketFactory, 443);
        this.httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
        return this;
    }

    public HttpUtils configRequestRetryCount(int count) {
        this.httpClient.setHttpRequestRetryHandler(new RetryHandler(count));
        return this;
    }

    public HttpUtils configRequestThreadPoolSize(int threadPoolSize) {
        EXECUTOR.setPoolSize(threadPoolSize);
        return this;
    }

    public <T> HttpHandler<T> send(HttpMethod method, String url, RequestCallBack<T> callBack) {
        return this.send(method, url, (RequestParams)null, callBack);
    }

    public <T> HttpHandler<T> send(HttpMethod method, String url, RequestParams params, RequestCallBack<T> callBack) {
        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        } else {
            com.lidroid.xutils.http.client.HttpRequest request = new com.lidroid.xutils.http.client.HttpRequest(method, url);
            return this.sendRequest(request, params, callBack);
        }
    }

    public ResponseStream sendSync(HttpMethod method, String url) throws com.lidroid.xutils.exception.HttpException {
        return this.sendSync(method, url, (RequestParams)null);
    }

    public ResponseStream sendSync(HttpMethod method, String url, RequestParams params) throws com.lidroid.xutils.exception.HttpException {
        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        } else {
            com.lidroid.xutils.http.client.HttpRequest request = new com.lidroid.xutils.http.client.HttpRequest(method, url);
            return this.sendSyncRequest(request, params);
        }
    }

    public HttpHandler<File> download(String url, String target, RequestCallBack<File> callback) {
        return this.download(HttpMethod.GET, url, target, (RequestParams)null, false, false, callback);
    }

    public HttpHandler<File> download(String url, String target, boolean autoResume, RequestCallBack<File> callback) {
        return this.download(HttpMethod.GET, url, target, (RequestParams)null, autoResume, false, callback);
    }

    public HttpHandler<File> download(String url, String target, boolean autoResume, boolean autoRename, RequestCallBack<File> callback) {
        return this.download(HttpMethod.GET, url, target, (RequestParams)null, autoResume, autoRename, callback);
    }

    public HttpHandler<File> download(String url, String target, RequestParams params, RequestCallBack<File> callback) {
        return this.download(HttpMethod.GET, url, target, params, false, false, callback);
    }

    public HttpHandler<File> download(String url, String target, RequestParams params, boolean autoResume, RequestCallBack<File> callback) {
        return this.download(HttpMethod.GET, url, target, params, autoResume, false, callback);
    }

    public HttpHandler<File> download(String url, String target, RequestParams params, boolean autoResume, boolean autoRename, RequestCallBack<File> callback) {
        return this.download(HttpMethod.GET, url, target, params, autoResume, autoRename, callback);
    }

    public HttpHandler<File> download(HttpMethod method, String url, String target, RequestParams params, RequestCallBack<File> callback) {
        return this.download(method, url, target, params, false, false, callback);
    }

    public HttpHandler<File> download(HttpMethod method, String url, String target, RequestParams params, boolean autoResume, RequestCallBack<File> callback) {
        return this.download(method, url, target, params, autoResume, false, callback);
    }

    public HttpHandler<File> download(HttpMethod method, String url, String target, RequestParams params, boolean autoResume, boolean autoRename, RequestCallBack<File> callback) {
        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        } else if (target == null) {
            throw new IllegalArgumentException("target may not be null");
        } else {
            com.lidroid.xutils.http.client.HttpRequest request = new com.lidroid.xutils.http.client.HttpRequest(method, url);
            HttpHandler<File> handler = new HttpHandler(this.httpClient, this.httpContext, this.responseTextCharset, callback);
            handler.setExpiry(this.currentRequestExpiry);
            handler.setHttpRedirectHandler(this.httpRedirectHandler);
            if (params != null) {
                request.setRequestParams(params, handler);
                handler.setPriority(params.getPriority());
            }

            handler.executeOnExecutor(EXECUTOR, new Object[]{request, target, autoResume, autoRename});
            return handler;
        }
    }

    private <T> HttpHandler<T> sendRequest(com.lidroid.xutils.http.client.HttpRequest request, RequestParams params, RequestCallBack<T> callBack) {
        HttpHandler<T> handler = new HttpHandler(this.httpClient, this.httpContext, this.responseTextCharset, callBack);
        handler.setExpiry(this.currentRequestExpiry);
        handler.setHttpRedirectHandler(this.httpRedirectHandler);
        request.setRequestParams(params, handler);
        if (params != null) {
            handler.setPriority(params.getPriority());
        }

        handler.executeOnExecutor(EXECUTOR, new Object[]{request});
        return handler;
    }

    private ResponseStream sendSyncRequest(com.lidroid.xutils.http.client.HttpRequest request, RequestParams params) throws com.lidroid.xutils.exception.HttpException {
        SyncHttpHandler handler = new SyncHttpHandler(this.httpClient, this.httpContext, this.responseTextCharset);
        handler.setExpiry(this.currentRequestExpiry);
        handler.setHttpRedirectHandler(this.httpRedirectHandler);
        request.setRequestParams(params);
        return handler.sendRequest(request);
    }
}
