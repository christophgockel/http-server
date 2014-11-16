package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

import java.util.Arrays;

public class PartialResponder extends BaseResponder {
  private final FileSystem fileSystem;

  public PartialResponder(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return method == RequestMethod.GET && path.equals("/partial_content.txt");
  }

  @Override
  protected Response respond(Request request) {
    DefaultResponder responder = new DefaultResponder(fileSystem);
    Response response = responder.respond(request);

    return partialResponse(request, response);
  }

  private Response partialResponse(Request request, Response response) {
    String headerData = request.getHeaders().get("Range");
    Response partialResponse = response;

    if (headerData != null) {
      partialResponse = new Response(StatusCode.PARTIAL_CONTENT);
      byte[] originalBody = response.getBody();
      String range = headerData.split("=")[1];
      String[] byteRange = range.split("\\-");
      int rangeStart = Integer.parseInt(byteRange[0]);
      int rangeEnd   = Integer.parseInt(byteRange[1]);

      byte[] newBody = Arrays.copyOfRange(originalBody, rangeStart, rangeEnd + 1);

      partialResponse.setBody(newBody);
    }
    return partialResponse;
  }
}
