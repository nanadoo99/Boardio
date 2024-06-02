package com.nki.t1.dao;

import com.nki.t1.dto.CommentDto;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context_test.xml"})
public class CommentDaoImplTest extends TestCase {
    @Autowired
    private CommentDao commentDao;

    private int pno = 1;
    private int uno = 1;

    @Test
    public void testInsertComment() throws Exception {
        commentDao.deleteAll(pno);
        List<CommentDto> list = new ArrayList<CommentDto>();
        int cnt = 20;

        for(int i=1; i<=cnt; i++) {
            CommentDto comment = new CommentDto(pno, uno, "comment_test_" + i);
            int result = commentDao.insertComment(comment);
            assertEquals(1, result);
            list.add(comment);
        }
        assertEquals(cnt, list.size());
    }

    @Test
    public void testCountComments() throws Exception {
        commentDao.deleteAll(pno);
        List<CommentDto> list = new ArrayList<CommentDto>();
        int cnt = 20;

        for(int i=1; i<=cnt; i++) {
            CommentDto comment = new CommentDto(pno, uno, "comment_test_" + i);
            int result = commentDao.insertComment(comment);
        }
        int result = commentDao.countComments(pno);
        assertEquals(cnt, result);
    }

    @Test
    public void testSeleteComments() throws Exception {
        commentDao.deleteAll(pno);
        int cnt = 20;

        for(int i=1; i<=cnt; i++) {
            CommentDto comment = new CommentDto(pno, uno, "comment_test_" + i);
            int result = commentDao.insertComment(comment);
            assertEquals(1, result);
        }
        List<CommentDto> list = commentDao.selectComments(pno);
        assertEquals(cnt, list.size());
    }

    @Test
    public void testUpdateComment() throws Exception {
        commentDao.deleteAll(pno);
        CommentDto comment = new CommentDto(pno, uno, "comment_test");

        int result1 = commentDao.insertComment(comment);
        assertEquals(1, result1);

        comment.setComment("updated");
        int result2 = commentDao.updateComment(comment);
        assertEquals(1, result2);

    }

    @Test
    public void testDeleteComment() throws Exception {
        commentDao.deleteAll(pno);
        CommentDto comment = new CommentDto(pno, uno, "comment_test");
        int result1 = commentDao.insertComment(comment);
        assertEquals(1, result1);

        Map<String, Object> map = new HashMap<>();
        map.put("cno", comment.getCno());
        map.put("uno", uno);
        int result2 = commentDao.deleteComment(map);;
        assertEquals(1, result2);
    }

}