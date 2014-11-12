package de.christophgockel.httpserver.http.method;

import de.christophgockel.httpserver.http.Request;

public interface Method {
  public String process(Request request);
}
