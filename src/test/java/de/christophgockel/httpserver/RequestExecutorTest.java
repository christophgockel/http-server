package de.christophgockel.httpserver;

import de.christophgockel.httpserver.controllers.DummyController;
import de.christophgockel.httpserver.filtering.FilterChain;
import de.christophgockel.httpserver.filtering.FilterResult;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.http.StubClientSocket;
import de.christophgockel.httpserver.routing.Router;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class RequestExecutorTest {
  private OutputStream outputStream;
  private RequestExecutor executor;

  public void prepareExecutorFor(String request) throws IOException {
    StubClientSocket socket = new StubClientSocket(request);
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

  @Test(expected = RuntimeException.class)
  public void throwsRuntimeErrorWhenCatchHandlerCannotDealWithFailuresAnymore() {
    ThrowingClientSocket socket = new ThrowingClientSocket();
    executor = new RequestExecutor(socket, new Router(new DummyController()));
    executor.run();
  }

  @Test
  public void returnsResponseFromDenyingFilter() throws IOException {
    FilterChain filters = new DenyingFilterChain();
    StubClientSocket socket = new StubClientSocket("GET / HTTP/1.1");
    outputStream = socket.getOutputStream();
    executor = new RequestExecutor(socket, new Router(new DummyController()), filters);
    executor.run();
    assertThat(outputStream.toString(), containsString("Not Implemented"));
  }

  private class ThrowingClientSocket extends StubClientSocket {
    public ThrowingClientSocket() {
      super("");
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
      return new OutputStream() {
        @Override
        public void write(int b) throws IOException {
          throw new IOException();
        }
      };
    }

    @Override
    public void close() throws IOException {
    }
  }

  private class DenyingFilterChain extends FilterChain {
    @Override
    public FilterResult filter(Request request) {
      return FilterResult.invalidWithResponse(new Response(StatusCode.NOT_IMPLEMENTED));
    }
  }
}
