package de.christophgockel.httpserver.filtering.filters;

import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.util.Authentication;

public class AuthenticationFilter extends Filter {
  private Authentication authentication;

  public AuthenticationFilter(String user, String password) {
    authentication = new Authentication(user, password);
  }

  @Override
  public boolean filter(Request request) {
    if (isAuthenticated(request)) {
      return true;
    }

    return false;
  }

  @Override
  public Response getRejectionResponse() {
    Response response = new Response(StatusCode.UNAUTHORIZED);

    response.addHeader("WWW-Authenticate", "Basic realm=\"HTTP Server\"");
    response.setBody("Authentication required\n".getBytes());

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
