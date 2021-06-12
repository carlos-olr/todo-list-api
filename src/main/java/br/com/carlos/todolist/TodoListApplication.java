package br.com.carlos.todolist;


import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.carlos.todolist.model.Tarefa;


@SpringBootApplication
public class TodoListApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoListApplication.class, args);
    }

    @Configuration
    @EnableCaching
    class CachingConfig {

        @Bean
        CacheManager cacheManager() {

            SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(Collections.singletonList(
                    new ConcurrentMapCache(Tarefa.CACHE_FIND_TAREFAS_BY_ID_USUARIO)) //
            );

            return cacheManager;
        }
    }

}
