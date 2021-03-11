package com.houlik.libhoulik.android.util;

import java.io.InputStream;
import java.util.List;

/**
 * 解析XML
 * Created by Houlik on 20/03/2017.
 */

public interface XMLParser {

    /**
     * 解析输入流 得到Object对象集合
     * @param is
     * @return
     * @throws Exception
     */
    public List<Object> parse(InputStream is) throws Exception;

    /**
     * 序列化Object对象集合 得到XML形式的字符串
     * @param list
     * @return
     * @throws Exception
     */
    public String serialize(List<Object> list) throws Exception;
}
