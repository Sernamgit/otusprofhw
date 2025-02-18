package ru.otus.prof.springcontext;


import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {
    private List<Product> products = new ArrayList<>();

    @PostConstruct
    public void init(){
        for (int i = 1; i < 11; i++){
          products.add(new Product(i, "Product" + i, i * 10.0));
        }
    }

    public List<Product> getAllProducts(){
        return products;
    }

    public Product getProductById(int id){
        return products.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
    }

}
