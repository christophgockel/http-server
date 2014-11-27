package de.christophgockel.httpserver;

import de.christophgockel.httpserver.helper.SingleThreadedExecutor;
import de.christophgockel.httpserver.http.StubServerSocket;
import de.christophgockel.httpserver.routes.Router;
import de.christophgockel.httpserver.routes.responders.NonRespondingResponder;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainTest {
  @Test
  public void hasNeededRoutes() {
    Router router = Main.createRouter(null, 0);

    assertTrue(router.matches("/method_options"));
    assertTrue(router.matches("/patch-content.txt"));
    assertTrue(router.matches("/parameters"));
    assertTrue(router.matches("/form"));
    assertTrue(router.matches("/redirect"));
    assertTrue(router.matches("/partial_content.txt"));
    assertTrue(router.matches("/logs"));
  }

  @Test
  public void ensureServerIsStopped() throws IOException {
    StubServerSocket socket = new StubServerSocket("GET / HTTP/1.1");
    HttpServer server = new HttpServer(socket, new SingleThreadedExecutor(), new Router(new NonRespondingResponder()));
    server.start();

    Main.ensureServerIsStopped(server);

    assertTrue(socket.isClosed());
  }
}
