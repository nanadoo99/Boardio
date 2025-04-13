package com.nki.t1.dao;

import com.nki.t1.dto.NotificationDto;
import com.nki.t1.dto.VisitorDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationDao {
    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.nki.t1.dao.NotificationsMapper.";

    public void insertNotification(NotificationDto notificationDto) {
        sqlSession.insert(namespace + "insertNotification", notificationDto);
    }

    public List<NotificationDto> selectUnreadNotificationListByUno(int uno) {
        return sqlSession.selectList(namespace + "selectUnreadNotificationListByUno");
    }

    public int countUnreadNotificationsByUno(int uno) {
        return sqlSession.selectOne(namespace + "countUnreadNotificationsByUno", uno);
    }

    public NotificationDto selectNotificationById(int notificationId) {
        return sqlSession.selectOne(namespace + "selectNotificationById", notificationId);
    }

    public void updateToReadNotificationsByUno(int uno) {
        sqlSession.update(namespace + "updateToReadNotificationsByUno", uno);
    }

    public void deleteNotificationsById(int id) {
        sqlSession.delete(namespace + "deleteNotificationById", id);
    }

    public void deleteNotificationsByTypeAndPIdx(NotificationDto notificationDto) {
        sqlSession.delete(namespace + "deleteNotificationsByTypeAndPIdx", notificationDto);
    }
}
