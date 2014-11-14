package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

import java.io.File;

import static de.christophgockel.httpserver.RequestMethod.*;
import static de.christophgockel.httpserver.StatusCode.*;

public class DefaultResponder extends BaseResponder {
  private final FileSystem fileSystem;

  public DefaultResponder(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return true;
  }

  @Override
  protected Response respond(Request request) {
    if (isGetRequest(request)) {
      return serveGetRequest(request);
    } else {
      return notAllowed();
    }
  }

  private boolean isGetRequest(Request request) {
    return request.getMethod() == GET;
  }

  private Response serveGetRequest(Request request) {
    String requestedResource = request.getURI();

    if (fileSystem.isFile(requestedResource)) {
      return serveFile(requestedResource);
    } else if (fileSystem.isDirectory(requestedResource)) {
      return serveDirectoryListing(requestedResource);
    } else {
      return new Response(NOT_FOUND);
    }
  }

  private Response serveDirectoryListing(String requestedResource) {
    String body = "<html><head><title>" + requestedResource + "</title></head><body>";

    body += "<ul>";

    for (File file : fileSystem.getFiles(requestedResource)) {
      body += "<li>";
      body += "<a href=\"" + file.getName() + "\">" + file.getName() + "</a>";
      body += "</li>";
    }

    body += "</ul>";
    body += "</body></html>";

    Response response = new Response(OK);
    response.addHeader("Content-Type", "text/html");
    response.setBody(body);

    return response;
  }

  private Response serveFile(String requestedResource) {
    String mimeType = fileSystem.getMimeType(requestedResource);
    byte[] content = fileSystem.getFileContent(requestedResource);

    Response response = new Response(OK);
    response.addHeader("Content-Type", mimeType);
    response.setBody(content);

    return response;
  }

  private Response notAllowed() {
    Response response = new Response(NOT_ALLOWED);
    response.addHeader("Allow", GET.toString());
    return response;
  }
}
