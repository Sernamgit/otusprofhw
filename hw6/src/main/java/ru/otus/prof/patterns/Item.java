package ru.otus.prof.patterns;

public class Item {
    private int id;
    private String title;
    private double price;


    public Item(int id, String title, double price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        Item other = (Item) obj;
        return id == other.id &&
                Double.compare(price, other.price) == 0 &&
                (title != null ? title.equals(other.title) : other.title == null);
    }
}
