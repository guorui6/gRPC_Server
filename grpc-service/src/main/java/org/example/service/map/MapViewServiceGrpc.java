package org.example.service.map;

import com.lime.supply.grpc.model.MapItemResponse;
import com.lime.supply.grpc.model.MapViewRequest;
import com.lime.supply.grpc.model.MapViewResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@GrpcService
public class MapViewServiceGrpc extends com.lime.supply.grpc.model.MapViewServiceGrpc.MapViewServiceImplBase {
    @Override
    public void mapView(MapViewRequest request, StreamObserver<MapViewResponse> responseObserver) {
        System.out.println("received request: " + request.toString());
        for (int i = 0; i < 10; i++) {
            List<MapItemResponse> items = mockGetFromDB();
            MapViewResponse response = MapViewResponse.newBuilder().addAllMapItems(items).build();
            responseObserver.onNext(response);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
            System.out.println("send to client-" + i);
        }
        responseObserver.onCompleted();
        System.out.println("send all. ");
    }

    private List<MapItemResponse> mockGetFromDB() {
        List<MapItemResponse> list = new ArrayList<>();
        int randomItemSize = new Random().nextInt(100);
        for (int j = 0; j < randomItemSize; j++) {
            list.add(MapItemResponse.newBuilder().build());
        }
        return list;
    }
}
