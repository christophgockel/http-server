package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

public class PatchResponder extends BaseResponder {
  private final FileSystem fileSystem;

  public PatchResponder(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return method == RequestMethod.PATCH && path.equals("/patch-content.txt");
  }

  @Override
  protected Response respond(Request request) {
    String requestedResource = request.getURI();

    if (fileSystem.isFile(requestedResource)) {
      String requestSHA1 = request.getHeaders().get("If-Match");

      if (requestSHA1 == null) {
        return new Response(StatusCode.PRECONDITION_FAILED);
      }

      String resourceSHA1 = fileSystem.getSHA1ForFile(requestedResource);

      if (resourceSHA1.equals(requestSHA1)) {
        String content = request.getBody();
        fileSystem.setFileContent(requestedResource, content.getBytes());

        return new Response(StatusCode.NO_CONTENT);
      } else {
        return new Response(StatusCode.PRECONDITION_FAILED);
      }
    } else {
      return new Response(StatusCode.NOT_FOUND);
    }
  }
}
