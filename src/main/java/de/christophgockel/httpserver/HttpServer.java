package de.christophgockel.httpserver;

import de.christophgockel.httpserver.filtering.FilterChain;
import de.christophgockel.httpserver.http.ClientSocket;
import de.christophgockel.httpserver.http.ServerSocket;
import de.christophgockel.httpserver.routing.Router;

import java.io.IOException;
import java.util.concurrent.Executor;

public class HttpServer {
  private final Router router;
  private final FilterChain filters;
  private final ServerSocket socket;
  private final Executor executor;

  public HttpServer(ServerSocket socket, Executor executor, Router router, FilterChain filters) throws IOException {
    this.socket = socket;
    this.executor = executor;
    this.router = router;
    this.filters = filters;
  }

  public void start() throws IOException {
    ClientSocket cs = socket.accept();

    while (cs != null) {
      RequestExecutor request = new RequestExecutor(cs, router, filters);
      executor.execute(request);
      cs = socket.accept();
    }
  }

  public void stop() throws IOException {
    socket.close();
  }
}
