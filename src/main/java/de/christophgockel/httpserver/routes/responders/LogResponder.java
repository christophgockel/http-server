package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.Logger;
import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.util.Authentication;

public class LogResponder extends BaseResponder {
  private Authentication authentication;

  public LogResponder() {
    authentication = new Authentication("admin", "hunter2");
  }

  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return path.equals("/logs")
        && (method == RequestMethod.GET
         || method == RequestMethod.PUT
         || method == RequestMethod.HEAD);
  }

  @Override
  protected Response respond(Request request) {
    Response response;

    if (isAuthenticated(request)) {
      response = new Response(StatusCode.OK);
      String content = "";
      for (String entry : Logger.getEntries()) {
        content += entry + "<br/>";
      }
      response.setBody(content.getBytes());
    } else {
      response = new Response(StatusCode.UNAUTHORIZED);
      response.addHeader("WWW-Authenticate", "Basic realm=\"HTTP Server\"");
      response.setBody("Authentication required\n".getBytes());
    }

    return response;
  }

  private boolean isAuthenticated(Request request) {
    String credentials = getCredentials(request);
    return authentication.isAuthenticated(credentials);
  }

  private String getCredentials(Request request) {
    String authenticationData = request.getHeaders().get("Authorization");

    if (authenticationData == null || authenticationData.equals("")) {
      return "";
    }

    return authenticationData.split(" ")[1];
  }
}
