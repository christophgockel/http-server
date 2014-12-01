package de.christophgockel.httpserver;

import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.DefaultServerSocket;
import de.christophgockel.httpserver.http.ServerSocket;
import de.christophgockel.httpserver.routes.Router;
import de.christophgockel.httpserver.routes.responders.*;
import de.christophgockel.httpserver.util.Arguments;

import java.io.IOException;
import java.util.concurrent.Executors;

public class Main {
  public static void main(String[] args) throws IOException {
    HttpServer server = null;
    Arguments arguments = new Arguments(args);
    FileSystem fileSystem = new FileSystem(arguments.getDocumentRoot());

    final Router router = createRouter(fileSystem, arguments.getPort());

    try {
      ServerSocket socket = new DefaultServerSocket(new java.net.ServerSocket(arguments.getPort()));
      server = new HttpServer(socket, Executors.newFixedThreadPool(5), router);
      server.start();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      ensureServerIsStopped(server);
    }
  }

  static Router createRouter(FileSystem fileSystem, int port) {
    Router router = new Router(new DefaultResponder(fileSystem));

    router.add("/method_options",      new OptionsResponder());
    router.add("/patch-content.txt",   new PatchResponder(fileSystem));
    router.add("/parameters",          new ParametersResponder());
    router.add("/form",                new FormResponder(fileSystem));
    router.add("/redirect",            new RedirectResponder(port));
    router.add("/partial_content.txt", new PartialResponder(fileSystem));
    router.add("/logs",                new LogResponder());

    return router;
  }

  static void ensureServerIsStopped(HttpServer server) throws IOException {
    if (server != null) {
      server.stop();
    }
  }
}
