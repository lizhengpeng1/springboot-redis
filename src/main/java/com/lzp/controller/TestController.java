package com.lzp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lzp.config.RedisUtil;
import com.lzp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author lizhengpeng
 * @create 2021/2/19 - 9:22
 * @describe
 */
@RestController
public class TestController {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value="/test/string")
    public String testString() throws JsonProcessingException {
        //实际开发环境都是使用json传输的
        User user =new User("lzp","15" );
        //String jsonUser = new ObjectMapper().writeValueAsString(user);
        //redisTemplate.opsForValue().set("user",jsonUser);
        //string类型操作
        redisTemplate.opsForValue().set("user",user);
        redisTemplate.opsForValue().set("k1","k1");
        redisUtil.set( "k2","k2" );
        User users=(User)redisTemplate.opsForValue().get("user");
        User users1=(User)redisUtil.get("user");
        System.out.println(redisTemplate.opsForValue().get("user"));
        System.out.println(redisUtil.get("user"));
        System.out.println(redisUtil.get("k1"));
        System.out.println(redisUtil.get("k2"));
        return "ok";
    }

    @RequestMapping(value="/test/map")
    public String testMap() throws JsonProcessingException {
        redisUtil.hset( "hm","name","lzp");
        Map<String,Object> map=new HashMap<String,Object>();
        User user1 =new User("lzp1","10" );
        User user2 =new User("lzp2","20" );
        map.put("user1",user1);
        map.put("user2",user2);
        map.put("abc","abc");
        redisUtil.hmset( "hm",map);
        System.out.println(redisUtil.hget("hm","user1"));
        System.out.println(redisUtil.hget("hm","user2"));
        System.out.println(redisUtil.hget("hm","abc"));
        System.out.println(redisUtil.hget("hm","name"));
        Map<Object, Object> hmap=redisUtil.hmget( "hm" );
        User u =(User)hmap.get("user1");
        return "ok";
    }

    @RequestMapping(value="/test/list")
    public String testList() throws JsonProcessingException {
        ///list 演示
        redisTemplate.opsForList().rightPush( "list0","aaa");
        List<Object> list0 = redisTemplate.opsForList().range( "list0", 0, -1 );
        long length=redisTemplate.opsForList().size( "list0");
        redisTemplate.opsForList().leftPushAll("list1","abc","sss",1);
        List<Object> list1 = redisTemplate.opsForList().range( "list1", 0, -1 );
        redisTemplate.opsForList().rightPushAll( "list2","d","s","52");
        List<Object> list2 = redisTemplate.opsForList().range( "list2", 0, -1 );
        List<Object> plist=new ArrayList<>();
        plist.add( "111" );
        plist.add( "222" );
        plist.add( 333 );
        redisTemplate.opsForList().rightPushAll( "plist",plist );
        redisTemplate.opsForList().rightPush( "plist","aaa");
        List<Object> list3 = redisTemplate.opsForList().range( "plist", 0, -1 );
        return "ok";
    }

    @RequestMapping(value="/test/set")
    public String testSet() throws JsonProcessingException {
        redisTemplate.opsForSet().add( "set1" ,"11","22","33","44");
        Set<Object> set1 = redisTemplate.opsForSet().members( "set1" );
        Boolean boo=redisTemplate.opsForSet().isMember("set1","abc" );
        Boolean boo1=redisTemplate.opsForSet().isMember("set1","11" );
        long size=redisTemplate.opsForSet().size("set1");
        return "ok";
    }

    @RequestMapping(value="/test/zset")
    public String testZSet() throws JsonProcessingException {
        Set<ZSetOperations.TypedTuple<String>> typedTupleSet=new HashSet<>(  );
        for(int i=1;i<=9;i++){
            double score= i*0.1;
            ZSetOperations.TypedTuple typedTuple=new DefaultTypedTuple<String>("value"+i,score);
            typedTupleSet.add( typedTuple );
        }
       // redisTemplate.opsForZSet().add("zset",typedTupleSet);
        String aa="测试1";

        return "ok";
    }
}
