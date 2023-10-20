package com.fairfair.callcenter.configuration.system.action;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class ExecutorServiceConfigurer {

    @Bean
    public ThreadPoolTaskExecutor taskExecutorAction() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize( 40 );
        threadPoolTaskExecutor.setMaxPoolSize( 500 );
        threadPoolTaskExecutor.setQueueCapacity( 0 );

        return threadPoolTaskExecutor;
    }
}
