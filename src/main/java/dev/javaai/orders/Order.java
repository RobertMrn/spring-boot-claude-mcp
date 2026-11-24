package dev.javaai.orders;

import java.time.LocalDate;
import java.util.List;

public record Order(
        Long id,
        String customer,
        OrderStatus status,
        List<String> items,
        LocalDate estimatedDelivery) {

    public enum OrderStatus { NEW, PAID, SHIPPED, DELIVERED, CANCELLED }
}
