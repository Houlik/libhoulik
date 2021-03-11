package com.houlik.libhoulik.android.util;

/**
 * Android版本对应各API
 * Created by houlik on 2018/10/3.
 */

public class VersionUtils {

    private final int GINGERBREAD_2_3_3 = 10;
    private final int HONEYCOMB_3_0_UP = 11;
    private final int HONEYCOMB_MR1_3_1_UP = 12;
    private final int HONEYCOMB_MR2_3_2 = 13;
    private final int ICE_CREAM_SANDWICH_4_0_UP = 14;
    private final int ICE_CREAM_SANDWICH_MR1_4_0_3 = 15;
    private final int JELLY_BEAN_4_1 = 16;
    private final int JELLY_BEAN_4_2 = 17;
    private final int JELLY_BEAN_4_3 = 18;
    private final int KITKAT_4_4 = 19;
    private final int LOLLIPOP_5_0 = 21;
    private final int LOLLIPOP_5_1 = 22;
    private final int MARSHMALLOW_6_0 = 23;
    private final int NOUGAT_7_0 = 24;
    private final int NOUGAT_7_1_1 = 25;
    private final int OREO_8_0 = 26;
    private final int OREO_8_1 = 27;
    private final int PIE_9_0 = 28;
    private final int ANDROID_10 = 29;
    private final int ANDROID_11 = 30;
    private final String CURRENT_VERSION= android.os.Build.VERSION.RELEASE;

    private static VersionUtils versionUtils = new VersionUtils();

    private VersionUtils(){}

    public static VersionUtils getInstance(){
        if(versionUtils == null){
            new VersionUtils();
        }
        return versionUtils;
    }

    public int getGINGERBREAD_2_3_3() {
        return GINGERBREAD_2_3_3;
    }

    public int getHONEYCOMB_3_0_UP() {
        return HONEYCOMB_3_0_UP;
    }

    public int getHONEYCOMB_MR1_3_1_UP() {
        return HONEYCOMB_MR1_3_1_UP;
    }

    public int getHONEYCOMB_MR2_3_2() {
        return HONEYCOMB_MR2_3_2;
    }

    public int getICE_CREAM_SANDWICH_4_0_UP() {
        return ICE_CREAM_SANDWICH_4_0_UP;
    }

    public int getICE_CREAM_SANDWICH_MR1_4_0_3() {
        return ICE_CREAM_SANDWICH_MR1_4_0_3;
    }

    public int getJELLY_BEAN_4_1() {
        return JELLY_BEAN_4_1;
    }

    public int getJELLY_BEAN_4_2() {
        return JELLY_BEAN_4_2;
    }

    public int getJELLY_BEAN_4_3() {
        return JELLY_BEAN_4_3;
    }

    public int getKITKAT_4_4() {
        return KITKAT_4_4;
    }

    public int getLOLLIPOP_5_0() {
        return LOLLIPOP_5_0;
    }

    public int getLOLLIPOP_5_1() {
        return LOLLIPOP_5_1;
    }

    public int getMARSHMALLOW_6_0() {
        return MARSHMALLOW_6_0;
    }

    public int getNOUGAT_7_0() {
        return NOUGAT_7_0;
    }

    public int getNOUGAT_7_1_1() {
        return NOUGAT_7_1_1;
    }

    public int getOREO_8_0() {
        return OREO_8_0;
    }

    public int getOREO_8_1() {
        return OREO_8_1;
    }

    public int getPIE_9_0() {
        return PIE_9_0;
    }

    public int getANDROID_10() {
        return ANDROID_10;
    }

    public int getANDROID_11() {
        return ANDROID_11;
    }

    public String getCURRENT_VERSION(){
        return CURRENT_VERSION;
    }
}
