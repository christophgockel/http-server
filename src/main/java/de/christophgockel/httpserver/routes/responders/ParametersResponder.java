package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.util.HtmlPage;

import java.util.Map;

public class ParametersResponder extends BaseResponder {
  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return method == RequestMethod.GET && path.equals("/parameters");
  }

  @Override
  protected Response respond(Request request) {
    HtmlPage page = new HtmlPage("Parameters");
    Response response = new Response(StatusCode.OK);
    response.addHeader("Content-Type", "text/html");

    if (request.hasParameters()) {
      for (Map.Entry<String, String> parameter : request.getParameters().entrySet()) {
        page.addParagraph(parameter.getKey() + " = " + parameter.getValue());
      }
    } else {
      page.addParagraph("No Parameters");
    }

    response.setBody(page.getContent());

    return response;
  }
}
