package com.dxhy.order.controller;

import com.dxhy.order.service.RedisService;
import com.dxhy.order.service.RedisZsetService;
import com.dxhy.order.util.DateUtils;
import com.dxhy.order.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
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
@RequestMapping("/zsetJq")
public class RedisZsetJqController extends BaseController{

    @Autowired
    private RedisZsetService redisZsetService;

    @Autowired
    private RedisService redisService;

    public static final String SECONDS = "_seconds";

    public static final String HUNDREDS_MS = "_hundreds_ms";

    public static final String TEN_MS = "_ten_ms";

    public static final String MS = "_ms";

    public static final String JINGDU = "_jingdu";

    public static final String TYPE = "_type";

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
        String jingLength = redisService.get(key + JINGDU);
        log.info("获取到的数据:{}", JsonUtils.getInstance().toJsonString(hh));
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("shuju", hh);
        result.put("itemJingdu", jingLength.split("_")[0]);
        if(!CollectionUtils.isEmpty(result)){
            return getSussRtn(result, "获取成功");
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
        String jingLength = redisService.get(key + JINGDU);
        log.info("获取到的数据:{}", JsonUtils.getInstance().toJsonString(hh));
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("shuju", hh);
        result.put("itemJingdu", jingLength.split("_")[0]);
        if(!CollectionUtils.isEmpty(result)){
            return getSussRtn(result, "获取成功");
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
        String jingLength = redisService.get(key + JINGDU);
        log.info("获取到的数据:{}", JsonUtils.getInstance().toJsonString(hh));
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("shuju", hh);
        result.put("itemJingdu", jingLength.split("_")[0]);
        if(!CollectionUtils.isEmpty(result)){
            return getSussRtn(result, "获取成功");
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
        String jingLength = redisService.get(key + JINGDU);
        log.info("获取到的数据:{}", JsonUtils.getInstance().toJsonString(hh));
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("shuju", hh);
        result.put("itemJingdu", jingLength.split("_")[0]);
        if(!CollectionUtils.isEmpty(result)){
            return getSussRtn(result, "获取成功");
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
        String jingLength = redisService.get(key + JINGDU);
        Long jingduContants = Long.valueOf(jingLength.split("_")[1]);
        Integer jingduLength = Integer.valueOf(jingLength.split("_")[0]);
        //先判断现在属于哪种精度
        String jingType = jingLength.split("_")[2];
        String nowLong = System.currentTimeMillis()+"";
        if("秒".equals(jingType)){
            nowLong = nowLong.substring(0,nowLong.length()-3);
        }else if("百毫秒".equals(jingType)){
            nowLong = nowLong.substring(0,nowLong.length()-2);
        }else if("十毫秒".equals(jingType)){
            nowLong = nowLong.substring(0,nowLong.length()-1);
        }else if("个毫秒".equals(jingType)){

        }else{
            return getFailRtn("没有此精度");
        }
        nowLong = nowLong.substring(nowLong.length()-jingduLength);

        //应该插入score后的时间戳
        String appendTime = (jingduContants-Long.valueOf(nowLong))+"";
        //如果要补的数据比时间戳长度小，补零
        if(appendTime.length()<jingduContants.toString().length()){
            for (int i = 0; i < jingduContants.toString().length() - appendTime.length(); i++) {
                appendTime = appendTime + "0";
            }
        }

        log.info("分数后面的时间戳:{}"+appendTime);

        score = score + Double.valueOf(appendTime);

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

        String jingLength = redisService.get(key + JINGDU);
        Long jingduContants = Long.valueOf(jingLength.split("_")[1]);
        Integer jingduLength = Integer.valueOf(jingLength.split("_")[0]);
        //先判断现在属于哪种精度
        String jingType = jingLength.split("_")[2];
        String nowLong = System.currentTimeMillis()+"";
        if("秒".equals(jingType)){
            nowLong = nowLong.substring(0,nowLong.length()-3);
        }else if("百毫秒".equals(jingType)){
            nowLong = nowLong.substring(0,nowLong.length()-2);
        }else if("十毫秒".equals(jingType)){
            nowLong = nowLong.substring(0,nowLong.length()-1);
        }else if("个毫秒".equals(jingType)){

        }else{
            return getFailRtn("没有此精度");
        }
        nowLong = nowLong.substring(nowLong.length()-jingduLength);

        //应该插入score后的时间戳
        String appendTime = (jingduContants-Long.valueOf(nowLong))+"";
        //如果要补的数据比时间戳长度小，补零
        if(appendTime.length()<jingduContants.toString().length()){
            for (int i = 0; i < jingduContants.toString().length() - appendTime.length(); i++) {
                appendTime = appendTime + "0";
            }
        }

        log.info("分数后面的时间戳:{}"+appendTime);

        score = score*Math.pow(10, jingduLength) + Double.valueOf(appendTime);

        double hh = redisZsetService.incrementScore(key,member,score);
        return getSussRtn(hh, "操作成功");
    }

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
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


    /**
     * @Description 获取时间范围内常数
     * @param key
     * @param startDate
     * @param endDate
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     * @Author wangruwei
     * @Date 2022/3/7 17:12
     **/
    @RequestMapping(value = "checkTimeConstant")
    public Map<String, Object> checkTimeConstant(String key,String startDate,String endDate ) {

        if(startDate.length()==10){
            startDate = startDate + " 00:00:00";
        }
        Date kaishi = DateUtils.stringToDate(startDate, DATE_TIME_PATTERN);
        log.info("开始时间:{}", kaishi.getTime());

        if(endDate.length()==10){
            endDate = endDate + " 23:59:59";
        }
        Date jieshu = DateUtils.stringToDate(endDate, DATE_TIME_PATTERN);
        log.info("结束时间:{}", jieshu.getTime());

        Map<String,Object> result = new HashMap<String,Object>();
        result.put("kaishi", kaishi.getTime());
        result.put("jieshu", jieshu.getTime());
        String kaishiStr = kaishi.getTime()+"";
        String jieshuStr = jieshu.getTime()+"";
        String changshu = "";
        for (int i = 0; i < kaishiStr.length(); i++) {
            char c = kaishiStr.charAt(i);
            System.out.println(c);
            if(c==jieshuStr.charAt(i)){
                //System.out.println("相等");
            }else{
                //System.out.println("不等"+c);
                //System.out.println(jieshuStr.substring(i));
                changshu = jieshuStr.substring(i);
                result.put("changshu", changshu);
                break;
            }
        }

        if(!CollectionUtils.isEmpty(result)){
            return getSussRtn(result, "获取成功");
        }else{
            return getFailRtn("获取失败");
        }
    }


    /**
    * @Description 设置精确度   1秒  2百毫秒  3十毫秒  4个毫秒
    * @param key
    * @param type
    * @Return java.util.Map<java.lang.String,java.lang.Object>
    * @Author wangruwei
    * @Date 2022/3/7 18:00
    **/
    @RequestMapping(value = "jingque")
    public Map<String, Object> jingque(String key,String type ,String changshuValue ) {
        String nowType = "";
        //秒
        if("1".equals(type)){
            nowType="秒";
            changshuValue = changshuValue.substring(0,changshuValue.length()-3);
            key = key+SECONDS;
        }else if("2".equals(type)){
            nowType="百毫秒";
            changshuValue = changshuValue.substring(0,changshuValue.length()-2);
            key = key+HUNDREDS_MS;
        }else if("3".equals(type)){
            nowType="十毫秒";
            changshuValue = changshuValue.substring(0,changshuValue.length()-1);
            key = key+TEN_MS;
        }else if("4".equals(type)){
            nowType="个毫秒";
            key = key+MS;
        }else{
            nowType="没有这个分类";
        }

        redisService.set(key+JINGDU, changshuValue.length()+"_"+changshuValue+"_"+nowType);

        Map<String,Object> result = new HashMap<String,Object>();

        result.put("nowType", nowType);
        result.put("changshuValue", changshuValue);
        result.put("key", key);

        if(!CollectionUtils.isEmpty(result)){
            return getSussRtn(result, "获取成功");
        }else{
            return getFailRtn("获取失败");
        }
    }
}
