package shitianqing.knowledge.thread;

public class Thread_Demo3 {
    public static void main(String[] args) throws InterruptedException {
        Cat cat = new Cat();
        Thread thread = new Thread(cat);
        //启动线程！
        thread.start();
        //setName 和 getName
        System.out.println("old name = " + thread.getName());
        thread.setName("线程1号");
        System.out.println("new name = " + thread.getName());
        //setPriority 和 getPriority
        System.out.println("\nold priority = " + thread.getPriority());
        thread.setPriority(Thread.MIN_PRIORITY);
        System.out.println("new priority = " + thread.getPriority());
        //先让主线程 main休眠 5s，等待run方法第一个for循环执行完成。
        Thread.sleep(5 * 1000);
        /*
            子线程休眠20s太长了，不想等了，倒计时3秒直接唤醒正在休眠的子线程
         */
        for (int i = 3; i > 0; --i) {
            System.out.println("唤醒子线程倒计时：" + i + "~");
            Thread.sleep(1 * 1000);
        }
        //唤醒正在休眠的子线程！
        thread.interrupt();
    }
}

class Cat implements Runnable {
    @Override
    public void run() {
        //run方法中第一个for循环
        for (int i = 0; i < 5; i++) {
            System.out.println("我是一只猫~ (number." + (i + 1) + ")");
            //每执行一次for循环，令子线程休眠1秒（以展示出效果）
            try {
                Thread.sleep(1 * 1000);
            } catch (
                    InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            //正常情况下，执行完run方法中第一个for循环后，令子线程休眠20秒。
            Thread.sleep(20 * 1000);
        } catch (
                InterruptedException e) {
            /*
                当执行interrupt方法中断子线程的休眠时，
                就会catch到一个InterruptedException类型的异常对象；
                我们可以在catch语句中写上自己的业务代码。
             */
            System.out.println("\n捕获到异常——" + e.getMessage());
            System.out.println(Thread.currentThread().getName() + "被唤醒！（被interrupt了）\n");
        }
        //run方法中第二个for循环
        for (int i = 0; i < 5; i++) {
            System.out.println("快乐的星猫~ (no." + (i + 1) + ")");
            try {
                Thread.sleep(500);
            } catch (
                    InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
