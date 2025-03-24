package shitianqing.knowledge.thread;

public class Thread_Demo6 {
    public static void main(String[] args) throws InterruptedException {
        Ye ye = new Ye();
        Thread thread = new Thread(ye);
        //设置ye为守护线程
        thread.setDaemon(true);
        //启动👴线程
        thread.start();
        //当for循环执行完毕后，主线程就会退出，此时没有其他线程运行了，因此守护线程👴也会退出。
        for (int i = 0; i < 11; ++i) {
            System.out.println("此处不留👴，自有👴光处!" + (i + 1));
            //每执行一次for循环，让主线程休眠1s，以展示出效果
            Thread.sleep(1000);
        }
    }
}
class Ye implements Runnable {
    @Override
    public void run() {
        int times = 0;
        /*
            一个死循环，如果不是守护线程的话，永远不会退出。
         */
        while (true) {
            System.out.println("卷死👴了! " + (++times));
            //每执行一次while循环，令子线程休眠0.5秒，不然太快了
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
