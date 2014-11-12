package de.christophgockel.httpserver.http.method;

import de.christophgockel.httpserver.http.Request;

public class Options implements Method {
  @Override
  public String process(Request request) {
    String response;

    response = "HTTP/1.1 200 OK\r\n";
    response += "Allow: GET,HEAD,POST,OPTIONS,PUT\n\r\n\r\n";

    return response;
  }
}
