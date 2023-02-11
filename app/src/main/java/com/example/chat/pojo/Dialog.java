package com.example.chat.pojo;

/**
 * 在消息界面 B_Tab4_Fragment 的消息列表中的 item
 */
public class Dialog {
    int postId;
    String from; // 对方的uid
    String to; // 自己的uid
    String message;

    public Dialog() {
    }

    public Dialog(Dialog dialog) {
        this.postId = dialog.getPostId();
        this.from = dialog.getFrom();
        this.to = dialog.getTo();
        this.message = dialog.getMessage();
    }

    public Dialog(int postId, String from, String to, String message) {
        this.postId = postId;
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
