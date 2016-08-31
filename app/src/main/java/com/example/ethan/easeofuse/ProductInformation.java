package com.example.ethan.easeofuse;

public class ProductInformation {
    private String imageUrl;
    private String title;
    private String description;
    private String link;
    private String price;
    private String recommendation;

    public ProductInformation(String title, String description){
        this.title = title;
        this.description = description;
    }

    public ProductInformation(String imageUrl, String title, String description, String link, String price, String recommendation){
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.link = link;
        this.price = price;
        this.recommendation = recommendation;
    }

    public String getImageUrl(){ return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLink(){ return link; }
    public void setLink(String link) { this.link = link; }
    public String getPrice(){ return price; }
    public void setPrice(String price) { this.price = price; }
    public String getRecommendation(){ return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
}
