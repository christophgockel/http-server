package de.christophgockel.httpserver.filtering.filters;

import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

public abstract class Filter {
  protected Response rejectingResponse;

  public abstract boolean filter(Request request);

  public Response getRejectionResponse() {
    return rejectingResponse;
  }
}
