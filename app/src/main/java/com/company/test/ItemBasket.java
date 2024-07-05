package com.company.test;

public class ItemBasket {
    private final int image;
    private final String title;
    private final int price;
    private final int id;
    private int count;

    public ItemBasket(int image, String title, int price, int id, int count) {
        this.image = image;
        this.title = title;
        this.price = price;
        this.id = id;
        this.count = count;
    }

    public int getId (){ return id; }
    public int getImage() {
        return image;
    }
    public String getTitle() {
        return title;
    }
    public int getPrice() {
        return price;
    }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
