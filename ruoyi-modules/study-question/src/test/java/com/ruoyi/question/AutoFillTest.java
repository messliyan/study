package com.ruoyi.question;

import com.ruoyi.question.domain.QueStore;
import com.ruoyi.question.mapper.QueStoreMapper;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 自动填充测试
 *
 * @author nieqiurong 2018-08-10 23:47:02.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AutoFillTest {

    @Resource
    private QueStoreMapper queStoreMapper;

    @Test
    public void test() {
        QueStore queStore = new QueStore();
        queStore.setStoreType("good");
        queStoreMapper.insert(queStore);
        log.info("query user:{}", queStoreMapper.selectById(queStore.getStoreId()));
    }
}