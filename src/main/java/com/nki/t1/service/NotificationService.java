package com.nki.t1.service;

import com.nki.t1.dao.NotificationDao;
import com.nki.t1.dao.PostDao;
import com.nki.t1.domain.NotificationType;
import com.nki.t1.dto.CommentDto;
import com.nki.t1.dto.NotificationDto;
import com.nki.t1.dto.PostDto;
import com.nki.t1.dto.UserSecurityDto;
import com.nki.t1.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NotificationService {

    private final NotificationDao notificationDao;
    private final SessionUtils sessionUtils;
    private final SimpMessagingTemplate messagingTemplate;
    private final PostDao postDao;

    public NotificationService(NotificationDao notificationDao, SimpMessagingTemplate messagingTemplate, SessionUtils sessionUtils, PostDao postDao) {
        this.notificationDao = notificationDao;
        this.messagingTemplate = messagingTemplate;
        this.sessionUtils = sessionUtils;
        this.postDao = postDao;
    }

    public boolean notifyNewComment(UserSecurityDto postWriterSecurityDto, CommentDto commentDto) {
        Integer commentWriterUno = sessionUtils.getUserSecurityDto().getUno();
        if (commentWriterUno == postWriterSecurityDto.getUno()) {
            log.info("----- commentWriterUno eqauls postWriterUno");
            return false;
        }
        PostDto postDto = postDao.selectPost(commentDto.getPno());
        if (postDto != null) {
            messagingTemplate.convertAndSendToUser(postWriterSecurityDto.getUsername(), "/queue/newComment",
                    "새로운 댓글: " + commentDto.getComment() + " (게시글: " + postDto.getTitle() + ")");

            notificationDao.insertNotification(new NotificationDto.Builder().uno(postWriterSecurityDto.getUno()).type(NotificationType.NEW_COMMENT_TO_POST).targetParentIdx(commentDto.getPno()).targetChildIdx(commentDto.getCno()).build());
            showNotificationMark(postWriterSecurityDto);
        }
        return true;
    }


    // 종을 누르면
    public List<NotificationDto> getNotificationList() {
        int uno = sessionUtils.getUserSecurityDto().getUno();
        // uno의 알림을 모두 가져온다.
        List<NotificationDto> notificationDtoList = notificationDao.selectUnreadNotificationListByUno(uno);

        // uno의 알림을 모두 read 로 변경한다.
        if (notificationDtoList.size() > 0) {
            notificationDao.updateToReadNotificationsByUno(uno);
        }

        return notificationDtoList;
    }

    public void showNotificationMark(UserSecurityDto userSecurityDto) { // 새글이 등록되었을때 혹은 로그인 성공시 호출
        int count = getNotificationCount(userSecurityDto);
        log.info("----- showNotificationMark userSecurityDto = {}", userSecurityDto);
        log.info("----- showNotificationMark count = {}", count);
        messagingTemplate.convertAndSendToUser(userSecurityDto.getUsername(), "/queue/newComment/count", count);
    }

    public int getNotificationCount(UserSecurityDto userSecurityDto) {
        int uno = userSecurityDto.getUno();
        int count = notificationDao.countUnreadNotificationsByUno(uno);

        log.info("----- getNotificationCount userSecurityDto = {}", userSecurityDto);
        log.info("----- getNotificationCount count = {}", count);

        // del = 0 이고 read 가 false 인 uno의 알림을 카운트 한다.
        return count;
    }

    public NotificationDto readNotification(int notificationId) {
        NotificationDto notificationDto = notificationDao.selectNotificationById(notificationId);
        notificationDao.deleteNotificationsByTypeAndPIdx(notificationDto);

        return notificationDto;
    }

}
