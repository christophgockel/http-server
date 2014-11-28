package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

import static de.christophgockel.httpserver.StatusCode.NOT_FOUND;
import static de.christophgockel.httpserver.StatusCode.NO_CONTENT;
import static de.christophgockel.httpserver.StatusCode.PRECONDITION_FAILED;

public class PatchController extends Controller {
  private final FileSystem fileSystem;

  public PatchController(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  protected Response get(Request request) {
    DefaultController defaultController = new DefaultController(fileSystem);

    return defaultController.dispatch(request);
  }

  @Override
  protected Response patch(Request request) {
    String requestedResource = request.getURI();

    if (fileSystem.isFile(requestedResource)) {
      return patchResponseForResource(request, requestedResource);
    } else {
      return new Response(NOT_FOUND);
    }
  }

  private Response patchResponseForResource(Request request, String requestedResource) {
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
  }
}
