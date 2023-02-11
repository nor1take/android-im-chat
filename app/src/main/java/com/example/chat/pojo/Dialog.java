package com.example.chat.pojo;

public class Dialog {
    int postId;
    String uidUser; // 对方的uid
    String message;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Dialog(int postId, String uidUser, String message) {
        this.postId = postId;
        this.uidUser = uidUser;
        this.message = message;
    }
}
