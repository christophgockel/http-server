package de.christophgockel.httpserver.helper;

import de.christophgockel.httpserver.http.Request;

import java.io.ByteArrayInputStream;

public class RequestHelper {
  public static Request requestFor(String content) {
    return new Request(new ByteArrayInputStream(content.getBytes()));
  }
}
