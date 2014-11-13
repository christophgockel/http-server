package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.http.Request;

public class DummyResponder extends BaseResponder {
  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return true;
  }

  @Override
  protected byte[] respond(Request request) {
    return "".getBytes();
  }
}
