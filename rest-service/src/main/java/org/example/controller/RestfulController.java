package org.example.controller;

import org.example.dto.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestfulController {
    @GetMapping("/products/{size}")
    public List<Product> getProducts(@PathVariable int size) {
        return buildResult(size);
    }

    private List<Product> buildResult(int size) {
        List<Product> res = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            res.add(new Product("ProductRest " + i, 2022, 199.99, 10000));
        }
        return res;
    }
}
