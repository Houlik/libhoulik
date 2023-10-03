package com.houlik.libhoulik.houlikapp.server;

import okhttp3.Response;

/**
 * @author : houlikapp
 * @desription :
 * @email : houlik@126.com
 * @since : 10/3/2023
 */
public interface ServerImp {

    void responseServerData(String response);

    void responseFailured(Response response);

    void responseException(Exception e);
}
