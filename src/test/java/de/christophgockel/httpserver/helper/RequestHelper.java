package de.christophgockel.httpserver.helper;

import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.RequestParser;

import java.io.ByteArrayInputStream;

public class RequestHelper {
  public static Request requestFor(String content) {
    return new RequestParser().parse(new ByteArrayInputStream(content.getBytes()));
  }
}
