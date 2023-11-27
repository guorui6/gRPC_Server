package org.example.service;

import io.grpc.stub.StreamObserver;
import org.example.model.DeliveryResult;
import org.example.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductStreamRequestForDelivery implements StreamObserver<Product> {
    private StreamObserver<DeliveryResult> deliveryResultStreamObserver;
    private Map<Integer, List<Product>> map;

    public ProductStreamRequestForDelivery(StreamObserver<DeliveryResult> deliveryResultStreamObserver) {
        this.deliveryResultStreamObserver = deliveryResultStreamObserver;
        map = new HashMap<>();
    }

    @Override
    public void onNext(Product product) {
        int year = product.getYear();
        map.putIfAbsent(year, new ArrayList<>());
        map.get(year).add(product);
        System.out.println("Server received: " + product.getName());
        if (map.get(year).size() == 3) {
            DeliveryResult result = DeliveryResult.newBuilder().addAllProducts(map.get(year)).setTimestamp(System.currentTimeMillis()).build();
            deliveryResultStreamObserver.onNext(result);
            System.out.println("Server collected 3 product with year " + year + " and delivered.");
            map.get(year).clear();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onCompleted() {
        deliveryResultStreamObserver.onCompleted();
        System.out.println("Server completed!");
    }
}
