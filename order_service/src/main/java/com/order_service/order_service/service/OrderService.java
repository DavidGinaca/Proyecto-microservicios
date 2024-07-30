package com.order_service.order_service.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


import org.springframework.stereotype.Service;

import com.order_service.order_service.dto.OrderLineItemsDto;
import com.order_service.order_service.dto.OrderRequest;
import com.order_service.order_service.model.Order;
import com.order_service.order_service.model.OrderLineItems;
import com.order_service.order_service.repository.OrderRepository;

//import io.micrometer.observation.Observation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

  public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);
       
        orderRepository.save(order);

        // List<String> skuCodes = order.getOrderLineItemsList().stream()
        //         .map(OrderLineItems::getSkuCode)
        //         .toList();

        // Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup",
        //         this.observationRegistry);
        // inventoryServiceObservation.lowCardinalityKeyValue("call", "inventory-service");
        // return inventoryServiceObservation.observe(() -> {
        //     InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
        //             .uri("http://inventory-service/api/inventory",
        //                     uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
        //             .retrieve()
        //             .bodyToMono(InventoryResponse[].class)
        //             .block();

        //     boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
        //             .allMatch(InventoryResponse::isInStock);

        //     if (allProductsInStock) {
        //         orderRepository.save(order);
        //         // publish Order Placed Event
        //         applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
        //         return "Order Placed";
        //     } else {
        //         throw new IllegalArgumentException("Product is not in stock, please try again later");
        //     }
        //});

    }

     private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

}
