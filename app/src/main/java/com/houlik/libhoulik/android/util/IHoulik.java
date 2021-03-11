package com.houlik.libhoulik.android.util;

/**
 * 接口讲解
 * Created by houlik on 2018/5/10.
 */

public interface IHoulik {

    /**
     * 接口是回调, 回调是用于在需要某类值的时候调用, 不使用时将不创建。
     * 接口是模版, 一般模版是用于多人开发时使用
     *
     * 简单来说 = 个人的理解
     *
     * 就是把当前需要在别处使用的值通过接口传递过去,而且该接口只有在调用该方法时候才产生效果,用于非常多类时使用
     *
     * 1. 创建一个接口, 创建接口方法以及你所需要的值传递
     *
     *      Interface OnResult{
     *          void getResult(String secret);
     *      }
     *
     * 2. 在希望传递值的类中创建接口的调用对象 以及 创建一个回调的方法,该方法中的传递值是把实现的接口对象交给当前类创建接口的调用对象
     *
     *  public class MyClass{
     *
     *      //接口对象
     *      OnResult result;
     *                              //实现的接口对象
     *      public void setOnResult(OnResult result){
     *          //把方法的接口对象交给当前类对象
     *          this.result = result;
     *      }
     *
     * 3. 在该类某方法中找到需要传递的值,把值赋予给接口方法中的函数
     *
     *      //当前类方法                    //函数
     *      public void doSomethingMethod(String important){
     *          // do Something 一些操作
     *
     *    //简称 类型  = 文件 = 数据值/函数值/变量值
     *          String name = important;
     *
     *          //使用当前类创建的接口对象调用接口中的方法把需要的值传递出去
     *          result.getResult(name);
     *      }
     *  }
     * 4. 也可以在该类方法中直接得到全局的变量值
     *      public void setOnResult(OnResult result){
     *          //把方法的接口对象交给当前类对象
     *          result.getResult(所需的当前类全局某变量的值);
     *      }
     * 5. 记住,回调,只能在该类的全局某变量值已经被赋值或者该类的方法已经被调用才能传递到需要的值,否则返回值将是空的
     * 6. 需要在哪个类中使用需要的值就可以在哪个类中实现当前类回调方法,实现接口以及接口方法任意使用接口方法中的值
     *
     *      MyClass myClass = new MyClass();
     *      //调用类方法实现接口以及接口中的方法
     *      myClass.setOnResult(new OnResult(){
     *
     *          @Override
     *          public void getResult(String secret){
     *
     *              //这里就可以得到secret 的值了
     *
     *              //当中值只有在MyClass类中的全局某变量值已经被赋值 以及 doSomethingMethod方法已经被使用时才有值,否则为空值
     *          }
     *
     *      })
     *
     *
     */
}
