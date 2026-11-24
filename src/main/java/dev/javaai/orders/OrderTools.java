package dev.javaai.orders;

import dev.javaai.orders.Order.OrderStatus;
import java.util.List;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

/**
 * Pasul 2 din video: metodele pe care Claude le poate apela.
 * Descrierea e importantă: după ea decide Claude CÂND să folosească tool-ul.
 */
@Component
public class OrderTools {

    private final OrderService orders;

    public OrderTools(OrderService orders) {
        this.orders = orders;
    }

    @McpTool(description = "Găsește o comandă după număr. Întoarce clientul, statusul, produsele și data estimată de livrare.")
    public Order findOrder(@McpToolParam(description = "Numărul comenzii, ex. 1042") Long id) {
        return orders.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nu există comanda #" + id));
    }

    @McpTool(description = "Listează comenzile care au un anumit status: NEW, PAID, SHIPPED, DELIVERED sau CANCELLED.")
    public List<Order> findOrdersByStatus(@McpToolParam(description = "Statusul căutat, ex. SHIPPED") OrderStatus status) {
        return orders.findByStatus(status);
    }
}
