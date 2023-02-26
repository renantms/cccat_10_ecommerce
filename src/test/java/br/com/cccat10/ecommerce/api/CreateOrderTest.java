package br.com.cccat10.ecommerce.api;

import br.com.cccat10.ecommerce.exception.DuplicatedItemException;
import br.com.cccat10.ecommerce.exception.InvalidQuantityException;
import br.com.cccat10.ecommerce.integration.base.BaseIntegrationTest;
import br.com.cccat10.ecommerce.domain.Coupon;
import br.com.cccat10.ecommerce.domain.Product;
import br.com.cccat10.ecommerce.domain.request.OrderRequest;
import br.com.cccat10.ecommerce.domain.request.ProductRequest;
import br.com.cccat10.ecommerce.domain.response.CreateOrderResponse;
import br.com.cccat10.ecommerce.integration.base.CleanupH2DatabaseTestListener;
import br.com.cccat10.ecommerce.repository.CouponRepository;
import br.com.cccat10.ecommerce.repository.ProductRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, CleanupH2DatabaseTestListener.class})
public class CreateOrderTest extends BaseApiTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldCreateOrder() {
        Product product = createProduct();

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

        MatcherAssert.assertThat(response.getTotalValue(), Matchers.comparesEqualTo(new BigDecimal("300")));
    }

    @Test
    void shouldCreateOrderWithCoupon() {
        Product product = createProduct();

        Product savedProduct = productRepository.save(product);

        OrderRequest orderRequest = createOrderRequestWithCoupon(savedProduct.getId());

        Coupon coupon = new Coupon();
        coupon.setCouponName(orderRequest.getCouponName());
        coupon.setDiscountPercentage(new BigDecimal("0.20"));
        coupon.setExpireDate(LocalDateTime.now().plusDays(1L));
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

        MatcherAssert.assertThat(response.getTotalValue(), Matchers.comparesEqualTo(new BigDecimal("240")));
    }

    @Test
    void shouldCreateOrderWithCouponExpired() {
        Product product = createProduct();

        Product savedProduct = productRepository.save(product);

        OrderRequest orderRequest = createOrderRequestWithCoupon(savedProduct.getId());

        Coupon expiredCoupon = new Coupon();
        expiredCoupon.setCouponName(orderRequest.getCouponName());
        expiredCoupon.setDiscountPercentage(new BigDecimal("0.20"));
        expiredCoupon.setExpireDate(LocalDateTime.now().minusDays(1L));
        couponRepository.save(expiredCoupon);

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

        MatcherAssert.assertThat(response.getTotalValue(), Matchers.comparesEqualTo(new BigDecimal("300")));
    }

    @Test
    void shouldNotCreateOrderWhenItemIsDuplicated() {
        Product product = createProduct();

        Product savedProduct = productRepository.save(product);

        OrderRequest orderRequest = createOrderRequest(savedProduct.getId());
        orderRequest.getProductList().add(createProductRequest(savedProduct.getId()));
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .log().all()
                .when()
                .post("/orders")
                .then()
                .statusCode(422)
                .body("message", Matchers.is(DuplicatedItemException.ERROR_MESSAGE));
    }

    @Test
    void shouldNotCreateOrderWhenItemHasInvalidQuantity() {
        Product product = createProduct();

        Product savedProduct = productRepository.save(product);

        OrderRequest orderRequest = createOrderRequestWithoutItem();
        orderRequest.setProductList(List.of(createProductRequestWithInvalidQuantity(savedProduct.getId())));

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .log().all()
                .when()
                .post("/orders")
                .then()
                .log().all()
                .statusCode(422)
                .body("message", Matchers.is(InvalidQuantityException.ERROR_MESSAGE));
    }

    OrderRequest createOrderRequestWithoutItem() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setBuyerCpf("11144477735");
        return orderRequest;
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

    ProductRequest createProductRequestWithInvalidQuantity(Long productId) {
        ProductRequest productRequest = new ProductRequest();

        productRequest.setId(productId);
        productRequest.setQuantity(-1L);
        return productRequest;
    }

    Product createProduct() {
        Product product = new Product();
        product.setPrice(new BigDecimal("30"));
        product.setDescription("AAAAAAAAAAAA");
        product.setHeight(100L);
        product.setWidth(30L);
        product.setLength(10L);
        product.setWeight(new BigDecimal("3"));
        return product;
    }
}
