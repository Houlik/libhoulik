package com.houlik.libhoulik.hl3api;

/**
 * Created by houlik on 2018/5/19.
 */

public class RxAndroidUtils {

    /**
     *
     * onCompleted()
     * onError()
     * Schedulers工作表 - 针对主线程问题 以及 解决多线程问题
     * 观察者模式 Observable 被观察 - onNext/onCompleted/onError - Observer 观察者
     *
     * 创建Observable的各种方法
     * 创建被观察者 Create,Just,From,Defer,Empty/Never/Throw,Interval,Range,Repeat,Start,Timer
     * Create 原始创建 一般使用 Just == Observable obs = Observable.create(new OnSubscribe<String>(){call}).
     * Just 与 create类似
     * From 可以接收数组/集合 等
     * Defer 在创建调用observable.subscribe 之后执行 - 使用 new Func0<Observable<String>>
     * Empty 空items Never 不执行 Throw 丢出错误执行onError回调
     * Interval 定时发送
     * Range 执行指定的之间的事件
     * Repeat 重复执行
     * Start 从何开始
     * Timer 定时
     *
     * 创建观察者
     * Subscriber sub = new Subscriber<String>(){onCompleted/onError/onNext}
     *
     *
     * 转换
     * 用于对Observable发射的数据执行变换操作的各种操作符
     * Map 一对一 用于类型转换
     * FlatMap 一对多
     * GroupBy 归类分组
     * Buffer 规定事件之间的发送 一次发送多少
     * Scan 叠加发送, 之前总数加上最新后发送
     * Window
     *
     * 条件过滤
     * 用于过滤和选择Observable发射的数据序列
     * Debounce 发送第一次事件后 在指定的时间间隔内无其它操作就发送其它事件
     * Distinct 去掉重复数据
     * ElementAt 取指定位置的数据类似 Linked
     * Filter 按照自定义的条件过滤
     * First 取第一个位置的数据
     * IgnoreElements 忽略相关数据
     * Last 取最后一个位置数据
     * Sample 取样本采集后发送
     * Skip 跳跃指定位置后取数据
     * SkipLast 跳跃最后数据,取前数据
     * Take 取指定几个值
     * TakeLast 取指定最后几个值
     *
     * 组合
     * 用于组合多个Observables
     * And / Then / When 将两个或多个Observable 数据集合合并到一起
     * CombinetLatest 将发送的每一次数据全都打包返回
     * Join 结合两个Observable发射的数据
     * Merge 将多个Observables的输出合并，就好像它们是一个单个的Observable一样
     * StartWith 想要一个Observable在发射数据之前先发射一个指定的数据序列 ／ 想一个Observable发射的数据末尾追加一个数据序列可以使用Concat操作符
     * Switch 将一个发射多个Observables的Observable转换成另一个单独的Observable，后者发射那些Observables最近发射的数据项
     * Zip 通过一个函数将多个Observables的发射物结合到一起，基于这个函数的结果为每个结合体发射单个数据项
     *
     * 错误处理
     * 对Observable发射的onError通知做出响应或者从错误中恢复
     * Catch 拦截原始Observable的onError通知，将它替换为其它的数据项或数据序列，让产生的Observable能够正常终止或者根本不终止
     * Retry 原始Observable遇到错误，重新订阅它期望它能正常终止
     *
     * 辅助操作
     * 用于Observable的辅助操作符
     * Delay 延迟一段指定的时间再发射来自Observable的发射物
     * Do 注册一个动作作为原始Observable生命周期事件的一种占位符
     * Materialize / Dematerialize 将数据项和事件通知都当做数据项发射，Dematerialize刚好相反
     * ObserveOn 指定一个观察者在哪个调度器上观察这个Observable
     * Serialize 强制一个Observable连续调用并保证行为正确
     * Subscribe 连接观察者和Observable的胶水。一个观察者要想看到Observable发射的数据项，或者想要从Observable获取错误和完成通知，它首先必须使用这个操作符订阅那个Observable
     * SubscribeOn 指定Observable自身在哪个调度器上执行
     * TimeInterval 将一个发射数据的Observable转换为发射那些数据发射时间间隔的Observable
     * Timeout 对原始Observable的一个镜像，如果过了一个指定的时长仍没有发射数据，它会发一个错误通知
     * Timestamp 给Observable发射的数据项附加一个时间戳
     * Using 创建一个只在Observable生命周期内存在的一次性资源
     * To 将Observable转换为另一个对象或数据结构
     *
     * 条件和布尔操作
     * 用于根据条件发射或变换Observables，或者对它们做布尔运算
     * All / Contains / Amb 判定是否Observable发射的所有数据都满足某个条件 / 给定两个或多个Observables，它只发射首先发射数据或通知的那个Observable的所有数据 / 判定一个Observable是否发射一个特定的值
     * DefaultIfEmpty
     * SequenceEqual
     * SkipUntil/SkipWhile
     * TakeUntil/TakeWhile
     *
     * 算数和聚合操作
     * 用于对整个序列执行算法操作或其它操作，由于这些操作必须等待数据发射完成（通常也必须缓存这些数据），它们对于非常长或者无限的序列来说是危险的，不推荐使用
     * Average / Concat / Reduce 计算原始Observable发射数字的平均值并发射它 / 不交错的发射两个或多个Observable的发射物 / 按顺序对Observable发射的每项数据应用一个函数并发射最终的值
     * Max / Min / Count / Sum 发射原始Observable的最大值 / 发射原始Observable的最小值 / 计算原始Observable发射物的数量，然后只发射这个值 / 计算Observable发射的数值的和并发射这个和
     *
     * 异步操作
     * rxjava-async
     * 用于将同步对象转换为Observable
     * start( ) — 创建一个Observable，它发射一个函数的返回值
     * toAsync( ) or asyncAction( ) or asyncFunc( ) — 将一个函数或者Action转换为已Observable，它执行这个函数并发射函数的返回值
     * startFuture( ) — 将一个返回Future的函数转换为一个Observable，它发射Future的返回值
     * deferFuture( ) — 将一个返回Observable的Future转换为一个Observable，但是并不尝试获取这个Future返回的Observable，直到有订阅者订阅它
     * forEachFuture( ) — 传递Subscriber方法给一个Subscriber，但是同时表现得像一个Future一样阻塞直到它完成
     * fromAction( ) — 将一个Action转换为Observable，当一个订阅者订阅时，它执行这个action并发射它的返回值
     * fromCallable( ) — 将一个Callable转换为Observable，当一个订阅者订阅时，它执行这个Callable并发射Callable的返回值，或者发射异常
     * fromRunnable( ) — convert a Runnable into an Observable that invokes the runable and emits its result when a Subscriber subscribes将一个Runnable转换为Observable，当一个订阅者订阅时，它执行这个Runnable并发射Runnable的返回值
     * runAsync( ) — 返回一个StoppableObservable，它发射某个Scheduler上指定的Action生成的多个actions
     *
     * 连接操作
     * ConnectableObservable
     * Connect 让一个可连接的Observable开始发射数据给订阅者
     * Publish 将普通的Observable转换为可连接的Observable
     * RefCount 让一个可连接的Observable行为像普通的Observable
     * Replay 保证所有的观察者收到相同的数据序列，即使它们在Observable开始发射数据之后才订阅
     *
     * 阻塞操作
     * BlockingObservable
     * 一个阻塞的Observable 继承普通的Observable类，增加了一些可用于阻塞Observable发射的数据的操作符
     * forEach( ) — 对Observable发射的每一项数据调用一个方法，会阻塞直到Observable完成
     * first( ) — 阻塞直到Observable发射了一个数据，然后返回第一项数据
     * firstOrDefault( ) — 阻塞直到Observable发射了一个数据或者终止，返回第一项数据，或者返回默认值
     * last( ) — 阻塞直到Observable终止，然后返回最后一项数据
     * lastOrDefault( ) — 阻塞直到Observable终止，然后返回最后一项的数据，或者返回默认值
     * mostRecent( ) — 返回一个总是返回Observable最近发射的数据的iterable
     * next( ) — 返回一个Iterable，会阻塞直到Observable发射了另一个值，然后返回那个值
     * latest( ) — 返回一个iterable，会阻塞直到或者除非Observable发射了一个iterable没有返回的值，然后返回这个值
     * single( ) — 如果Observable终止时只发射了一个值，返回那个值，否则抛出异常
     * singleOrDefault( ) — 如果Observable终止时只发射了一个值，返回那个值，否则否好默认值
     * toFuture( ) — 将Observable转换为一个Future
     *toIterable( ) — 将一个发射数据序列的Observable转换为一个Iterable
     * getIterator( ) — 将一个发射数据序列的Observable转换为一个Iterator
     *
     *
     * StringObservable
     * 用于处理字符串序列和流的特殊操作符
     * byLine( ) — 将一个字符串的Observable转换为一个行序列的Observable，这个Observable将原来的序列当做流处理，然后按换行符分割
     * decode( ) — 将一个多字节的字符流转换为一个Observable，它按字符边界发射字节数组
     * encode( ) — 对一个发射字符串的Observable执行变换操作，变换后的Observable发射一个在原始字符串中表示多字节字符边界的字节数组
     * from( ) — 将一个字符流或者Reader转换为一个发射字节数组或者字符串的Observable
     * join( ) — 将一个发射字符串序列的Observable转换为一个发射单个字符串的Observable，后者用一个指定的字符串连接所有的字符串
     * split( ) — 将一个发射字符串的Observable转换为另一个发射字符串的Observable，后者使用一个指定的正则表达式边界分割前者发射的所有字符串
     * stringConcat( ) — 将一个发射字符串序列的Observable转换为一个发射单个字符串的Observable，后者连接前者发射的所有字符串
     *
     *
     *
     * Android使用一个叫 Handler 的类绑定异步通信到消息循环。为了在任意线程 观察 一个Observable，需要创建一个与那个类关联的 Handler，然后使用 AndroidSchedulers.handlerThread 调度器
     * 必须在 onDestroy 里取消订阅 this.subscription.unsubscribe();
     *
     */



}
