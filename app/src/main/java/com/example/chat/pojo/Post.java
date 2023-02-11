package com.example.chat.pojo;

import java.util.Date;



public class Post {
    private Integer id;
    private Integer poster;
    private String label;
    private Integer peopleNum;
    private String body;
    private Date time;

    @Override
    public String toString() {

        return "Post{" +
                "id=" + id +
                ", poster=" + poster +
                ", label='" + label + '\'' +
                ", peopleNum=" + peopleNum +
                ", body='" + body + '\'' +
                ", time=" + time +
                '}';
    }

    public Post(){

    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPoster() {
        return poster;
    }

    public void setPoster(Integer poster) {
        this.poster = poster;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


}
