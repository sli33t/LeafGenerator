package cn.caishen.leaf.service;

import cn.caishen.leaf.constant.LeafConstant;
import cn.caishen.leaf.dao.LeafDao;
import cn.caishen.leaf.thread.UpdateMaxIdThread;
import cn.caishen.leaf.utils.LbMap;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author LB
 */
@Service
@Slf4j
public class LeafServiceImpl {

    @Resource
    private LeafDao leafDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UpdateMaxIdThread updateMaxIdThread;

    private Lock lock = new ReentrantLock(true);

    @Autowired
    private Cache<String, Object> caffeineCache;

    /**
     * 使用redis进行缓存，缓存内容为 LbMap
     * @param businessType
     * @return
     */
    @SneakyThrows
    public long getNextId(int businessType){
        lock.lock();
        try {
            LbMap cacheMap_one = (LbMap) redisTemplate.opsForValue().get(LeafConstant.KEY_ONE);
            if (Objects.isNull(cacheMap_one)) {
                LbMap idMap = leafDao.getNextId(businessType);
                long maxId = idMap.getLong("maxId");
                //当前ID
                idMap.put("thisId", maxId + 1);
                redisTemplate.opsForValue().set(LeafConstant.KEY_ONE, idMap);
                return maxId;
            } else {
                long maxId = cacheMap_one.getLong("maxId");
                int step = cacheMap_one.getInt("step");
                long thisId = cacheMap_one.getLong("thisId");

                //判断是否获取第二段
                if ((thisId - maxId) * 10 / step == 1 && (thisId - maxId) * 10 % step == 0) {
                    //log.info("这里获取第二号段：[thisId]" + thisId + "；[maxId]" + maxId);
                    LbMap cacheMap_two = (LbMap) redisTemplate.opsForValue().get(LeafConstant.KEY_TWO);
                    if (Objects.isNull(cacheMap_two)) {
                        //写入第二段
                        updateMaxIdThread.updateMaxId(businessType);
                    }
                }

                //判断第一段是否用完
                if (maxId + step == thisId) {
                    LbMap cacheMap_two = (LbMap) redisTemplate.opsForValue().get(LeafConstant.KEY_TWO);

                    maxId = cacheMap_two.getLong("maxId");
                    cacheMap_two.put("thisId", maxId + 1);

                    redisTemplate.opsForValue().set(LeafConstant.KEY_ONE, cacheMap_two);
                    redisTemplate.delete(LeafConstant.KEY_TWO);

                    return maxId;
                }

                cacheMap_one.put("thisId", thisId + 1);
                redisTemplate.opsForValue().set(LeafConstant.KEY_ONE, cacheMap_one);
                return thisId;
            }
        }catch (Exception e){
            throw new Exception("生成流水号时出错，"+e.getMessage());
        }finally {
            lock.unlock();
        }
    }

    /*@SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void updateMaxId(int businessType){
        leafDao.updateMaxId(businessType);
        LbMap nextMap = leafDao.getNextId(businessType);
        redisTemplate.opsForValue().set(LeafConstant.KEY_TWO, nextMap);
    }*/


