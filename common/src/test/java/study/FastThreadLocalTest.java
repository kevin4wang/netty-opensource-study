package study;

import io.netty.util.concurrent.FastThreadLocal;

/**
 * Created by kevin on 2018/12/11.
 */
public class FastThreadLocalTest {
    private static FastThreadLocal<Object> threadLocal = new FastThreadLocal<Object>(){
        @Override
        protected Object initialValue() {
            return new Object();
        }
    };

//

    /**
     * 在该方法中有有两个线程，每个线程都从FastThreadLocal获取对象，这时每个线程获取的对象都不一致，如果一个线程修改获取后的对象，其他线程
     * 获取到的对象不会变化，这样就做到了线程独享该对象。
     * @param args
     */
    public static void main(String[] args) {
        new Thread(() -> {
            Object object =threadLocal.get();
            System.out.println(object);
            while(true){
                threadLocal.set(new Object());
            }
        }).start();
        new Thread(() -> {
            Object object =threadLocal.get();
            System.out.println(object);
        }).start();
    }
}
