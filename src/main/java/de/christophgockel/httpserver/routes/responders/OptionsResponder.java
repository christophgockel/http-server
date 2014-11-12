package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.http.Request;

public class OptionsResponder extends BaseResponder {
  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return method == RequestMethod.OPTIONS;
  }

  @Override
  protected String respond(Request request) {
    String response;

    response = "HTTP/1.1 200 OK\r\n";
    response += "Allow: GET,HEAD,POST,OPTIONS,PUT\n\r\n\r\n";

    return response;
  }
}
