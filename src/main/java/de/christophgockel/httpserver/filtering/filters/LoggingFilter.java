package de.christophgockel.httpserver.filtering.filters;

import de.christophgockel.httpserver.Logger;
import de.christophgockel.httpserver.http.Request;

public class LoggingFilter extends Filter {
  @Override
  public boolean filter(Request request) {
    Logger.log(request);
    return true;
  }
}
