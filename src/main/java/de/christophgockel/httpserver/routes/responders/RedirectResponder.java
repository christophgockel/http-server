package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

public class RedirectResponder extends BaseResponder {
  private final int port;

  public RedirectResponder(int port) {
    this.port = port;
  }

  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return method == RequestMethod.GET && path.equals("/redirect");
  }

  @Override
  protected Response respond(Request request) {
    Response response = new Response(StatusCode.FOUND);
    response.addHeader("Location", "http://localhost:" + port + "/");

    return response;
  }
}
