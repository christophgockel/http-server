package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

import java.util.Arrays;

import static de.christophgockel.httpserver.StatusCode.PARTIAL_CONTENT;

public class PartialController extends Controller {
  private final FileSystem fileSystem;

  public PartialController(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  protected Response get(Request request) {
    DefaultController defaultController = new DefaultController(fileSystem);
    Response response = defaultController.dispatch(request);

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
