package de.christophgockel.httpserver;

import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.routes.Router;
import de.christophgockel.httpserver.routes.responders.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HttpServer {
  private final int port;
  private final String documentRoot;
  private boolean isRunning;
  private final FileSystem fileSystem;

  private final ServerSocket socket;
  Executor threadPool;


  public HttpServer(int port, String documentRoot) throws IOException {
    this.port = port;
    this.documentRoot = documentRoot;
    this.isRunning = false;

    socket = new ServerSocket(this.port);
    threadPool = Executors.newFixedThreadPool(5);
    fileSystem = new FileSystem(this.documentRoot);
  }

  public int getPort() {
    return port;
  }

  public String getDocumentRoot() {
    return documentRoot;
  }

  public void start() throws IOException {
    isRunning = true;

    final Router router = new Router(new DefaultResponder(fileSystem));
    router.add("/method_options", new OptionsResponder());
    router.add("/patch-content.txt", new PatchResponder(fileSystem));
    router.add("/parameters", new ParametersResponder());
    router.add("/form", new FormResponder(fileSystem));
    router.add("/redirect", new RedirectResponder(5000));
    router.add("/partial_content.txt", new PartialResponder(fileSystem));
    router.add("/logs", new LogResponder());

    while (true) {
      final Socket clientSocket = socket.accept();
      RequestExecutor r = new RequestExecutor(clientSocket, router);
      threadPool.execute(r);
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
