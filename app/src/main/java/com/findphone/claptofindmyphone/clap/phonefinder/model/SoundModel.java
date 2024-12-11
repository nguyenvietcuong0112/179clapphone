package com.findphone.claptofindmyphone.clap.phonefinder.model;

import android.media.MediaPlayer;

public class SoundModel {
    public int imageSound;
    public MediaPlayer sound;
    public Boolean isCheck;
    public String isoSound;

    public String getIsoSound() {
        return isoSound;
    }

    public void setIsoSound(String isoSound) {
        this.isoSound = isoSound;
    }

    public SoundModel(int imageSound, MediaPlayer sound, Boolean isCheck, String isoSound) {
        this.imageSound = imageSound;
        this.sound = sound;
        this.isCheck = isCheck;
        this.isoSound = isoSound;
    }

    public int getImageSound() {
        return imageSound;
    }
    public void setImageSound(int imageSound) {
        this.imageSound = imageSound;
    }
    public void setSound(MediaPlayer sound) {
        this.sound = sound;
    }

    public MediaPlayer getSound() {
        return sound;
    }

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }

}
