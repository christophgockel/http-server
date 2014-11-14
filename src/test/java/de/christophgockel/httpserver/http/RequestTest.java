package de.christophgockel.httpserver.http;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.helper.RequestHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RequestTest {
  @Test
  public void knowsTheDifferentRequestMethods() {
    Assert.assertEquals(RequestMethod.GET, methodFor("GET / HTTP/1.1"));
    assertEquals(RequestMethod.POST, methodFor("POST / HTTP/1.1"));
    assertEquals(RequestMethod.HEAD, methodFor("HEAD / HTTP/1.1"));
    assertEquals(RequestMethod.DELETE, methodFor("DELETE / HTTP/1.1"));
    assertEquals(RequestMethod.TRACE, methodFor("TRACE / HTTP/1.1"));
    assertEquals(RequestMethod.CONNECT, methodFor("CONNECT / HTTP/1.1"));
    assertEquals(RequestMethod.OPTIONS, methodFor("OPTIONS / HTTP/1.1"));
  }

  @Test
  public void providesURIInformations() {
    assertEquals("/some/long/path", uriFor("GET /some/long/path HTTP/1.1"));
    assertEquals("https://www.google.de", uriFor("GET https://www.google.de HTTP/1.1"));
    assertEquals("*", uriFor("OPTIONS * HTTP/1.1"));
  }

  @Test
  public void parsesHeaders() {
    String request = "GET / HTTP/1.1\r\n" +
                     "Host: www.google.co.uk\r\n" +
                     "Accept-Charset: utf-8";
    Map<String, String> headers = new HashMap<>();
    headers.put("Host", "www.google.co.uk");
    headers.put("Accept-Charset", "utf-8");

    assertEquals(headers, requestFor(request).getHeaders());
  }

  @Test
  public void ignoresBodyContentIfContentLengthHeaderIsMissing() {
    String request = "POST / HTTP/1.1\r\n" +
                     "\r\n" +
                     "var=value";

    assertEquals("", requestFor(request).getBody());
  }

  @Test
  public void returnsBodyContentIfContentLengthHeaderIsPresent() {
    String request = "POST / HTTP/1.1\r\n" +
                     "Content-Length: 9\r\n" +
                     "\r\n" +
                     "var=value";

    assertThat(requestFor(request).getBody(), containsString("var=value"));
  }

  @Test
  public void parsesFullRequest() {
    String content = "PUT / HTTP/1.1\r\n" +
                     "Accept-Charset: utf-8\r\n" +
                     "Content-Length: 9\r\n" +
                     "\r\n" +
                     "some body";
    Map<String, String> headers = new HashMap<>();
    headers.put("Accept-Charset", "utf-8");
    headers.put("Content-Length", "9");

    Request request = requestFor(content);

    assertEquals(RequestMethod.PUT, request.getMethod());
    assertEquals("/", request.getURI());
    assertEquals(headers, request.getHeaders());
    assertThat(request.getBody(), containsString("some body"));
  }

  @Test(expected = Request.MalformedException.class)
  public void throwsExceptionOnMalformedRequest() {
    requestFor("");
  }

  @Test(expected = RequestMethod.UnknownMethod.class)
  public void throwsExceptionForUnknownMethods() {
    methodFor("SOMETHING / HTTP/1.1");
  }

  @Test(expected = Request.MalformedException.class)
  public void throwsExceptionOnMalformedRequestLine() {
    requestFor("GET");
  }

  @Test(expected = Request.MalformedException.class)
  public void throwExceptionForMalformedHeaders() {
    String content = "PUT / HTTP/1.1\r\n" +
                     "Accept-Charset";
    requestFor(content);
  }

  @Test
  public void returnsURIWithoutParameters() {
    String content = "GET /resource?parameter=value HTTP/1.1\r\n";
    Request request = requestFor(content);

    assertEquals("/resource", request.getURI());
  }

  @Test
  public void parsesURIParameters() {
    String content = "GET /resource?parameter=value&other=%20%3C%2C%20 HTTP/1.1\r\n";
    Request request = requestFor(content);

    assertTrue(request.hasParameters());
    assertEquals("value", request.getParameters().get("parameter"));
    assertEquals(" <, ", request.getParameters().get("other"));
  }

  private RequestMethod methodFor(String requestLine) {
    return requestFor(requestLine).getMethod();
  }

  private String uriFor(String requestLine) {
    return requestFor(requestLine).getURI();
  }

  private Request requestFor(String requestLine) {
    return RequestHelper.requestFor(requestLine);
  }
}
