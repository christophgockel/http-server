package de.christophgockel.httpserver;

import de.christophgockel.httpserver.routes.Router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HttpServer {
  private final int port;
  private final String documentRoot;
  private final Router router;
  private boolean isRunning;

  private final ServerSocket socket;
  Executor threadPool;


  public HttpServer(int port, String documentRoot, Router router) throws IOException {
    this.port = port;
    this.documentRoot = documentRoot;
    this.router = router;
    this.isRunning = false;

    socket = new ServerSocket(this.port);
    threadPool = Executors.newFixedThreadPool(5);
  }

  public int getPort() {
    return port;
  }

  public String getDocumentRoot() {
    return documentRoot;
  }

  public void start() throws IOException {
    isRunning = true;

    while (true) {
      final Socket clientSocket = socket.accept();
      RequestExecutor request = new RequestExecutor(clientSocket, router);
      threadPool.execute(request);
    }
  }

  public boolean isRunning() {
    return isRunning;
  }

  public void stop() throws IOException {
    isRunning = false;
    socket.close();
  }
}
