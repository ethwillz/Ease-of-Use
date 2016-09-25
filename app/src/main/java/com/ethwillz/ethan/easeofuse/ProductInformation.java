package com.ethwillz.ethan.easeofuse;

public class ProductInformation {
    private String imageUrl;
    private String title;
    private String description;
    private String link;
    private String price;
    private String recommendation;
    private String type;
    private String style;

    public ProductInformation(String imageUrl, String title, String description, String link, String price, String recommendation, String type, String style){
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.link = link;
        this.price = price;
        this.recommendation = recommendation;
        this.type = type;
        this.style = style;
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

    public String getStyle(){ return style; }
    public void setStyle(String style) { this.style = style; }

    public String getType(){ return type; }
    public void setType(String type) { this.type = type; }
}
