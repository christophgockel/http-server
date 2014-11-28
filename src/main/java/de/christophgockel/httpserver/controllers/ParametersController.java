package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.util.HtmlPage;

import java.util.Map;

public class ParametersController extends Controller {
  @Override
  protected Response get(Request request) {
    Response response = new Response(StatusCode.OK);
    HtmlPage page = new HtmlPage("Parameters");

    response.addHeader("Content-Type", "text/html");

    if (request.hasParameters()) {
      addParameters(page, request.getParameters());
    } else {
      page.addParagraph("No Parameters");
    }

    response.setBody(page.getContent());

    return response;
  }

  private void addParameters(HtmlPage page, Map<String, String> parameters) {
    for (Map.Entry<String, String> parameter : parameters.entrySet()) {
      page.addParagraph(parameter.getKey() + " = " + parameter.getValue());
    }
  }
}
