package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

public class OptionsController extends Controller {
  @Override
  protected Response options(Request request) {
    Response response = new Response(StatusCode.OK);
    response.addHeader("Allow", "GET,HEAD,POST,OPTIONS,PUT");

    return response;
  }
}
