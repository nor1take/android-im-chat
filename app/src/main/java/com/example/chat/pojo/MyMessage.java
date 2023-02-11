package com.example.chat.pojo;

import java.util.Date;

/**
 * 从数据库传来的 json
 */
public class MyMessage {
    private Integer id;
    private Integer senderId;
    private String message;
    private Integer receiverId;
    private Date sendTime;
    private Integer postId;
    private String chatGroup;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", message='" + message + '\'' +
                ", receiverId=" + receiverId +
                ", sendTime=" + sendTime +
                ", postId=" + postId +
                ", chatGroup='" + chatGroup + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(String chatGroup) {
        this.chatGroup = chatGroup;
    }
}
