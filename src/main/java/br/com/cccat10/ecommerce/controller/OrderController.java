package br.com.cccat10.ecommerce.controller;

import br.com.cccat10.ecommerce.domain.Order;
import br.com.cccat10.ecommerce.domain.OrderProduct;
import br.com.cccat10.ecommerce.domain.dto.OrderValueDTO;
import br.com.cccat10.ecommerce.domain.dto.ProductDTO;
import br.com.cccat10.ecommerce.domain.request.OrderRequest;
import br.com.cccat10.ecommerce.domain.request.ProductRequest;
import br.com.cccat10.ecommerce.domain.response.CreateOrderResponse;
import br.com.cccat10.ecommerce.domain.response.ErrorResponse;
import br.com.cccat10.ecommerce.domain.response.OrderResponse;
import br.com.cccat10.ecommerce.domain.response.ProductResponse;
import br.com.cccat10.ecommerce.exception.DuplicatedItemException;
import br.com.cccat10.ecommerce.exception.InvalidCpfException;
import br.com.cccat10.ecommerce.exception.InvalidQuantityException;
import br.com.cccat10.ecommerce.usecase.CreateOrderUseCase;
import br.com.cccat10.ecommerce.usecase.RetrieveOrderUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> create(@RequestBody final OrderRequest orderRequest) {
        try {
            final var order = mapToOrder(orderRequest);
            final var products = mapToProductDTO(orderRequest.getProductList());

            final OrderValueDTO orderValue = createOrderUseCase.execute(order, orderRequest.getCouponName(), products);
            CreateOrderResponse createOrderResponse = new CreateOrderResponse();
            createOrderResponse.setTotalValue(orderValue.getTotalValue());
            createOrderResponse.setFreightValue(orderValue.getFreightValue());
            return ResponseEntity.status(HttpStatus.CREATED).body(createOrderResponse);
        } catch (InvalidQuantityException | DuplicatedItemException e) {
            log.error("{}, cpf={}", e.getMessage(), orderRequest.getBuyerCpf(), e);
            return ResponseEntity.unprocessableEntity().body(new ErrorResponse(e.getMessage()));
        } catch (InvalidCpfException e) {
            log.error("Pedido com CPF invalido, invalidCpf={}, message={}",
                    orderRequest.getBuyerCpf(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro inesperado, buyerCpf={}, message={}", orderRequest.getBuyerCpf(), e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private OrderResponse mapToOrderResponse(final Order order) {
        final var productList = mapToProductResponseList(order.getProductList());

        return OrderResponse.builder()
                .buyerCpf(order.getBuyerCpf())
                .couponName(order.getCoupon().getCouponName())
                .productList(productList)
                .orderDate(order.getCreatedAt())
                .orderValue(order.calculateOrderValue().getTotalValue())
                .build();
    }

    private List<ProductResponse> mapToProductResponseList(final List<OrderProduct> productList) {
        return productList.stream()
                .map(product ->
                        ProductResponse.builder()
                                .description(product.getProduct().getDescription())
                                .price(product.getProduct().getPrice())
                                .quantity(product.getQuantity())
                                .build())
                .toList();
    }

    private List<ProductDTO> mapToProductDTO(final List<ProductRequest> productRequestList) {
        return productRequestList
                .stream()
                .map(ProductRequest::toDTO)
                .collect(Collectors.toList());
    }

    private Order mapToOrder(final OrderRequest orderRequest) {
        return Order.builder()
                .buyerCpf(unmaskCpf(orderRequest.getBuyerCpf()))
                .productList(new ArrayList<>())
                .build();
    }

    private String unmaskCpf(final String cpf) {
        return cpf
                .replace(".", "")
                .replace("-", "");
    }

}
