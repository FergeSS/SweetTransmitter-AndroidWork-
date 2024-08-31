package com.company.transmitter;

public class Item {
    private final int image;
    private final String title;
    private final int price;
    private boolean isFav;
    private final int id;
    private final int imageCard;
    private final String desc;


    public Item(int image, String title, int price, boolean isFav, int id, int imageCard, String desc) {
        this.image = image;
        this.title = title;
        this.price = price;
        this.isFav = isFav;
        this.id = id;
        this.imageCard = imageCard;
        this.desc = desc;
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

    public boolean getFav() {
        return isFav;
    }

    public void setFav(boolean isFav) {
        this.isFav = isFav;
    }

    public int getImageCard() { return imageCard; }
    public String getDesc() { return desc; }
}
