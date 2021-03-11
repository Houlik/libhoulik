package com.houlik.libhoulik.android.util;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Houlik on 2017-12-26.
 */

public class FragmentUtils {

    private FragmentActivity activity;
    private FragmentTransaction transaction;

    /**
     * 传递数据的类型 Bundle
     * BOOLEAN,BYTE,CHAR,SHORT,INT,LONG,FLOAT,DOUBLE,STRING,CHAR_SEQUENCE,
     * INTEGER_ARRAYLIST,STRING_ARRAYLIST,CHAR_SEQUENCE_ARRAYLIST,SERIALIZABLE,BOOLEAN_ARRAY,BYTE_ARRAY,SHORT_ARRAY,
     * CHAR_ARRAY,INT_ARRAY,LONG_ARRAY,FLOAT_ARRAY,DOUBLE_ARRAY,STRING_ARRAY,CHAR_SEQUENCE_ARRAY
     *
     * Bundle args = new bundle();
     * args.put...(* , *);
     * newFragment.setArguments(args)
     */

    public FragmentUtils(FragmentActivity activity){
        this.activity = activity;
        transaction = activity.getSupportFragmentManager().beginTransaction();
    }

    /**
     * 动态添加Fragment
     * @param idFrameLayout
     * @param savedInstanceState
     * @param addNewFragment
     */
    public void addFragment(int idFrameLayout, Bundle savedInstanceState, Fragment addNewFragment){
        if(activity.findViewById(idFrameLayout) != null){
            if(savedInstanceState != null){
                return;
            }
            addNewFragment.setArguments(activity.getIntent().getExtras());
            activity.getSupportFragmentManager().beginTransaction().add(idFrameLayout, addNewFragment).commit();
        }
    }

    public void add2Fragment(int idFrameLayout, Fragment currentFragment, Fragment newFragment){
        transaction = activity.getSupportFragmentManager().beginTransaction();
        if(currentFragment != null){
            transaction.hide(currentFragment);
            transaction.addToBackStack(null);
        }
        transaction.add(idFrameLayout, newFragment).commit();
    }

    /**
     * 动态更新Fragment
     * @param newFragment
     * @param putSomething
     * @param idFrameLayout
     */
    public void replaceFragment(Fragment newFragment, Bundle putSomething, int idFrameLayout){
        //传递一些数据
        newFragment.setArguments(putSomething);
        transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(idFrameLayout, newFragment);
        //添加到backStack 便于返回
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Fragment 与 Fragment 之间更换
     * @param idFrameLayout
     * @param currentFragment
     * @param newFragment
     */
    public void toFragment(int idFrameLayout, Fragment currentFragment, Fragment newFragment, String tag){
        transaction = activity.getSupportFragmentManager().beginTransaction();
        if(currentFragment != null){
            transaction.hide(currentFragment);
            transaction.addToBackStack(null);
        }
        if(tag == null) {
            transaction.replace(idFrameLayout, newFragment).commit();
        }else{
            transaction.replace(idFrameLayout, newFragment, tag).commit();
        }
    }

    public FragmentTransaction getTransaction(){
        return this.transaction;
    }
}
