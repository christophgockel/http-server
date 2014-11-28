package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

import static de.christophgockel.httpserver.StatusCode.NOT_ALLOWED;

public class Controller {
  public final Response dispatch(Request request) {
    switch (request.getMethod()) {
      case GET:
        return get(request);
      case PUT:
        return put(request);
      case POST:
        return post(request);
      case HEAD:
        return head(request);
      case DELETE:
        return delete(request);
      case TRACE:
        return trace(request);
      case CONNECT:
        return connect(request);
      case OPTIONS:
        return options(request);
      case PATCH:
        return patch(request);
    }
    return null;
  }

  protected Response get(Request request) {
    return new Response(NOT_ALLOWED);
  }

  protected Response post(Request request) {
    return new Response(NOT_ALLOWED);
  }

  protected Response put(Request request) {
    return new Response(NOT_ALLOWED);
  }

  protected Response head(Request request) {
    return new Response(NOT_ALLOWED);
  }

  protected Response delete(Request request) {
    return new Response(NOT_ALLOWED);
  }

  protected Response trace(Request request) {
    return new Response(NOT_ALLOWED);
  }

  protected Response connect(Request request) {
    return new Response(NOT_ALLOWED);
  }

  protected Response options(Request request) {
    return new Response(NOT_ALLOWED);
  }

  protected Response patch(Request request) {
    return new Response(NOT_ALLOWED);
  }
}
