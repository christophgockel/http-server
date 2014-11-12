package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.http.Request;

public class OptionsResponder extends BaseResponder {
  @Override
  public String handle(Request request) {
    String response;

    response = "HTTP/1.1 200 OK\r\n";
    response += "Allow: GET,HEAD,POST,OPTIONS,PUT\n\r\n\r\n";

    return response;
  }
}
