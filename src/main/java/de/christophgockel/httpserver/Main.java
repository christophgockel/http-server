package de.christophgockel.httpserver;

import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.routes.Router;
import de.christophgockel.httpserver.routes.responders.*;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    HttpServer server = null;

    FileSystem fileSystem = new FileSystem("/Users/christoph/development/8thlight/cob_spec/public");

    final Router router = createRouter(fileSystem, 5000);


    try {
      server = new HttpServer(5000, "/Users/christoph/development/8thlight/cob_spec/public", router);
      server.start();
    } catch (IOException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      if (server != null) {
        server.stop();
      }
    }
  }

  private static Router createRouter(FileSystem fileSystem, int port) {
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
}
