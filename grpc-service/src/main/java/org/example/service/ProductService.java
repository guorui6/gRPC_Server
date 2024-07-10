package org.example.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@GrpcService
public class ProductService extends ProductServiceGrpc.ProductServiceImplBase {
    @Override
    public void getProducts(SizeRequest request, StreamObserver<ProductList> responseObserver) {
        int size = request.getSize();
        List<Product> products = buildResult(size);
        ProductList list = ProductList.newBuilder().addAllProduct(products).build();
        responseObserver.onNext(list);
        responseObserver.onCompleted();
    }

    @Override
    public void getProductStream(SizeRequest request, StreamObserver<Product> responseObserver) {
        int size = request.getSize();
        for (int i = 0; i < size; i++) {
            Product product = Product.newBuilder()
                    .setQuantity(100)
                    .setName("Product From Server " + i)
                    .setYear(2000 + ThreadLocalRandom.current().nextInt(23))
                    .setPrice(29.99 + ThreadLocalRandom.current().nextInt(100))
                    .build();
            responseObserver.onNext(product);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(product.getName() + " is sent to client.");
        }
        System.out.println(size + " of products has been sent to client. Completed!");
        responseObserver.onCompleted();
        System.out.println("Server Complete sending!");
    }

    @Override
    public StreamObserver<Product> sendProduct(StreamObserver<Summary> responseObserver) {
        return new ProductStreamRequestForSummary(responseObserver);
    }

    @Override
    public StreamObserver<Product> delivery(StreamObserver<DeliveryResult> responseObserver) {
        return new ProductStreamRequestForDelivery(responseObserver);
    }

    private List<Product> buildResult(int size) {
        List<Product> res = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            res.add(Product.newBuilder()
                    .setName("Product " + i)
                    .setYear(2022)
                    .setPrice(199.99)
                    .setQuantity(10000)
                    .build());
        }
        return res;
    }
}
