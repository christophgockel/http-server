package de.christophgockel.httpserver;

import de.christophgockel.httpserver.controllers.*;
import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.filtering.FilterChain;
import de.christophgockel.httpserver.filtering.filters.AuthenticationFilter;
import de.christophgockel.httpserver.filtering.filters.LoggingFilter;
import de.christophgockel.httpserver.http.DefaultServerSocket;
import de.christophgockel.httpserver.http.ServerSocket;
import de.christophgockel.httpserver.routing.Router;
import de.christophgockel.httpserver.util.Arguments;

import java.io.IOException;
import java.util.concurrent.Executors;

public class Main {
  public static void main(String[] args) throws IOException {
    HttpServer server = null;
    Arguments arguments = new Arguments(args);
    FileSystem fileSystem = new FileSystem(arguments.getDocumentRoot());

    final Router router = createRouter(fileSystem, arguments.getPort());
    final FilterChain filters = createFilterChain();

    try {
      ServerSocket socket = new DefaultServerSocket(new java.net.ServerSocket(arguments.getPort()));
      server = new HttpServer(socket, Executors.newFixedThreadPool(5), router, filters);
      server.start();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      ensureServerIsStopped(server);
    }
  }

  static FilterChain createFilterChain() {
    FilterChain chain = new FilterChain();

    chain.addFilter(new LoggingFilter());
    chain.addFilter("/logs", new AuthenticationFilter("admin", "hunter2"));

    return chain;
  }

  static Router createRouter(FileSystem fileSystem, int port) {
    Router router = new Router(new DefaultController(fileSystem));

    router.addRoute("/method_options",      new OptionsController());
    router.addRoute("/patch-content.txt",   new PatchController(fileSystem));
    router.addRoute("/parameters",          new ParametersController());
    router.addRoute("/form",                new FormController(fileSystem));
    router.addRoute("/redirect",            new RedirectController(port));
    router.addRoute("/partial_content.txt", new PartialController(fileSystem));
    router.addRoute("/logs",                new LogController());

    return router;
  }

  static void ensureServerIsStopped(HttpServer server) throws IOException {
    if (server != null) {
      server.stop();
    }
  }
}
