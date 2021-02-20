package com.lzp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author lizhengpeng
 * @create 2021/2/20 - 16:53
 * @describe
 */
@RestController
public class RedisLock {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @GetMapping
    public void lock(){
        String uuid= UUID.randomUUID().toString();
        WatchDog watchDog;
        try {
            while (true){
                Boolean flag=redisTemplate.opsForValue().setIfAbsent( "lock",uuid,3, TimeUnit.SECONDS );
                if(flag){
                    // 看门狗续命,防止业务时间太长，key时间过期
                    watchDog = new WatchDog(redisTemplate, uuid);
                    watchDog.start();
                    // 业务逻辑start
                    //处理业务4秒
                    Thread.sleep( 4 );
                    // 业务逻辑处理 end
                    break;
                }else{
                    //睡眠100ms再自旋
                    Thread.sleep( 100 );
                }
            }

        }catch (Exception e){
            System.out.println(e);
        }finally {
            // 关闭锁
            String l = (String) redisTemplate.opsForValue().get("lock");
            if (l.equalsIgnoreCase(uuid)) {
                redisTemplate.delete("lock");
            }

        }
    }


}
