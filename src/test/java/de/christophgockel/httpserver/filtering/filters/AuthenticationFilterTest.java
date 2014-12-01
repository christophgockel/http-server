package de.christophgockel.httpserver.filtering.filters;

import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class AuthenticationFilterTest {
  private AuthenticationFilter filter;

  @Before
  public void setup() {
    filter = new AuthenticationFilter("some-name", "some-password");
  }

  @Test
  public void deniesRequestWhenNoAuthDataIsGiven() throws IOException {
    String content = "GET /logs HTTP/1.1\r\n";
    Request request = RequestHelper.requestFor(content);

    assertFalse(filter.filter(request));
  }

  @Test
  public void providesResponseForAuthentication() throws IOException {
    String content = "GET /logs HTTP/1.1\r\n";
    Request request = RequestHelper.requestFor(content);

    filter.filter(request);
    Response response = filter.getRejectionResponse();

    assertEquals(StatusCode.UNAUTHORIZED, response.getStatus());
    assertTrue(response.getHeaders().containsKey("WWW-Authenticate"));
    assertContains(response, "Authentication required");
  }

  @Test
  public void acceptsRequestIfAuthDataIsGiven() {
    AuthenticationFilter filter = new AuthenticationFilter("user", "pass");
    Request request = RequestHelper.requestFor(createAuthenticatedRequest("user", "pass"));

    assertTrue(filter.filter(request));
  }

  private String createAuthenticatedRequest(String username, String password) {
    String encodedCredentials = Base64.encodeBase64String((username + ":" + password).getBytes());
    return "GET /logs HTTP/1.1\r\n" +
           "Authorization: Basic " + encodedCredentials + "\r\n\r\n";
  }

  private void assertContains(Response response, String expected) throws IOException {
    assertThat(new String(response.getFullResponse()), containsString(expected));
  }
}
