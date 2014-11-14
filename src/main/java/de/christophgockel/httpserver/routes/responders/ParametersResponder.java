package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ParametersResponder extends BaseResponder {
  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return method == RequestMethod.GET && path.equals("/parameters");
  }

  @Override
  protected Response respond(Request request) {
    String body = "<html><head><title>Parameters</title></head><body>";

    Response response = new Response(StatusCode.OK);
    response.addHeader("Content-Type", "text/html");

    try {
      URI uri = new URI(request.getURI());
      if (uri == null) {
        response.setBody("noe");
        return response;
      }
      String queryString = uri.getQuery();

      if (request.hasParameters()) {
        for (Map.Entry<String, String> parameter : request.getParameters().entrySet()) {
          body += parameter.getKey() + " = " + parameter.getValue() + "<br/>";
        }
      } else {
        body += "no parameters";
      }
      body += "</body></html>";

      response.setBody(body);
    } catch (URISyntaxException e) {
      response.setBody(e.getMessage());
    }

    return response;
  }
}
