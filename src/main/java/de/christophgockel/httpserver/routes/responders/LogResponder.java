package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.Logger;
import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;

public class LogResponder extends BaseResponder {
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
    String authenticationData = request.getHeaders().get("Authorization");

    if (authenticationData != null) {
      String decodedCredentials = decodeCredentials(authenticationData);
      String[] parts = decodedCredentials.split("\\:");
      String username = parts[0];
      String password = parts[1];

      if (credentialsAreValid(username, password)) {
        return true;
      }
    }

    return false;
  }

  private String decodeCredentials(String authenticationData) {
    String encodedCredentials = authenticationData.split(" ")[1];
    return new String(Base64.decodeBase64(encodedCredentials.getBytes()), Charset.defaultCharset());
  }

  private boolean credentialsAreValid(String username, String password) {
    return username.equals("admin") && password.equals("hunter2");
  }
}
