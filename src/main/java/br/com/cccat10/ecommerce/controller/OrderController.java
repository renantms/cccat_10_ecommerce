package br.com.cccat10.ecommerce.controller;

import br.com.cccat10.ecommerce.domain.Order;
import br.com.cccat10.ecommerce.domain.Product;
import br.com.cccat10.ecommerce.domain.request.OrderRequest;
import br.com.cccat10.ecommerce.domain.request.ProductRequest;
import br.com.cccat10.ecommerce.domain.response.OrderResponse;
import br.com.cccat10.ecommerce.domain.response.ProductResponse;
import br.com.cccat10.ecommerce.exception.InvalidCpfException;
import br.com.cccat10.ecommerce.usecase.CreateOrderUseCase;
import br.com.cccat10.ecommerce.usecase.RetrieveOrderUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;

    private final RetrieveOrderUseCase retrieveOrderUseCase;

    @GetMapping("/{order_id}")
    public ResponseEntity<OrderResponse> retrieveById(@PathVariable("order_id") Long orderId) {
        try {
            final var order = retrieveOrderUseCase.execute(orderId);
            final var orderResponse = mapToOrderResponse(order);
            return ResponseEntity.ok(orderResponse);
        } catch (NoSuchElementException e) {
            log.error("Pedido nao encontrado, id={}", orderId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro inesperado, id={}, message={}", orderId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody final OrderRequest orderRequest) {
        try {
            final var order = mapToOrder(orderRequest);
            createOrderUseCase.execute(order);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (InvalidCpfException e) {
            log.error("Pedido com CPF invalido, invalidCpf={}, message={}",
                    orderRequest.getBuyerCpf(), e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Erro inesperado, buyerCpf={}, message={}", orderRequest.getBuyerCpf(), e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private OrderResponse mapToOrderResponse(final Order order) {
        final var productList = mapToProductResponseList(order.getProductList());

        return OrderResponse.builder()
                .buyerCpf(order.getBuyerCpf())
                .discountPercentage(order.getDiscountPercentage())
                .productList(productList)
                .orderDate(order.getCreatedAt())
                .orderValue(order.getOrderValue())
                .build();
    }

    private List<ProductResponse> mapToProductResponseList(final List<Product> productList) {
        return productList.stream()
                .map(product ->
                        ProductResponse.builder()
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .quantity(product.getQuantity())
                                .build())
                .toList();
    }

    private Order mapToOrder(final OrderRequest orderRequest) {
        final var productList = mapToProductList(orderRequest.getProductList());

        return Order.builder()
                .buyerCpf(unmaskCpf(orderRequest.getBuyerCpf()))
                .discountPercentage(orderRequest.getDiscountPercentage())
                .productList(productList)
                .build();
    }

    private List<Product> mapToProductList(final List<ProductRequest> productRequestList) {
        return productRequestList.stream()
                .map(productRequest ->
                        Product.builder()
                                .description(productRequest.getDescription())
                                .price(productRequest.getPrice())
                                .quantity(productRequest.getQuantity())
                                .build())
                .toList();
    }

    private String unmaskCpf(final String cpf) {
        return cpf
                .replace(".", "")
                .replace("-", "");
    }

}
