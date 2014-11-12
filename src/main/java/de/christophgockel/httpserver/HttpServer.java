package de.christophgockel.httpserver;

import de.christophgockel.httpserver.http.MethodDispatcher;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.method.*;

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

  private final ServerSocket socket;
  Executor threadPool;


  public HttpServer(int port, String documentRoot) throws IOException {
    this.port = port;
    this.documentRoot = documentRoot;
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
      final Socket s = socket.accept();
      Runnable r = new Runnable() {
        @Override
        public void run() {
          try {
            DataOutputStream out;
            out = new DataOutputStream(s.getOutputStream());

            Request request = new Request(s.getInputStream());

            MethodDispatcher dispatcher = new MethodDispatcher();
            dispatcher.addHandler(RequestMethod.GET, new Get());
            dispatcher.addHandler(RequestMethod.POST, new Post());
            dispatcher.addHandler(RequestMethod.PUT, new Put());
            dispatcher.addHandler(RequestMethod.HEAD, new Head());
            dispatcher.addHandler(RequestMethod.OPTIONS, new Options());

            out.writeBytes(dispatcher.process(request));
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
