package de.christophgockel.httpserver.http.method;

import de.christophgockel.httpserver.http.Request;

public class Post implements Method {
  @Override
  public String process(Request request) {
    String response;

    response = "HTTP/1.1 200 OK\r\n\r\n";

    return response;
  }
}
