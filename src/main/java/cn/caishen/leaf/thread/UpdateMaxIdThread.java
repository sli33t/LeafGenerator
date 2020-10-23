package cn.caishen.leaf.thread;

import cn.caishen.leaf.constant.LeafConstant;
import cn.caishen.leaf.dao.LeafDao;
import cn.caishen.leaf.utils.LbMap;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 开启更新线程
 * @author LB
 */
@Component
public class UpdateMaxIdThread {

    @Autowired
    private LeafDao leafDao;

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private Cache<String, Object> caffeineCache;

    @Async
    @SneakyThrows
    public void updateMaxId(int businessType){
        leafDao.updateMaxId(businessType);
        LbMap nextMap = leafDao.getNextId(businessType);
        redisTemplate.opsForValue().set(LeafConstant.KEY_TWO, nextMap);
    }

    @Async
    @SneakyThrows
    public void updateMaxId3(int businessType){
        leafDao.updateMaxId(businessType);
        LbMap nextMap = leafDao.getNextId(businessType);
        caffeineCache.put(LeafConstant.KEY_TWO, nextMap);
    }
}
