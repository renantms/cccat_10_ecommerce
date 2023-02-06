package br.com.cccat10.ecommerce;

import br.com.cccat10.ecommerce.base.BaseIntegrationTest;
import br.com.cccat10.ecommerce.domain.Coupon;
import br.com.cccat10.ecommerce.domain.Product;
import br.com.cccat10.ecommerce.domain.request.OrderRequest;
import br.com.cccat10.ecommerce.domain.request.ProductRequest;
import br.com.cccat10.ecommerce.domain.response.CreateOrderResponse;
import br.com.cccat10.ecommerce.repository.CouponRepository;
import br.com.cccat10.ecommerce.repository.ProductRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderTest extends BaseIntegrationTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldCreateOrder() {
        Product product = new Product();
        product.setPrice(new BigDecimal("30"));
        product.setDescription("AAAAAAAAAAAA");

        Product savedProduct = productRepository.save(product);

        OrderRequest orderRequest = createOrderRequest(savedProduct.getId());


        CreateOrderResponse response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .log().all()
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(CreateOrderResponse.class);

        MatcherAssert.assertThat(response.getTotalValue(), Matchers.comparesEqualTo(new BigDecimal("150")));
    }

    @Test
    void shouldCreateOrderWithCoupon() {
        Product product = new Product();
        product.setPrice(new BigDecimal("30"));
        product.setDescription("AAAAAAAAAAAA");

        Product savedProduct = productRepository.save(product);

        OrderRequest orderRequest = createOrderRequestWithCoupon(savedProduct.getId());

        Coupon coupon = new Coupon();
        coupon.setCouponName(orderRequest.getCouponName());
        coupon.setDiscountPercentage(new BigDecimal("0.20"));
        couponRepository.save(coupon);

        CreateOrderResponse response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .log().all()
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(CreateOrderResponse.class);

        MatcherAssert.assertThat(response.getTotalValue(), Matchers.comparesEqualTo(new BigDecimal("120")));
    }

    OrderRequest createOrderRequest(Long productId) {
        List<ProductRequest> productRequestList = new ArrayList<>();
        productRequestList.add(createProductRequest(productId));
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setBuyerCpf("11144477735");
        orderRequest.setProductList(productRequestList);
        return orderRequest;
    }

    OrderRequest createOrderRequestWithCoupon(Long productId) {
        OrderRequest orderRequest = createOrderRequest(productId);
        orderRequest.setCouponName("VALE20");

        return orderRequest;
    }

    ProductRequest createProductRequest(Long productId) {
        ProductRequest productRequest = new ProductRequest();

        productRequest.setId(productId);
        productRequest.setQuantity(5L);
        return productRequest;
    }
}
