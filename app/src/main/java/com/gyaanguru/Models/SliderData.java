package com.gyaanguru.Models;

public class SliderData {
    private int ImgId;

    // Constructor
    public SliderData(int ImgId) {
        this.ImgId = ImgId;
    }

    // Default constructor
    public SliderData() {
    }

    // Getter method.
    public int getImgId() {
        return ImgId;
    }

    // Setter method.
    public void setImgId(int imgId) {
        this.ImgId = imgId;
    }
}
