package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

import java.util.Arrays;

import static de.christophgockel.httpserver.StatusCode.PARTIAL_CONTENT;

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

    String headerContent = request.getHeaders().get("Range");

    if (headerContent != null) {
      return partialResponse(headerContent, response);
    }

    return response;
  }

  private Response partialResponse(String rangeHeader, Response originalResponse) {
      byte[] originalBody = originalResponse.getBody();

      String rangeDefinition = getRangeDefinition(rangeHeader);

      if (rangeCanBeSplit(rangeDefinition)) {
        String[] byteRanges = rangeDefinition.split("\\-");

        int rangeStart = Integer.parseInt(byteRanges[0]);
        int rangeEnd = Integer.parseInt(byteRanges[1]);

        byte[] body = Arrays.copyOfRange(originalBody, rangeStart, rangeEnd + 1);

        return partialResponseWith(body);
      }

    return originalResponse;
  }

  private Response partialResponseWith(byte[] body) {
    Response response = new Response(PARTIAL_CONTENT);
    response.setBody(body);

    return response;
  }

  private boolean rangeCanBeSplit(String rangeDefinition) {
    return rangeDefinition.contains("-");
  }

  private String getRangeDefinition(String header) {
    return header.split("=")[1];
  }
}
