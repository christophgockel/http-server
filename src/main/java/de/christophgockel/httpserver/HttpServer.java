package de.christophgockel.httpserver;

import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.routes.Router;
import de.christophgockel.httpserver.routes.responders.DefaultResponder;
import de.christophgockel.httpserver.routes.responders.OptionsResponder;

import java.io.DataOutputStream;
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

    while (true) {
      final Socket s = socket.accept();
      Runnable r = new Runnable() {
        @Override
        public void run() {
          try {
            DataOutputStream out;
            out = new DataOutputStream(s.getOutputStream());

            Request request = new Request(s.getInputStream());

            Router router = new Router(new DefaultResponder(fileSystem));
            router.add("/method_options", new OptionsResponder());

            out.writeBytes(router.dispatch(request));

            out.close();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      };

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
