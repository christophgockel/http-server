package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.http.Request;

public abstract class BaseResponder {
  final public Response handle(Request request) {
    if (respondsTo(request.getMethod(), request.getURI())) {
      return respond(request);
    }

    return new Response(StatusCode.NOT_IMPLEMENTED);
  }

  abstract protected boolean respondsTo(RequestMethod method, String path);
  abstract protected Response respond(Request request);
}
