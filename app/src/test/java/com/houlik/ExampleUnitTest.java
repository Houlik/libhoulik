package com.houlik;

import com.houlik.libhoulik.houlik.lucky.Lucky2Cal;
import com.houlik.libhoulik.inject.InjectIDUtils;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        //assertEquals(4, 2 + 2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Lucky2Cal lucky2Cal = new Lucky2Cal();
                List<String> list = lucky2Cal.getArraysAfterLoop(new String[]{"5","6","7","8"});
                for (Object obj: list
                ) {
                    System.out.println(obj);
                }
            }
        }).start();


    }

}