package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

import static de.christophgockel.httpserver.RequestMethod.GET;
import static de.christophgockel.httpserver.RequestMethod.PATCH;
import static de.christophgockel.httpserver.StatusCode.*;

public class PatchResponder extends BaseResponder {
  private final FileSystem fileSystem;

  public PatchResponder(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return path.equals("/patch-content.txt")
      && (method == PATCH
       || method == GET);
  }

  @Override
  protected Response respond(Request request) {
    if (isGetRequest(request)) {
      return get(request);
    } else {
      return patch(request);
    }
  }

  private Response get(Request request) {
    DefaultResponder defaultResponder = new DefaultResponder(fileSystem);

    return defaultResponder.respond(request);
  }

  private boolean isGetRequest(Request request) {
    return request.getMethod() == GET;
  }

  private Response patch(Request request) {
    String requestedResource = request.getURI();

    if (fileSystem.isFile(requestedResource)) {
      String requestSHA1 = request.getHeaders().get("If-Match");

      if (requestSHA1 == null) {
        return new Response(PRECONDITION_FAILED);
      }

      String resourceSHA1 = fileSystem.getSHA1ForFile(requestedResource);

      if (resourceSHA1.equals(requestSHA1)) {
        String content = request.getBody();
        fileSystem.setFileContent(requestedResource, content.getBytes());

        return new Response(NO_CONTENT);
      } else {
        return new Response(PRECONDITION_FAILED);
      }
    } else {
      return new Response(NOT_FOUND);
    }
  }
}
