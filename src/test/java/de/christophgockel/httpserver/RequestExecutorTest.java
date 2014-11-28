package de.christophgockel.httpserver;

import de.christophgockel.httpserver.http.StubSocket;
import de.christophgockel.httpserver.routing.Router;
import de.christophgockel.httpserver.controllers.DummyController;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

public class RequestExecutorTest {
  private OutputStream outputStream;
  private RequestExecutor executor;

  public void prepareExecutorFor(String request) throws IOException {
    StubSocket socket = new StubSocket(request);
    outputStream = socket.getOutputStream();
    executor = new RequestExecutor(socket, new Router(new DummyController()));
  }

  @Test
  public void invalidRequestsResultInInternalServerErrors() throws IOException {
    prepareExecutorFor("invalid request");
    executor.run();
    assertThat(outputStream.toString(), containsString("Internal Server Error"));
  }

  @Test
  public void unknownResourcesResultInNotAllowed() throws IOException {
    prepareExecutorFor("GET /unknown HTTP/1.1");
    executor.run();
    assertThat(outputStream.toString(), containsString("Not Allowed"));
  }

  @Test
  public void logsEveryRequest() throws IOException {
    Logger.clear();
    prepareExecutorFor("GET /something HTTP/1.1");
    executor.run();
    assertEquals("GET /something HTTP/1.1", Logger.getEntries().get(0));
  }

  @Test(expected = RuntimeException.class)
  public void throwsRuntimeErrorWhenCatchHandlerCannotDealWithFailuresAnymore() {
    ThrowingClientSocket socket = new ThrowingClientSocket();
    executor = new RequestExecutor(socket, new Router(new DummyController()));
    executor.run();
  }

  private class ThrowingClientSocket extends StubSocket {
    public ThrowingClientSocket() {
      super("");
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
      return new ThrowingOutputStream();
    }

    @Override
    public void close() throws IOException {
    }
  }

  private class ThrowingOutputStream extends OutputStream {
    @Override
    public void write(int b) throws IOException {
      throw new IOException();
    }
  }
}
