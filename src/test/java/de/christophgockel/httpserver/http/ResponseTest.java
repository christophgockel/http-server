package de.christophgockel.httpserver.http;

import org.junit.Test;

import java.io.IOException;

import static de.christophgockel.httpserver.StatusCode.NOT_FOUND;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ResponseTest {
  @Test
  public void hasAStatusCode() {
    Response response = new Response(NOT_FOUND);
    assertEquals(NOT_FOUND, response.getStatus());
  }

  @Test
  public void containsHeaders() {
    Response response = new Response();
    response.addHeader("a", "b");

    assertEquals(response.getHeaders().get("a"), "b");
  }

  @Test
  public void hasBody() {
    Response response = new Response();
    response.setBody("body".getBytes());

    assertArrayEquals(response.getBody(), "body".getBytes());
  }

  @Test
  public void canTakeStringBodyContent() {
    Response response = new Response();
    response.setBody("body");

    assertArrayEquals(response.getBody(), "body".getBytes());
  }

  @Test
  public void formatsResponse() throws IOException {
    Response response = new Response();
    response.addHeader("Content-Type", "text/plain");
    response.setBody("body");

    String expectedResponse = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/plain\r\n"
                            + "\r\n"
                            + "body";

    assertArrayEquals(response.getFullResponse(), expectedResponse.getBytes());
  }
}
