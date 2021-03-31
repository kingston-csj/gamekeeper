package jforgame.admin.utils;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by qizhao.liao on 2019/11/4.
 */
public class NameThreadFactory implements ThreadFactory {

    private final String name;
    private final AtomicInteger index = new AtomicInteger(0);

    public NameThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "name="+name+",index="+index.getAndIncrement());
    }
}
