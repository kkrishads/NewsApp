package com.dreamworks.newsapp;

public class NewsItems {
    private String CatId;
    private String NewsDate;
    private String NewsHeading;
    private String NewsImage;
    private String NewsPaper;
    private String newsUrl;
    private String imgUrl;
    private String newsDetails;


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNewsDetails() {
        return newsDetails;
    }

    public void setNewsDetails(String newsDetails) {
        this.newsDetails = newsDetails;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getCatId() {
        return this.CatId;
    }

    public void setCatId(String catid) {
        this.CatId = catid;
    }

    public String getNewsHeading() {
        return this.NewsHeading;
    }

    public void setNewsHeading(String newsheading) {
        this.NewsHeading = newsheading;
    }

    public String getNewsImage() {
        return this.NewsImage;
    }

    public void setNewsImage(String newsimage) {
        this.NewsImage = newsimage;
    }

    public String getNewsDate() {
        return this.NewsDate;
    }

    public void setNewsDate(String newsdate) {
        this.NewsDate = newsdate;
    }

    public String getNewsPaper() {
        return this.NewsPaper;
    }

    public void setNewsPaper(String newspaper) {
        this.NewsPaper = newspaper;
    }
}
