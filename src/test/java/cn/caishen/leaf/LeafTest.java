package cn.caishen.leaf;

import cn.caishen.leaf.constant.LeafConstant;
import cn.caishen.leaf.service.LeafServiceImpl;
import cn.caishen.leaf.thread.GetNextIdThread;
import cn.caishen.leaf.utils.SnowflakeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = LeafApplication.class)
@RunWith(SpringRunner.class)
public class LeafTest {

    @Resource
    private LeafServiceImpl leafService;

    @Resource
    private GetNextIdThread getNextIdThread;

    /**
     * 使用redis进行缓存，缓存内容为 LbMap
     */
    @Test
    public void testNextId(){
        long begin = System.currentTimeMillis();
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            long nextId = leafService.getNextId(LeafConstant.BusinessType.BUSINESS_SMDOC);
            System.out.println("获取流水号["+nextId+"]");
            list.add(nextId);
        }
        long end = System.currentTimeMillis();
        long diff = end - begin;
        System.out.println("redis缓存用时：" + diff + "|生成id："+list.size());
    }

    /**
     * 使用redis进行缓存，缓存内容变更为 Map<String, Object>
     */
    @Test
    public void testNextId2(){
        long begin = System.currentTimeMillis();
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            long nextId = leafService.getNextId2(LeafConstant.BusinessType.BUSINESS_SMDOC);
            System.out.println("获取流水号["+nextId+"]");
            list.add(nextId);
        }
        long end = System.currentTimeMillis();
        long diff = end - begin;
        System.out.println("redis缓存用时：" + diff + "|生成id："+list.size());
    }


    /**
     * 使用本地缓存，咖啡因
     */
    @Test
    public void testNextId3(){
        long begin = System.currentTimeMillis();
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            long nextId = leafService.getNextId3(LeafConstant.BusinessType.BUSINESS_SMDOC);
            System.out.println("获取流水号["+nextId+"]");
            list.add(nextId);
        }
        long end = System.currentTimeMillis();
        long diff = end - begin;
        System.out.println("咖啡因缓存用时：" + diff + "|生成id："+list.size());
    }


    /**
     * 雪花算法
     */
    @Test
    public void testSnowNextId(){
        long begin = System.currentTimeMillis();
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            long nextId = SnowflakeUtils.getNextId();
            System.out.println("获取流水号["+nextId+"]");
            list.add(nextId);
        }
        long end = System.currentTimeMillis();
        long diff = end - begin;
        System.out.println("雪花用时：" + diff + "|生成id："+list.size());
    }
}
