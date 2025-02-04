package ru.otus.prof.patterns;

import java.sql.SQLException;
import java.util.List;

public class ItemsService {

    private final ItemsDao itemsDao;

    public ItemsService(ItemsDao itemsDao) {
        this.itemsDao = itemsDao;
    }

    public void save100Items() {
        for (int i = 0; i < 100; i++) {
            Item item = new Item(i, "Item" + i, i * 10.0);
            try {
                itemsDao.createItem(item);
            } catch (SQLException e) {
                System.out.println("Error on save item: " + item.getTitle());
                System.out.println(e.getMessage());
            }
        }
    }

    public void doublePriceAll() {
        try {
            List<Item> items = itemsDao.getAllItems();
            for (Item item : items) {
                item.setPrice(item.getPrice() * 2);
                itemsDao.updateItem(item);
            }
        } catch (SQLException e) {
            System.out.println("Error on update price: " + e.getMessage());
        }

    }

}
