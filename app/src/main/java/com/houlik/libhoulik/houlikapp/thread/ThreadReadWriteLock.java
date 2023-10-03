package com.houlik.libhoulik.houlikapp.thread;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 多线程之间的读写锁
 * @author Houlik
 * @since 2022/11/5
 * @description 这是多线程之间读写使用的锁
 *
 */
public class ThreadReadWriteLock {

    /** 多线程之间同一变量使用 **/
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public ThreadReadWriteLock(){}

    //写锁
    public void actionWriteInLock(OnLock onLock){
        readWriteLock.writeLock().lock();
        onLock.writeAction();
        readWriteLock.writeLock().unlock();
    }

    //读锁
    public void actionReadInLock(OnLock onLock){
        readWriteLock.readLock().lock();
        onLock.readAction();
        readWriteLock.readLock().unlock();
    }

    //读写锁回调
    public interface OnLock{
        void writeAction();
        void readAction();
    }
}
