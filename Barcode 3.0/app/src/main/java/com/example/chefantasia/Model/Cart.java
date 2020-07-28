package com.example.chefantasia.Model;

public class Cart {
    private String Name;
    private String Price;
    private String Quantity;

    public Cart() {
    }

    public Cart(String name, String price, String quantity) {
        Name = name;
        Price = price;
        Quantity = quantity;
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

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
