package shitianqing.knowledge.thread;

public class Thread_Demo2 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("第一个线程是：" + Thread.currentThread().getName());
        Apple apple = new Apple();
        //请Thread大仙出山帮个忙
        Thread thread = new Thread(apple);
        thread.start();
        for (int i = 0; i < 5; ++i) {
            System.out.println("主线程正在运行 " + (i + 1));
            Thread.sleep(500);
        }
    }
}

class Fruit {
}

class Apple extends Fruit implements Runnable {
    int times = 0;

    @Override
    public void run() {
        System.out.println("第二个线程是：" + Thread.currentThread().getName());
        while (true) {
            System.out.println("Apple NB! " + (++times));
            try {
                Thread.sleep(500);
            } catch (
                    InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (times >= 11)
                break;
        }
    }
}