    /**
     * 使用redis进行缓存，缓存内容为 Map<String, Object>
     * @param businessType
     * @return
     */
    @SneakyThrows
    public long getNextId2(int businessType){
        lock.lock();
        try {
            Map<String, Object> cacheMap_one = (Map<String, Object>) redisTemplate.opsForValue().get(LeafConstant.KEY_ONE);
            if (Objects.isNull(cacheMap_one)) {
                Map<String, Object> idMap = leafDao.getNextId(businessType);
                long maxId = (long) idMap.get("maxId");
                //当前ID
                idMap.put("thisId", maxId + 1);
                redisTemplate.opsForValue().set(LeafConstant.KEY_ONE, idMap);
                return maxId;
            } else {
                long maxId = (long) cacheMap_one.get("maxId");
                int step = (int) cacheMap_one.get("step");
                long thisId = (long) cacheMap_one.get("thisId");

                //判断是否获取第二段
                if ((thisId - maxId) * 10 / step == 1 && (thisId - maxId) * 10 % step == 0) {
                    //log.info("这里获取第二号段：[thisId]" + thisId + "；[maxId]" + maxId);
                    Map<String, Object> cacheMap_two = (Map<String, Object>) redisTemplate.opsForValue().get(LeafConstant.KEY_TWO);
                    if (Objects.isNull(cacheMap_two)) {
                        //写入第二段
                        updateMaxIdThread.updateMaxId(businessType);
                    }
                }

                //判断第一段是否用完
                if (maxId + step == thisId) {
                    Map<String, Object> cacheMap_two = (Map<String, Object>) redisTemplate.opsForValue().get(LeafConstant.KEY_TWO);

                    maxId = (long) cacheMap_two.get("maxId");
                    cacheMap_two.put("thisId", maxId + 1);

                    redisTemplate.opsForValue().set(LeafConstant.KEY_ONE, cacheMap_two);
                    redisTemplate.delete(LeafConstant.KEY_TWO);

                    return maxId;
                }

                cacheMap_one.put("thisId", thisId + 1);
                redisTemplate.opsForValue().set(LeafConstant.KEY_ONE, cacheMap_one);
                return thisId;
            }
        }catch (Exception e){
            throw new Exception("生成流水号时出错，"+e.getMessage());
        }finally {
            lock.unlock();
        }
    }

    /**
     * 使用本地缓存，咖啡因
     * @param businessType
     * @return
     */
    @SneakyThrows
    public long getNextId3(int businessType){
        lock.lock();
        try {
            LbMap cacheMap_one = (LbMap) caffeineCache.asMap().get(LeafConstant.KEY_ONE);
            if (Objects.isNull(cacheMap_one)) {
                LbMap idMap = leafDao.getNextId(businessType);
                long maxId = idMap.getLong("maxId");
                //当前ID
                idMap.put("thisId", maxId + 1);
                caffeineCache.put(LeafConstant.KEY_ONE, idMap);
                return maxId;
            } else {
                long maxId = cacheMap_one.getLong("maxId");
                int step = cacheMap_one.getInt("step");
                long thisId = cacheMap_one.getLong("thisId");

                //判断是否获取第二段
                if ((thisId - maxId) * 10 / step == 1 && (thisId - maxId) * 10 % step == 0) {
                    //log.info("这里获取第二号段：[thisId]" + thisId + "；[maxId]" + maxId);
                    LbMap cacheMap_two = (LbMap) caffeineCache.asMap().get(LeafConstant.KEY_TWO);
                    if (Objects.isNull(cacheMap_two)) {
                        //写入第二段，这里如果是异步开启线程的话，那么线程还没有执行，号段就已经用完了，会导致取 key_two 为null
                        //updateMaxIdThread.updateMaxId3(businessType);
                        leafDao.updateMaxId(businessType);
                        LbMap nextMap = leafDao.getNextId(businessType);
                        caffeineCache.put(LeafConstant.KEY_TWO, nextMap);
                    }
                }

                //判断第一段是否用完
                if (maxId + step == thisId) {
                    LbMap cacheMap_two = (LbMap) caffeineCache.asMap().get(LeafConstant.KEY_TWO);

                    maxId = cacheMap_two.getLong("maxId");
                    cacheMap_two.put("thisId", maxId + 1);

                    caffeineCache.put(LeafConstant.KEY_ONE, cacheMap_two);
                    caffeineCache.asMap().remove(LeafConstant.KEY_TWO);

                    return maxId;
                }

                cacheMap_one.put("thisId", thisId + 1);
                caffeineCache.put(LeafConstant.KEY_ONE, cacheMap_one);
                return thisId;
            }
        }catch (Exception e){
            throw new Exception("生成流水号时出错，"+e.getMessage());
        }finally {
            lock.unlock();
        }
    }
}
