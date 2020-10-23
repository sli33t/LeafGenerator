package cn.caishen.leaf.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 为了防止重复，创建一个单例
 * @author LB
 */
public class SnowflakeUtils {

    /**
     * 创建一个私有方法，防止实例化
     */
    private SnowflakeUtils(){

    }

    /**
     * 创建一个内部类，维护单例
     */
    private static class SnowflakeFactory {
        private static final Snowflake snowflake = IdUtil.getSnowflake(1, 1);
    }

    /**
     * 获取实例，返回nextId
     * @return long型的id
     */
    public static long getNextId(){
        Snowflake snowflake = SnowflakeFactory.snowflake;
        return snowflake.nextId();
    }

    /**
     * 返回字符串型的nextId
     *      如果第一次获取为0，那么再获取一次id
     * @return String型的id
     */
    public static String getNextIdStr(){
        long id = getNextId();
        if (id<=0){
            //如果ID小于等于0，则再调用一次
            id = getNextId();
        }
        return String.valueOf(id);
    }

    /**
     * 获取当前时间 + 雪花算法为nextId
     *      时间格式为: yyyyMMddHHmm
     * @return 当前时间 + nextId
     */
    public static String getTimeId(){
        DateTimeFormatter MILLISECOND = DateTimeFormatter.ofPattern("yyyyMMdd");
        Snowflake snowflake = SnowflakeFactory.snowflake;
        return LocalDateTime.now().format(MILLISECOND) + snowflake.nextId();
    }

    /**
     * 生成随机数
     * @param decimals 位数，传入16则生成16位，传入32则生成32位
     * @return 随机数字符串
     */
    public static String getRadomId(int decimals){
        if (decimals<=0){
            //默认为32位
            decimals = 32;
        }

        StringBuilder sb = new StringBuilder();
        //产生32位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < decimals; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type){
                case 0:
                    //0-9的随机数
                    sb.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    sb.append((char)(rd.nextInt(25)+65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    sb.append((char)(rd.nextInt(25)+97));
                    break;
                default:
                    break;
            }
        }

        return sb.toString();
    }

    /**
     * 生成16位随机数
     * @return 随机数字符串
     */
    public static String getRadomId16(){
        return getRadomId(16);
    }

    /**
     * 生成32位随机数
     * @return 随机数字符串
     */
    public static String getRadomId32(){
        return getRadomId(32);
    }

    /**
     * 生成单据号
     * @param prefix 前缀
     * @return 单据号
     */
    public static String createNo(String prefix){
        return prefix + getNextIdStr();
    }
}
