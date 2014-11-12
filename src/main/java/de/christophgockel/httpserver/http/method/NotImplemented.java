package de.christophgockel.httpserver.http.method;

import de.christophgockel.httpserver.http.Request;

public class NotImplemented implements Method {
  @Override
  public String process(Request request) {
    return "HTTP/1.1 501 Not Implemented";
  }
}
