package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.http.Request;

public class NonRespondingResponder extends BaseResponder {
  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return false;
  }

  @Override
  protected Response respond(Request request) {
    return new Response();
  }
}
