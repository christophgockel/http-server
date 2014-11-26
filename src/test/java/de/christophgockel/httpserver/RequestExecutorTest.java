package de.christophgockel.httpserver;

import de.christophgockel.httpserver.routes.Router;
import de.christophgockel.httpserver.routes.responders.NonRespondingResponder;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

public class RequestExecutorTest {
  private OutputStream outputStream;
  private RequestExecutor executor;

  public void prepareExecutorFor(String request) throws IOException {
    InputStream inputStream = new ByteArrayInputStream(request.getBytes());
    outputStream = new ByteArrayOutputStream();
    Socket mockedSocket = mock(Socket.class);
    when(mockedSocket.getInputStream()).thenReturn(inputStream);
    when(mockedSocket.getOutputStream()).thenReturn(outputStream);

    executor = new RequestExecutor(mockedSocket, new Router(new NonRespondingResponder()));
  }

  @Test
  public void invalidRequestsResultInInternalServerErrors() throws IOException {
    prepareExecutorFor("invalid request");
    executor.run();
    assertThat(outputStream.toString(), containsString("Internal Server Error"));
  }

  @Test
  public void unknownResourcesResultInNotImplemented() throws IOException {
    prepareExecutorFor("GET /unknown HTTP/1.1");
    executor.run();
    assertThat(outputStream.toString(), containsString("Not Implemented"));
  }

  @Test
  public void logsEveryRequest() throws IOException {
    prepareExecutorFor("GET /something HTTP/1.1");
    executor.run();
    assertEquals("GET /something HTTP/1.1", Logger.getEntries().get(0));
  }
}
