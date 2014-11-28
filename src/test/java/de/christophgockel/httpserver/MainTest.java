package de.christophgockel.httpserver;

import de.christophgockel.httpserver.controllers.DummyController;
import de.christophgockel.httpserver.helper.SingleThreadedExecutor;
import de.christophgockel.httpserver.http.StubServerSocket;
import de.christophgockel.httpserver.routing.Router;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MainTest {
  @Test
  public void hasNeededRoutes() {
    Router router = Main.createRouter(null, 0);

    assertNotNull(router.getController("/method_options"));
    assertNotNull(router.getController("/patch-content.txt"));
    assertNotNull(router.getController("/parameters"));
    assertNotNull(router.getController("/form"));
    assertNotNull(router.getController("/redirect"));
    assertNotNull(router.getController("/partial_content.txt"));
    assertNotNull(router.getController("/logs"));
  }

  @Test
  public void ensureServerIsStopped() throws IOException {
    StubServerSocket socket = new StubServerSocket("GET / HTTP/1.1");
    HttpServer server = new HttpServer(socket, new SingleThreadedExecutor(), new Router(new DummyController()));
    server.start();

    Main.ensureServerIsStopped(server);

    assertTrue(socket.isClosed());
  }
}
