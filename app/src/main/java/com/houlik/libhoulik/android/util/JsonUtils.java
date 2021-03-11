package com.houlik.libhoulik.android.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON解析
 * Created by Houlik on 2017-11-01.
 */

public class JsonUtils {

    /**
      这是Json文件中数据存在的样式
       { 这是最外围的大括号
           "subject": [            #这个[ 是用于数组存储
               {           #这是内括号
               "主键": 100000,           #这是整数值
               "主键": 0,            #这是整数值
               "主键": "value_en",         #这是字符串,英文
               "主键": "value_cn",          #这是字符串,中文
               "主键": "value"          #这是字符串, 结束后不再有其它数据就不再需要设置逗号
               }           #这是结束内括号
           ]           #这是结束数组存储
       }           #这是结束外括号
     **/


    /**
     * JSONObject
     * 例子：
     * {
     *     product : {
     *         "code" : " ??? ",
     *         "description" : " ??? ",
     *         "qty" : " ??? "
     *     }
     * }
     *
     * JSONArray
     * 例子：
     * {
     *     product : [
     *     {
     *         "code" : " ??? ",
     *         "description" : " ??? ",
     *         "qty" : " ??? "
     *     },
     *     {
     *         "code" : " ??? ",
     *         "description" : " ??? ",
     *         "qty" : " ??? "
     *     }
     *     ]
     * }
     */

    private String jsonFile;
    private String readLine;
    private StringBuilder stringBuilder;

    /**
     * json文件必须放在assets资源文件夹里
     * @param context
     * @param jsonFileWithExtension
     */
    public JsonUtils(Context context ,String jsonFileWithExtension){
        this.jsonFile = jsonFileWithExtension;
        jsonRead(context);
    }

    public JsonUtils(StringBuilder builder){
        this.stringBuilder = builder;
    }

    /**
     * 解析JSON文件的主方法
     * @param context
     */
    private void jsonRead(Context context){
        //得到assets文件夹内要解析的文件
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/"+jsonFile);
        InputStream is = null;
        try {
            is = context.getAssets().open(jsonFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //传递文件给字符流
        InputStreamReader inputStreamReader = new InputStreamReader(is);
        //再把字符流传递给缓冲流
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        stringBuilder = new StringBuilder();
        try {
            while((readLine = bufferedReader.readLine()) != null){
                stringBuilder.append(readLine);
            }
            bufferedReader.close();
            inputStreamReader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单个数据
     * 得到JSON全部所需相关内容
     * 例子： name : 名称
     * 例子： value : 值
     * 希望按照名称得到值,那么就将以上 名称 设置为 key, 得到值只需要传递 名称key 就能得到相关得值
     * @param subject 根据主题解析所需内容
     * @param key 根据所需设置为主键
     * @param value 根据所需按照主键得到的值
     * @return 返回 Map 集合
     */
    public Map getJsonValue(String subject, String key, String value){
        //创建Map集合
        Map tmp_map = new HashMap();
        try {
            //创建Json对象得到解析后的字符串
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray(subject);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject j = jsonArray.getJSONObject(i);
                tmp_map.put(j.get(key),j.get(value));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tmp_map;
    }

    /**
     * 通过subject 和 key 数组获取全部的值，保存在集合中返回
     * 例子：List<List> list = jsonUtils.getJsonValue(new String[]{"subject1","subject2"},new String[]{"key1","key2","key3"});
     * @param subject 主题
     * @param key 键
     * @return
     */
    public List<List> getJsonValue(String[] subject, String... key){
        //临时集合
        List tmp_list;
        //保存集合到另一个集合中
        List tmp_List2List = new ArrayList();
        try {
            //获取已经解析的数据
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            for (int sub = 0; sub < subject.length; sub++) {
                //通过subject获取相关数据
                JSONArray jsonArray = jsonObject.getJSONArray(subject[sub]);
                for (int i = 0; i < jsonArray.length(); i++) {
                    //暂时的集合
                    tmp_list = new ArrayList();
                    for (int j = 0; j < key.length; j++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        //获取键的值保存到临时的集合中
                        tmp_list.add(jo.get(key[j]));
                    }
                    //把临时的集合保存到主集合中用于返回
                    tmp_List2List.add(tmp_list);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tmp_List2List;
    }

    /**
     * 数组使用
     * 得到JSON文件内容,按照键值对存储到Map集合，循环一次后把Map集合保存到List集合中返回
     * 可以通过List<Map> 得到单独所需数据
     * @param subject
     * @return
     */
    public List<Map> getJSONArrayKeyValue(String subject){
        List<Map> list = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray(subject);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                Map tmp_Map = new HashMap();
                for (int j = 0; j < jo.names().length(); j++) {
                    tmp_Map.put(jo.names().get(j),jo.getString((String) jo.names().get(j)));
                }
                list.add(tmp_Map);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 单一使用
     * 得到JSON文件内容,按照键值对存储到Map集合中返回
     * @param subject 根据主题解析所需内容
     */
    public Map getJSONObjectKeyValue(String subject){
        //创建Map集合
        Map tmp_map = new HashMap();
        try {
            //创建Json对象得到解析后的字符串
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONObject j = null;
            for (int i = 0; i < jsonObject.length(); i++) {
                j = jsonObject.getJSONObject(subject);
            }
            for (int i = 0; i < j.names().length(); i++) {
                tmp_map.put(j.names().get(i), j.getString((String) j.names().get(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tmp_map;
    }
}
