package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class DefaultResponder extends BaseResponder {
  private final FileSystem fileSystem;

  public DefaultResponder(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return method == RequestMethod.GET;
  }

  @Override
  protected byte[] respond(Request request) {
    String body = "<html><head><title>Something</title></head><body>";

    String requestedResource = request.getURI();

    if (fileSystem.isFile(requestedResource)) {
      String mimeType = fileSystem.getMimeType(requestedResource);
      byte[] content = fileSystem.getFileContent(requestedResource);


      byte[] header = ("HTTP/1.1 200 OK\r\n" +
        "Content-Type: " + mimeType + "\r\n" +
        "\r\n").getBytes();

      ByteArrayOutputStream response = new ByteArrayOutputStream();
      response.write(header, 0, header.length);
      response.write(content, 0, content.length);

      byte[] thing = response.toByteArray();
      try {
        response.close();
      } catch (IOException e) {
        return "".getBytes();
      }

      return thing;
    } else if (fileSystem.isDirectory(requestedResource)) {
      body += "<ul>";

      for (File file : fileSystem.getFiles(request.getURI())) {
        body += "<li>" + file.getName() + "</li>";
      }

      body += "</ul>";
      body += "</body></html>";

      return ("HTTP/1.1 200 OK\r\n" +
        "Content-Type: text/html\r\n" +
        "\r\n" +
        body).getBytes();
    } else {
      return "HTTP/1.1 404 Not Found\r\n".getBytes();
    }
  }
}
