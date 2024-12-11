package com.findphone.claptofindmyphone.clap.phonefinder.model;

public class HelpGuidModel {
    public int img;
//    public String content;
    public String titleGuide;

    public HelpGuidModel(int img, String titleGuide) {
        this.img = img;
//        this.content = content;
        this.titleGuide = titleGuide;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

//    public String getContent() {
//        return content;
//    }
    public String getTitleGuide() {
        return titleGuide;
    }

//    public void setContent(String content) {
//        this.content = content;
//    }
    public void setTitleGuide(String content) {
        this.titleGuide = content;
    }
}