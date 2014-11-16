package de.christophgockel.httpserver;

import de.christophgockel.httpserver.http.Request;

import java.util.ArrayList;
import java.util.List;

public class Logger {
  private static final List<String> entries = new ArrayList<>();

  public static void log(Request request) {
    String entry = request.getMethod().toString();
    entry += " " + request.getURI() + " ";
    entry += "HTTP/1.1";

    entries.add(entry);
  }

  public static List<String> getEntries() {
    return new ArrayList<>(entries);
  }
}
