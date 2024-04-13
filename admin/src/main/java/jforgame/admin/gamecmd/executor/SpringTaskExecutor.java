package jforgame.admin.gamecmd.executor;

import jforgame.commons.thread.NamedThreadFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by qizhao.liao on 2019/11/4.
 */
//@Configuration
//@EnableAsync
public class SpringTaskExecutor implements AsyncConfigurer {

    private static final int DEFAULT_INIT_THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() / 10;

//    /**
//     * 执行线程池
//     */
//    private ThreadPoolExecutor executor;
//
//    @PostConstruct
//    private void init() {
//        executor = new ThreadPoolExecutor(DEFAULT_INIT_THREAD_POOL_SIZE, DEFAULT_INIT_THREAD_POOL_SIZE * 2, 0L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<>(), new NameThreadFactory("执行线程池"));
//    }
//
//
//    public ThreadPoolExecutor getExecutor() {
//        return executor;
//    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(DEFAULT_INIT_THREAD_POOL_SIZE);
        taskExecutor.setMaxPoolSize(DEFAULT_INIT_THREAD_POOL_SIZE * 2);
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.setThreadFactory(new NamedThreadFactory("执行线程池"));
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
