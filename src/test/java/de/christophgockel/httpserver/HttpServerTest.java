package de.christophgockel.httpserver;

import de.christophgockel.httpserver.filtering.FilterChain;
import de.christophgockel.httpserver.helper.SingleThreadedExecutor;
import de.christophgockel.httpserver.http.StubServerSocket;
import de.christophgockel.httpserver.routing.Router;
import de.christophgockel.httpserver.controllers.DummyController;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class HttpServerTest {
  private HttpServer server;
  private StubServerSocket socket;
  private SingleThreadedExecutor executor;

  @Before
  public void setup() throws IOException {
    socket = new StubServerSocket("GET / HTTP/1.1");
    executor = new SingleThreadedExecutor();
    server = new HttpServer(socket, executor, new Router(new DummyController()), new FilterChain());
  }

  @Test
  public void executesRequests() throws IOException {
    server.start();
    assertTrue(executor.hasExecuted());
  }

  @Test
  public void closesTheSocketWhenStopped() throws IOException {
    server.start();
    server.stop();
    assertTrue(socket.isClosed());
  }
}
