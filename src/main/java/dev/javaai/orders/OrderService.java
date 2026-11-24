package dev.javaai.orders;

import dev.javaai.orders.Order.OrderStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * Serviciul tău existent. Aici e în memorie ca exemplul să pornească fără bază de date;
 * în aplicația ta reală ar fi un repository JPA.
 */
@Service
public class OrderService {

    private final Map<Long, Order> orders = new ConcurrentHashMap<>();

    public OrderService() {
        LocalDate today = LocalDate.now();
        save(new Order(1041L, "Ana Popescu", OrderStatus.DELIVERED, List.of("Tastatură mecanică"), today.minusDays(2)));
        save(new Order(1042L, "Mihai Ionescu", OrderStatus.SHIPPED, List.of("Monitor 27\"", "Cablu HDMI"), today.plusDays(2)));
        save(new Order(1043L, "Ioana Marin", OrderStatus.PAID, List.of("Laptop stand"), today.plusDays(5)));
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    public List<Order> findByStatus(OrderStatus status) {
        return orders.values().stream().filter(o -> o.status() == status).toList();
    }

    private void save(Order order) {
        orders.put(order.id(), order);
    }
}
