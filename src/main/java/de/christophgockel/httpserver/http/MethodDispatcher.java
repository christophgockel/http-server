package de.christophgockel.httpserver.http;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.http.method.Method;
import de.christophgockel.httpserver.http.method.NotImplemented;

import java.util.HashMap;
import java.util.Map;

public class MethodDispatcher {
  private Map<RequestMethod, Method> methods;

  public MethodDispatcher() {
    methods = new HashMap<>();
  }

  public void addHandler(RequestMethod requestMethod, Method method) {
    methods.put(requestMethod, method);
  }

  public String process(Request request) {
    Method method = methods.get(request.getMethod());

    if (method == null) {
      return new NotImplemented().process(request);
    }

    return method.process(request);
  }
}
