package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RedirectControllerTest {

  private RedirectController controller;

  @Before
  public void setup() {
    controller = new RedirectController(1234);
  }

  @Test
  public void respondsWith302RedirectToRoot() {
    String content = "GET /redirect HTTP/1.1\r\n" +
                     "Host: www.somehost.com\r\n\r\n";
    Request request = RequestHelper.requestFor(content);

    Response response = controller.dispatch(request);

    assertEquals(StatusCode.FOUND, response.getStatus());
    assertEquals("http://www.somehost.com:1234/", response.getHeaders().get("Location"));
  }

  @Test
  public void redirectsWithPortFromRequest() {
    String content = "GET /redirect HTTP/1.1\r\n" +
                     "Host: www.somehost.com:8080\r\n\r\n";
    Request request = RequestHelper.requestFor(content);

    Response response = controller.dispatch(request);

    assertEquals(StatusCode.FOUND, response.getStatus());
    assertEquals("http://www.somehost.com:8080/", response.getHeaders().get("Location"));
  }

  @Test
  public void redirectsToLocalhostWhenNoHostIsGivenInRequest() {
    String content = "GET /redirect HTTP/1.0\r\n";
    Request request = RequestHelper.requestFor(content);

    Response response = controller.dispatch(request);

    assertEquals(StatusCode.FOUND, response.getStatus());
    assertEquals("http://localhost:1234/", response.getHeaders().get("Location"));
  }
}
