package de.christophgockel.httpserver;

import de.christophgockel.httpserver.controllers.Controller;
import de.christophgockel.httpserver.filtering.FilterChain;
import de.christophgockel.httpserver.filtering.FilterResult;
import de.christophgockel.httpserver.http.*;
import de.christophgockel.httpserver.routing.Router;

import java.io.DataOutputStream;
import java.io.IOException;

public class RequestExecutor implements Runnable {
  private final ClientSocket socket;
  private final Router router;
  private final FilterChain filters;
  private DataOutputStream out;

  public RequestExecutor(ClientSocket socket, Router router) {
    this(socket, router, new FilterChain());
  }

  public RequestExecutor(ClientSocket socket, Router router, FilterChain filters) {
    this.socket = socket;
    this.router = router;
    this.filters = filters;
  }

  @Override
  public void run() {
    try {
      out = new DataOutputStream(socket.getOutputStream());

      RequestParser parser = new RequestParser();
      Request request = parser.parse(socket.getInputStream());

      FilterResult result = filters.filter(request);

      if (result.isValid()) {
        Controller controller = router.getController(request.getURI());
        out.write(controller.dispatch(request).getFullResponse());
      } else {
        out.write(result.getRejectionResponse().getFullResponse());
      }

      out.close();
      socket.close();
    } catch (Exception e) {
      sendError(e.getMessage());
    }
  }

  private void sendError(String message) {
    try {
      Response response = new Response(StatusCode.INTERNAL_SERVER_ERROR);
      response.setBody(message);
      out.write(response.getFullResponse());
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
