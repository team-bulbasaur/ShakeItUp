package com.bryonnicoson.shakeitup;

/**
 * Created by bryon on 7/27/16.
 */
public class Restaurant {
    public String id;
    public String name;
    public String image;
    public String website;
    public Double latitude;
    public Double longitude;
    public String categories;
    public String snippetText;
    public Integer reviewCount;     // <-- it's a sailboat :)
    public String snippetImg;
    public String ratingImg;
    public String address;
    public String phone;
    public Restaurant(String id, String name, String image, String categories, String address,
                      String phone, String website, String ratingImg, Integer reviewCount,
                      String snippetImg, String snippetText, Double latitude, Double longitude) {
        this.phone = phone;
        this.address = address;
        this.ratingImg = ratingImg;
        this.snippetImg = snippetImg;
        this.reviewCount = reviewCount;
        this.snippetText = snippetText;
        this.categories = categories;
        this.longitude = longitude;
        this.latitude = latitude;
        this.website = website;
        this.image = image;
        this.name = name;
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getImage() {
        return image;
    }
    public String getWebsite() {
        return website;
    }
    public Double getLatitude() {
        return latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public String getCategories() {
        return categories;
    }
    public String getSnippetText() {
        return snippetText;
    }
    public Integer getReviewCount() {
        return reviewCount;
    }
    public String getSnippetImg() {
        return snippetImg;
    }
    public String getRatingImg() {
        return ratingImg;
    }
    public String getAddress() {
        return address;
    }
    public String getPhone() {
        return phone;
    }
}
