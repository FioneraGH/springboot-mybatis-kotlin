package com.fionera.test.domain;

import com.fionera.test.mapper.ActivityMapper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.util.Date;

/**
 * ActivityTest
 * Created by fionera on 17-5-17.
 */
public class ActivityTest {

    private static SqlSessionFactory sqlSessionFactory;

    @Before
    public void setUp() {
        try {
            Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ActivityMapper activityMapper = sqlSession.getMapper(ActivityMapper.class);
        Activity activity = activityMapper.selectById(10);
        if (activity == null) {
            return;
        }
        sqlSession.commit();
        Assert.assertEquals("查找成功", "爱美丽", activity.getTitle());
        sqlSession.close();
    }

    @Test
    public void insert() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ActivityMapper activityMapper = sqlSession.getMapper(ActivityMapper.class);
        Activity activity = new Activity();
        activity.setTitle("测试插入");
        activity.setNote("插入数据");
        activity.setImgPath("https://www.baidu.com/");
        activity.setStartTime(new Date());
        activity.setEndTime(new Date());
        activityMapper.insert(activity);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void update() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ActivityMapper activityMapper = sqlSession.getMapper(ActivityMapper.class);
        Activity activity = activityMapper.selectById(10);
        if (activity == null) {
            return;
        }
        activity.setTitle("测试更新");
        activity.setNote("更新数据");
        activity.setStartTime(new Date());
        activity.setEndTime(new Date());
        activityMapper.update(activity);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void delete() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ActivityMapper activityMapper = sqlSession.getMapper(ActivityMapper.class);
        activityMapper.deleteById(10);
        sqlSession.commit();
        sqlSession.close();
    }
}
