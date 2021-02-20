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
        User user =new User("lzp1","10" );
        Set<ZSetOperations.TypedTuple<Object>> set =new HashSet<>();
        DefaultTypedTuple<Object> defaultTypedTuple1 = new DefaultTypedTuple<>("b", 20.0);
        DefaultTypedTuple<Object> defaultTypedTuple2 = new DefaultTypedTuple<>("c", 30.0);
        DefaultTypedTuple<Object> defaultTypedTuple3 = new DefaultTypedTuple<>("d", 40.0);
        DefaultTypedTuple<Object> defaultTypedTuple4 = new DefaultTypedTuple<>(user, 10.0);
        set.add(defaultTypedTuple1);
        set.add(defaultTypedTuple2);
        set.add(defaultTypedTuple3);
        set.add(defaultTypedTuple4);
        redisTemplate.opsForZSet().add( "plZset",set);
        Set<Object> plZset = redisTemplate.opsForZSet().range( "plZset", 0, -1 );

        //在zset中添加a
        redisTemplate.opsForZSet().add("myZSet", "aa",10);
        redisTemplate.opsForZSet().add("myZSet", "bb",5);
        redisTemplate.opsForZSet().add("myZSet", "cc",15);
        User user1 =new User("lzp1","10" );
        redisTemplate.opsForZSet().add("myZSet", user1,3);
        redisTemplate.opsForZSet().add("myZSet", "bba",5);
        Set<Object> myZSet = redisTemplate.opsForZSet().range( "myZSet", 0, -1 );

        System.out.println(redisTemplate.opsForZSet().range("myZSet", 0, -1));
        //获取zset中指定score范围值内的元素
        Set<Object> myZSet1 = redisTemplate.opsForZSet().rangeByScore( "myZSet", 3, 5 );
        //对zset中元素的score进行递增
        Double score = redisTemplate.opsForZSet().score( "myZSet", "cc" );
        redisTemplate.opsForZSet().incrementScore("myZSet","cc",5);
        Double scoreAfter = redisTemplate.opsForZSet().score( "myZSet", "cc" );
        //根据score的区间值统计zset在改score区间中的元素个数
        Long myZSet2 = redisTemplate.opsForZSet().count( "myZSet", 5, 15 );
        //根据score获取zset元素中rank
        Long rank = redisTemplate.opsForZSet().rank( "myZSet", "cc" );
        //根据rank获取zset元素中score
        Double score1 = redisTemplate.opsForZSet().score( "myZSet", "cc" );
        return "ok";
    }
}
