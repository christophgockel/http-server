package de.christophgockel.httpserver;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    HttpServer server = null;

    try {
      server = new HttpServer(5000, "/Users/christoph/development/8thlight/cob_spec/public");
      server.start();
    } catch (IOException e) {
      if (server != null) {
        server.stop();
      }
    }
  }
}
