package com.example.chat.pojo;


import java.util.List;


public class HotList {
    private String label;
    private String hot1;
    private String hot2;
    private String hot3;
    private List<String> hotList;


    public HotList(String label, String hot1, String hot2, String hot3) {
        this.label = label;
        this.hot1 = hot1;
        this.hot2 = hot2;
        this.hot3 = hot3;
    }

    public HotList(String label, List<String> hotList) {
        this.label = label;
        this.hotList = hotList;
    }

    public String getLabel() {
        return label;
    }

    public String getHot1() {
        return hot1;
    }

    public String getHot2() {
        return hot2;
    }

    public String getHot3() {
        return hot3;
    }

    public List<String> getHotList() {
        return hotList;
    }
}
