package ru.otus.prof.patterns;

import java.util.ArrayList;
import java.util.List;

public final class Matryoshka {
    private final List<String> items;

    public Matryoshka(String colour) {
        this.items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add(colour + i);
        }
    }

    public List<String> getItems(){
        return items;
    }
}
