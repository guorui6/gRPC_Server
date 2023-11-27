package org.example.service;

import io.grpc.stub.StreamObserver;
import org.example.model.Product;
import org.example.model.Summary;

import java.util.HashMap;
import java.util.Map;

public class ProductStreamRequestForSummary implements StreamObserver<Product> {
    private final StreamObserver<Summary> summaryStreamObserver;
    private final Map<Integer, Integer> map;

    public ProductStreamRequestForSummary(StreamObserver<Summary> summaryStreamObserver) {
        this.summaryStreamObserver = summaryStreamObserver;
        this.map = new HashMap<>();
    }

    @Override
    public void onNext(Product product) {
        int year = product.getYear();
        map.put(year, map.getOrDefault(year, 0) + 1);
        System.out.println("Server received: " + product.getName());
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onCompleted() {
        Summary summary = Summary.newBuilder().putAllMapYear(map).build();
        summaryStreamObserver.onNext(summary);
        summaryStreamObserver.onCompleted();
        System.out.println("Server completed.");
    }
}
