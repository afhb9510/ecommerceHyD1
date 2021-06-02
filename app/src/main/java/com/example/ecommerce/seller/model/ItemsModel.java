package com.example.ecommerce.seller.model;

import java.io.Serializable;

public class ItemsModel implements Serializable {
    String name,imageUrl,id;



    Long quantity,price;



    public ItemsModel(String name, Long quantity, Long price, String imageUrl, String id) {
        this.name = name;

        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }


    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
