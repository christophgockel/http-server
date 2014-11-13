package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.http.Request;

public class OptionsResponder extends BaseResponder {
  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return method == RequestMethod.OPTIONS;
  }

  @Override
  protected Response respond(Request request) {
    Response response = new Response(StatusCode.OK);
    response.addHeader("Allow", "GET,HEAD,POST,OPTIONS,PUT");

    return response;
  }
}
