package com.ecommerce.price_service.infrastructure.adapter.out.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "prices")
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "price_list", nullable = false)
    private Integer priceList;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "curr", nullable = false, length = 3)
    private String currency;

    protected PriceEntity() {
        // required by JPA
    }

    public PriceEntity(Long brandId, Long productId, Integer priceList, Integer priority,
                       LocalDateTime startDate, LocalDateTime endDate,
                       BigDecimal price, String currency) {
        this.brandId = brandId;
        this.productId = productId;
        this.priceList = priceList;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.currency = currency;
    }
    public Long getId() {
        return id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Integer getPriceList() {
        return priceList;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getPriority() {
        return priority;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}
