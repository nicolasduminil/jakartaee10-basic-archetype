package $package;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import org.apache.http.*;
import jakarta.inject.*;
import org.junit.jupiter.api.*;
import org.slf4j.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.containers.wait.strategy.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.*;

import java.net.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import org.eclipse.microprofile.config.inject.*;

@Testcontainers
public class MyResourceIT
{
  private Client client;
  private WebTarget webTarget;
  private static final String APP_URL = "http://%s:%d/${artifactId}/api/myresource";
  private static URI uri;
  @Inject
  @ConfigProperty(name = "message")
  private String message;

  @Container
  private static final GenericContainer<?> payara =
    new GenericContainer<>("payara/server-full:6.2022.1")
      .withExposedPorts(4848, 8080)
      .withFileSystemBind("target/${artifactId}.war",
        "/opt/payara/deployments/${artifactId}.war", BindMode.READ_ONLY)
      .withLogConsumer(
        new Slf4jLogConsumer(LoggerFactory.getLogger(MyResourceIT.class)))
      .waitingFor(Wait.forLogMessage(".*JMXStartupService has started.*", 1));

  @BeforeAll
  public static void beforeAll()
  {
    uri = UriBuilder.fromUri(
        String.format(APP_URL, payara.getHost(), payara.getMappedPort(8080)))
      .build();
  }

  @BeforeEach
  public void beforeEach()
  {
    client = ClientBuilder.newClient();
    webTarget = client.target(
      String.format(APP_URL, payara.getHost(), payara.getMappedPort(8080)));
  }

  @AfterAll
  public static void afterAll()
  {
    uri = null;
  }

  @AfterEach
  public void afterEach()
  {
    if (client != null)
    {
      client.close();
      client = null;
    }
    webTarget = null;
  }

  @Test
  public void testShouldSucceed()
  {
    Response response = webTarget.request().get();
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
    assertThat(response.readEntity(String.class)).isEqualTo("Got it !");
  }
}
