package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.http.Request;

public class DummyResponder extends BaseResponder {
  @Override
  boolean respondsTo(RequestMethod method, String path) {
    return true;
  }

  @Override
  String respond(Request request) {
    return "";
  }
}
