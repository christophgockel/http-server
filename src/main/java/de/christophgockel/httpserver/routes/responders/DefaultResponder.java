package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;

import java.io.File;

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
  protected Response respond(Request request) {
    String body = "<html><head><title>Something</title></head><body>";

    String requestedResource = request.getURI();

    if (fileSystem.isFile(requestedResource)) {
      String mimeType = fileSystem.getMimeType(requestedResource);
      byte[] content = fileSystem.getFileContent(requestedResource);

      Response response = new Response(StatusCode.OK);
      response.addHeader("Content-Type", mimeType);
      response.setBody(content);

      return response;
    } else if (fileSystem.isDirectory(requestedResource)) {
      body += "<ul>";

      for (File file : fileSystem.getFiles(request.getURI())) {
        body += "<li>" + file.getName() + "</li>";
      }

      body += "</ul>";
      body += "</body></html>";

      Response response = new Response(StatusCode.OK);
      response.addHeader("Content-Type", "text/html");
      response.setBody(body);

      return response;
    } else {
      return new Response(StatusCode.NOT_FOUND);
    }
  }
}
