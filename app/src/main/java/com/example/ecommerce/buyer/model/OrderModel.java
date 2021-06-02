package com.example.ecommerce.buyer.model;

public class OrderModel {
    String Bname,Bemail,Baddress,Bimg;
    String Id,name,status,imgurl,PID;
    Long quantity,price;

    public OrderModel(String bname, String bemail, String baddress, String bimg, String id, String name, String status, String imgurl, Long quantity, Long price, String PID) {
        Bname = bname;
        Bemail = bemail;
        Baddress = baddress;
        Bimg = bimg;
        Id = id;
        this.name = name;
        this.status = status;
        this.imgurl = imgurl;
        this.PID = PID;
        this.quantity = quantity;
        this.price = price;
    }



    public String getBname() {
        return Bname;
    }

    public void setBname(String bname) {
        Bname = bname;
    }

    public String getBemail() {
        return Bemail;
    }

    public void setBemail(String bemail) {
        Bemail = bemail;
    }

    public String getBaddress() {
        return Baddress;
    }

    public void setBaddress(String baddress) {
        Baddress = baddress;
    }

    public String getBimg() {
        return Bimg;
    }

    public void setBimg(String bimg) {
        Bimg = bimg;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public OrderModel(String id, String name, String status, String imgurl, Long quantity, Long price,String PID) {
        Id = id;
        this.name = name;
        this.status = status;
        this.imgurl = imgurl;
        this.quantity = quantity;
        this.price = price;
        this.PID = PID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
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


}
