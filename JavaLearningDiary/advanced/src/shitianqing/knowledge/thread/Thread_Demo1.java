package shitianqing.knowledge.thread;

public class Thread_Demo1 {
    public static void main(String[] args) {
        //先来看看主线程叫啥名字吧，不会就叫main吧？
        /*
            通过Thread类的静态方法currentThread可以获取到当前线程对象，
            而通过getName方法，可以获得当前线程对象的名字。
         */
        System.out.println("第一个线程是：" + Thread.currentThread().getName());
        //创建线程对象
        Grape grape = new Grape();
        //启动线程！
        grape.start();
        /*
            通过start方法启动线程，默认会调用线程类中的run方法。
         */
        for (int i = 0; i < 5; ++i) {
            System.out.println("主线程正在执行中，这是👴第 " + (i+1) + " 次出现。");
            try {
                Thread.sleep(500);
            } catch (
                    InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Grape extends Thread {
    /**
     * 当某个类继承了Thread类后，就可以当作线程使用
     */
    private int times = 0;      //定义times变量，用于统计run方法被执行的次数。

    /*
        一般要重写run方法（本质是实现了Runnable接口中的抽象方法），以实现自己的业务需求。
     */
    @Override
    public void run() {
        //那第二个线程默认名字是啥呢？
        System.out.println("第二个线程是：" + Thread.currentThread().getName());
        while (true) {
            System.out.println("我被吃第" + ++times + "次了😭！");
            /*
                Thread类中的静态方法sleep可以让线程休眠指定时间，传入的实参以毫秒为单位。
                使用sleep方法时会出现编译期异常，使用try-catch包围。
             */
            try {
                Thread.sleep(500);      //让线程每执行一次run方法就休眠0.5秒
            } catch (
                    InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (times >= 11)            //执行11次后，跳出while循环
                break;
        }
    }
}
