package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;

import java.io.File;

public class DefaultResponder extends BaseResponder {
  private final FileSystem fileSystem;

  public DefaultResponder(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  public String handle(Request request) {
    String body = "<html><head><title>Something</title></head><body>";

    body += "<ul>";
    for (File file : fileSystem.getFiles()) {
      body += "<li>" + file.getName() + "</li>";
    }
    body += "</ul>";
    body += "</body></html>";

    return "HTTP/1.1 200 OK\r\n" +
           "Content-Type: text/html\r\n" +
           "\r\n" +
           body;
  }
}
