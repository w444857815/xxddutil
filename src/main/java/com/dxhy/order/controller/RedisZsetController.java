package com.dxhy.order.controller;

import com.dxhy.order.service.RedisService;
import com.dxhy.order.service.RedisZsetService;
import com.dxhy.order.service.StockService;
import com.dxhy.order.util.DateUtils;
import com.dxhy.order.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName RedisKucunController
 * @Description TODO
 * @Author wangruwei
 * @Date 2022/2/25 16:05
 * @Version 1.0
 */
@RestController
@Slf4j
public class RedisZsetController extends BaseController{

    @Autowired
    private RedisZsetService redisZsetService;

    /**
    * @Description 按分数由小到大排序，取前多少名。  活动key，起始位置，终止位置
    * @param
    * @Return java.util.Set<java.lang.String>
    * @Author wangruwei
    * @Date 2022/3/3 17:26
    **/
    @RequestMapping(value = "rangeWithScores")
    public Map<String, Object> rangeWithScores(String key,long startIndex,long lastIndex ) {
        Set<ZSetOperations.TypedTuple<String>> hh = redisZsetService.rangeWithScores(key,startIndex,lastIndex);
        log.info("获取到的数据:{}", JsonUtils.getInstance().toJsonString(hh));
        if(!CollectionUtils.isEmpty(hh)){
            return getSussRtn(hh, "获取成功");
        }else{
            return getFailRtn("获取失败");
        }
    }

    /**
     * @Description 按分数由大到小排序，取前多少名。  活动key，起始位置，终止位置
     * @param
     * @Return java.util.Set<java.lang.String>
     * @Author wangruwei
     * @Date 2022/3/3 17:26
     **/
    @RequestMapping(value = "reverseRangeWithScores")
    public Map<String, Object> reverseRangeWithScores(String key, long startIndex, long lastIndex ) {
        Set<ZSetOperations.TypedTuple<String>> hh = redisZsetService.reverseRangeWithScores(key,startIndex,lastIndex);
        log.info("获取到的数据:{}", JsonUtils.getInstance().toJsonString(hh));
        if(!CollectionUtils.isEmpty(hh)){
            return getSussRtn(hh, "获取成功");
        }else{
            return getFailRtn("获取失败");
        }
    }

    /**
    * @Description 按分数区间从小到大取数据。  活动key,低分，高分(包含)
    * @param key
    * @param lowScore
    * @param highScore
    * @Return java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple<java.lang.String>>
    * @Author wangruwei
    * @Date 2022/3/3 17:56
    **/
    @RequestMapping(value = "rangeByScoreWithScores")
    public Map<String, Object> rangeByScoreWithScores(String key, long lowScore, long highScore ) {
        Set<ZSetOperations.TypedTuple<String>> hh = redisZsetService.rangeByScoreWithScores(key,lowScore,highScore);
        log.info("获取到的数据:{}", JsonUtils.getInstance().toJsonString(hh));
        if(!CollectionUtils.isEmpty(hh)){
            return getSussRtn(hh, "获取成功");
        }else{
            return getFailRtn("获取失败");
        }
    }

    /**
     * @Description 按分数区间从大到小取数据。  活动key,低分，高分(包含)
     * @param key
     * @param lowScore
     * @param highScore
     * @Return java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple<java.lang.String>>
     * @Author wangruwei
     * @Date 2022/3/3 17:56
     **/
    @RequestMapping(value = "reverseRangeByScoreWithScores")
    public Map<String, Object> reverseRangeByScoreWithScores(String key, long lowScore, long highScore ) {
        Set<ZSetOperations.TypedTuple<String>> hh = redisZsetService.reverseRangeByScoreWithScores(key,lowScore,highScore);
        log.info("获取到的数据:{}", JsonUtils.getInstance().toJsonString(hh));
        if(!CollectionUtils.isEmpty(hh)){
            return getSussRtn(hh, "获取成功");
        }else{
            return getFailRtn("获取失败");
        }
    }


    /**
     * @Description 活动里新增一个用户  活动key,用户编码，分数
     * @param key
     * @param member
     * @param score
     * @Return java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple<java.lang.String>>
     * @Author wangruwei
     * @Date 2022/3/3 17:56
     **/
    @RequestMapping(value = "zSetadd")
    public Map<String, Object> zSetadd(String key, String member, double score ) {
        boolean hh = redisZsetService.add(key,member,score);
        if(hh){
            return getSussRtn("新增成功", "新增成功");
        }else{
            return getFailRtn("新增失败");
        }
    }

    /**
     * @Description 类似于 size length，获取活动key共有多少member参加
     * @param key
     * @Return java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple<java.lang.String>>
     * @Author wangruwei
     * @Date 2022/3/3 17:56
     **/
    @RequestMapping(value = "zCard")
    public Map<String, Object> zCard(String key) {
        Long hh = redisZsetService.zCard(key);
        return getSussRtn(hh, "查询成功");
    }


    /**
     * @Description 活动里操作某个用户的分数，加或者减     活动key  成员id   分数(正增加负减少)
     * @param key
     * @param member
     * @param score
     * @Return java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple<java.lang.String>>
     * @Author wangruwei
     * @Date 2022/3/3 17:56
     **/
    @RequestMapping(value = "incrementScore")
    public Map<String, Object> incrementScore(String key, String member, double score ) {
        double hh = redisZsetService.incrementScore(key,member,score);
        return getSussRtn(hh, "操作成功");
    }

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
    public static void main(String[] args) {
        String a = "2022-03-07 12:12:12 123";

        Date kaishi = DateUtils.stringToDate(a, DATE_TIME_PATTERN);
        System.out.println("活动开始日期:"+kaishi.getTime());

        String b = "2022-04-07 12:12:12 123";
        Date jieshu = DateUtils.stringToDate(b, DATE_TIME_PATTERN);
        System.out.println("活动结束日期:"+jieshu.getTime());

        String c = "2022-03-17 12:12:12 123";
        Date zhongjian = DateUtils.stringToDate(c, DATE_TIME_PATTERN);
        System.out.println("活动中间日期:"+zhongjian.getTime());
    }

}
