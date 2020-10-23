package cn.caishen.leaf.thread;

import cn.caishen.leaf.constant.LeafConstant;
import cn.caishen.leaf.service.LeafServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试线程
 * @author LB
 */
@Component
@Slf4j
public class GetNextIdThread implements Runnable {

    @Autowired
    private LeafServiceImpl leafService;

    @Override
    public void run() {
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < 90; i++) {
            long nextId = leafService.getNextId(LeafConstant.BusinessType.BUSINESS_SMDOC);
            list.add(nextId);
        }
        String name = Thread.currentThread().getName();
        log.info("线程["+name+"]生成ID："+list.toString());
    }
}
