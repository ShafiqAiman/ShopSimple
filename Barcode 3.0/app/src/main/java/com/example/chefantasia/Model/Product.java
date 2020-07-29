package com.example.chefantasia.Model;

public class Product {
    private String Name;
    private String Price;
    private String Image;
    private String Id;

    public Product() {
    }

    public Product(String name, String price, String image, String id) {
        Name = name;
        Price = price;
        Image = image;
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
