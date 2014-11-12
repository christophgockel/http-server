package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.http.Request;

public class BaseResponder {
  public String handle(Request request) {
    return "HTTP/1.1 501 Not Implemented\r\n";
  }
}
