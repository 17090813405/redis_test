package com.example.redis;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author daizhichao
 * @Date 2020/9/23 15:25
 */
@SpringBootTest
class RedisApplicationTests {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testInsert() {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("zKey", JSONObject.toJSONString(new User("cat1", 1, 1)), 100);
        zSetOperations.add("zKey", JSONObject.toJSONString(new User("cat2", 2, 1)), 50);
        zSetOperations.add("zKey", JSONObject.toJSONString(new User("cat3", 3, 1)), 25);
        zSetOperations.add("zKey", JSONObject.toJSONString(new User("cat4", 4, 1)), 5);
    }

    @Test
    public void testGet() {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        Long zKey = zSetOperations.zCard("zKey");
        System.out.println(zKey);

        //范围查找
        Set<ZSetOperations.TypedTuple<Object>> zKey1 = zSetOperations.rangeWithScores("zKey", 0, -1);
        assert zKey1 != null;
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = zKey1.iterator();
        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            System.out.println(typedTuple.getValue() + "===>>>" + typedTuple.getScore());
        }

        //降序范围查找
        Set<ZSetOperations.TypedTuple<Object>> zKey2 = zSetOperations.reverseRangeWithScores("zKey", 0, 2);
        assert zKey2 != null;
        Iterator<ZSetOperations.TypedTuple<Object>> iterator1 = zKey2.iterator();
        while (iterator1.hasNext()) {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator1.next();
            System.out.println(typedTuple.getValue() + "===>>>" + typedTuple.getScore());
        }

        Long rank = zSetOperations.rank("zKey", JSONObject.toJSONString(new User("cat1", 1, 1)));
        System.out.println(rank + 1);

        Long reverseRank = zSetOperations.reverseRank("zKey", JSONObject.toJSONString(new User("cat1", 1, 1)));
        System.out.println(reverseRank + 1);

        System.out.println(zSetOperations.count("zKey", 50, 100));
    }

    @Test
    public void testUpdate() {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        Boolean add = zSetOperations.add("zKey", JSONObject.toJSONString(new User("cat1", 1, 1)), 1000);
        System.out.println(add);
    }

    @Test
    public void testDelete() {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.removeRangeByScore("zKey", 5, 25);
    }

    @Data
    @AllArgsConstructor
    static class User {
        private String userName;
        private int age;
        private int sex;
    }
}
