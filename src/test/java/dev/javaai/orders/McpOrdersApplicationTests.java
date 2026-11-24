package dev.javaai.orders;

import static org.assertj.core.api.Assertions.assertThat;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/** Pornește aplicația și vorbește cu ea exact cum ar face Claude: printr-un client MCP. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class McpOrdersApplicationTests {

    @LocalServerPort
    int port;

    @Test
    void claudeCanFindAnOrderThroughMcp() {
        var transport = HttpClientStreamableHttpTransport.builder("http://localhost:" + port).endpoint("/mcp").build();
        try (McpSyncClient client = McpClient.sync(transport).build()) {
            client.initialize();

            var tools = client.listTools().tools().stream().map(McpSchema.Tool::name).toList();
            assertThat(tools).contains("findOrder", "findOrdersByStatus");

            var result = client.callTool(new McpSchema.CallToolRequest("findOrder", Map.of("id", 1042)));
            assertThat(result.isError()).isNotEqualTo(Boolean.TRUE);
            var text = ((McpSchema.TextContent) result.content().getFirst()).text();
            assertThat(text).contains("SHIPPED").contains("Mihai Ionescu");
        }
    }
}
