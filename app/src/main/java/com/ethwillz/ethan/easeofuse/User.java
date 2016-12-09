package com.ethwillz.ethan.easeofuse;

public class User {

    String uid;
    String userName;
    String imageUrl;
    String displayName;

    public User(String uid, String userName, String imageUrl, String displayName){
        this.uid = uid;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.displayName = displayName;
    }

    public void setUid(String uid){ this.uid = uid; }
    public String getUid(){ return uid; }

    public void setUserName(String userName){ this.userName = userName; }
    public String getUserName(){ return userName; }

    public void setUDisplayName(String displayName){ this.displayName = displayName; }
    public String getDisplayName(){ return displayName; }

    public void setImageUrl(String imageUrl){ this.imageUrl = imageUrl;}
    public String getImageUrl(){ return imageUrl; }

}
