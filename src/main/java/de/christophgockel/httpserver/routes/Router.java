package de.christophgockel.httpserver.routes;

import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.routes.responders.BaseResponder;

import java.util.HashMap;
import java.util.Map;

public class Router {
  private Map<String, BaseResponder> routes;
  private BaseResponder defaultResponder;

  public Router(BaseResponder defaultResponder) {
    this.defaultResponder = defaultResponder;
    this.routes = new HashMap<>();
  }

  public boolean matches(String path) {
    return routes.containsKey(path);
  }

  public void add(String path, BaseResponder responder) {
    routes.put(path, responder);
  }

  public byte[] dispatch(Request request) {
    BaseResponder responder = routes.get(request.getURI());

    if (responder == null) {
      return defaultResponder.handle(request);
    }

    return responder.handle(request);
  }
}
