package ru.otus.prof.patterns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class Box {

    private final Matryoshka red;
    private final Matryoshka green;
    private final Matryoshka blue;
    private final Matryoshka magenta;

    public Box() {
        this.red = new Matryoshka("red");
        this.green = new Matryoshka("green");
        this.blue = new Matryoshka("blue");
        this.magenta = new Matryoshka("magenta");
    }

    public Iterator<String> getSmallFirstIterator() {
        List<Iterator<String>> iteratorList = List.of(
                red.getItems().iterator(),
                green.getItems().iterator(),
                blue.getItems().iterator(),
                magenta.getItems().iterator());

        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return iteratorList.stream().allMatch(Iterator::hasNext);
            }

            @Override
            public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                String nextItem = iteratorList.get(index).hasNext() ? iteratorList.get(index).next() : null;
                index = (index + 1) % iteratorList.size();

                while (nextItem == null) {
                    index = (index + 1) % iteratorList.size();
                    if (iteratorList.get(index).hasNext()) {
                        nextItem = iteratorList.get(index).next();
                    }
                }
                return nextItem;
            }
        };
    }

    public Iterator<String> getColorFirstIterator() {
        List<String> all = new ArrayList<>();
        all.addAll(red.getItems());
        all.addAll(green.getItems());
        all.addAll(blue.getItems());
        all.addAll(magenta.getItems());

        return all.iterator();
    }

}