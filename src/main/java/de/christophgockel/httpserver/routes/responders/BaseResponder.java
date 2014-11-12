package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.http.Request;

public abstract class BaseResponder {
  final public String handle(Request request) {
    if (respondsTo(request.getMethod(), request.getURI())) {
      return respond(request);
    }

    return "HTTP/1.1 501 Not Implemented\r\n";
  }

  abstract protected boolean respondsTo(RequestMethod method, String path);
  abstract protected String respond(Request request);
}
