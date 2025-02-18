package ru.otus.prof.springcontext;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@ComponentScan
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        ProductRepository productRepository = context.getBean(ProductRepository.class);
        Cart cart = context.getBean(Cart.class);
        Scanner scanner = new Scanner(System.in);


        while (true) {
            System.out.println("1. Show all products");
            System.out.println("2. Add product to cart");
            System.out.println("3. Delete product from cart");
            System.out.println("4. Show cart");
            System.out.println("5. Exit");
            int choice = scanner.nextInt();

            if (choice == 5) {
                break;
            }

            switch (choice) {
                case 1:
                    System.out.println("All products:");
                    productRepository.getAllProducts().forEach(System.out::println);
                    break;
                case 2:
                    System.out.println("Enter product id to add too cart: ");
                    int productIdToAdd = scanner.nextInt();
                    Product productToAdd = productRepository.getProductById(productIdToAdd);
                    if (productToAdd != null) {
                        cart.addProduct(productToAdd);
                        System.out.println("Product added to cart");
                    } else {
                        System.out.println("Product not found");
                    }
                    break;
                case 3:
                    System.out.println("Enter product id to remove from cart: ");
                    int productIdToRemove = scanner.nextInt();
                    cart.removeProductById(productIdToRemove);
                    System.out.println("Product removed from cart");
                    break;
                case 4:
                    System.out.println("Products in cart: ");
                    cart.getProducts().forEach(System.out::println);
                    break;
                default:
                    System.out.println("Unknown choice");
            }

        }


    }
}
