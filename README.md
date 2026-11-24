# Aplicația ta Spring Boot poate vorbi cu Claude (MCP în 3 pași)

Proiectul complet din video. Un API de comenzi în Spring Boot, expus lui Claude prin **MCP (Model Context Protocol)** cu Spring AI. După ce îl pornești, îl poți întreba pe Claude „Care e statusul comenzii #1042?” și el îți apelează direct codul Java.

**Stack:** Java 21 · Spring Boot 4.2 · Spring AI 2.1 · MCP Streamable HTTP

---

## Pornire rapidă

```bash
./mvnw spring-boot:run
```

Serverul MCP pornește la `http://localhost:8080/mcp`.

Conectează-l la Claude Code (într-un alt terminal):

```bash
claude mcp add --transport http shop http://localhost:8080/mcp
```

Deschide `claude` și întreabă:

> Care e statusul comenzii #1042?
>
> Ce comenzi sunt expediate acum?

Claude va apela `findOrder` sau `findOrdersByStatus` și îți va răspunde cu datele din aplicație.

---

## Cei 3 pași din video

### 1. Dependența (`pom.xml`)

```xml
<dependency>
  <groupId>org.springframework.ai</groupId>
  <artifactId>spring-ai-starter-mcp-server-webmvc</artifactId>
</dependency>
```

plus BOM-ul Spring AI în `<dependencyManagement>` (vezi `pom.xml`).

### 2. Un tool pentru Claude (`OrderTools.java`)

```java
@McpTool(description = "Găsește o comandă după număr. ...")
public Order findOrder(@McpToolParam(description = "Numărul comenzii, ex. 1042") Long id) {
    return orders.findById(id).orElseThrow(...);
}
```

**Descrierea contează cel mai mult.** După ea decide Claude *când* să folosească tool-ul. Scrie-o ca pentru un coleg nou: ce face, ce primește, ce întoarce.

### 3. Configurare și conectare

```properties
spring.ai.mcp.server.protocol=STREAMABLE
```

(În Spring AI 2.1 e deja implicit; e lăsat explicit ca să fie clar.) Apoi `claude mcp add ...` de mai sus.

---

## Structura

```
src/main/java/dev/javaai/orders/
├── Order.java            # modelul
├── OrderService.java     # serviciul tău existent (aici în memorie)
├── OrderController.java  # API-ul REST pe care îl ai deja, neschimbat
└── OrderTools.java       # ← nou: tool-urile MCP pentru Claude
```

`OrderTools` doar refolosește `OrderService`. Nu rescrii nimic din aplicație: adaugi un strat subțire lângă API-ul existent.

## Test

```bash
./mvnw test
```

Testul pornește aplicația și vorbește cu ea printr-un client MCP real, exact ca Claude: listează tool-urile și apelează `findOrder(1042)`.

## Cum îl adaptezi la aplicația ta

1. Adaugă dependența și BOM-ul Spring AI.
2. Creează o clasă `@Component` cu metode `@McpTool` care apelează serviciile tale existente.
3. Pornește aplicația și rulează `claude mcp add`.

**Atenție la securitate:** exemplul nu are autentificare, ca să fie simplu. Nu expune un server MCP fără autentificare în afara mașinii tale. Expune doar operațiile pe care chiar vrei ca un AI să le poată face (începe cu citire, nu cu ștergere).

## Probleme frecvente

- **Claude nu vede tool-urile:** verifică `claude mcp list` și că aplicația rulează pe portul 8080.
- **Claude nu folosește tool-ul:** îmbunătățește `description` din `@McpTool`.
- **Port ocupat:** pornește cu `--server.port=8081` și actualizează URL-ul în `claude mcp add`.
