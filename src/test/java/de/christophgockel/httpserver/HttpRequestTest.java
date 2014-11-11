package de.christophgockel.httpserver;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HttpRequestTest {
  @Test
  public void knowsTheDifferentRequestMethods() {
    assertEquals(RequestMethod.GET,      methodFor("GET / HTTP/1.1"));
    assertEquals(RequestMethod.POST,     methodFor("POST / HTTP/1.1"));
    assertEquals(RequestMethod.HEAD,     methodFor("HEAD / HTTP/1.1"));
    assertEquals(RequestMethod.DELETE,   methodFor("DELETE / HTTP/1.1"));
    assertEquals(RequestMethod.TRACE,    methodFor("TRACE / HTTP/1.1"));
    assertEquals(RequestMethod.CONNECT,  methodFor("CONNECT / HTTP/1.1"));
    assertEquals(RequestMethod.OPTIONS,  methodFor("OPTIONS / HTTP/1.1"));
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

    assertEquals("var=value", requestFor(request).getBody());
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

    HttpRequest request = requestFor(content);

    assertEquals(RequestMethod.PUT, request.getMethod());
    assertEquals("/", request.getURI());
    assertEquals(headers, request.getHeaders());
    assertEquals("some body", request.getBody());
  }

  @Test(expected = HttpRequest.MalformedException.class)
  public void throwsExceptionOnMalformedRequest() {
    requestFor("");
  }

  @Test(expected = RequestMethod.UnknownMethod.class)
  public void throwsExceptionForUnknownMethods() {
    methodFor("SOMETHING / HTTP/1.1");
  }

  @Test(expected = HttpRequest.MalformedException.class)
  public void throwsExceptionOnMalformedRequestLine() {
    requestFor("GET");
  }

  @Test(expected = HttpRequest.MalformedException.class)
  public void throwExceptionForMalformedHeaders() {
    String content = "PUT / HTTP/1.1\r\n" +
                     "Accept-Charset";
    requestFor(content);
  }

  private RequestMethod methodFor(String requestLine) {
    return requestFor(requestLine).getMethod();
  }

  private String uriFor(String requestLine) {
    return requestFor(requestLine).getURI();
  }

  private HttpRequest requestFor(String requestLine) {
    return new HttpRequest(new ByteArrayInputStream(requestLine.getBytes()));
  }
}
