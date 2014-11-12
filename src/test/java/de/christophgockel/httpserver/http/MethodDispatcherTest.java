package de.christophgockel.httpserver.http;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.method.Method;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;

public class MethodDispatcherTest {
  @Test
  public void dispatchesToAnotherHandler() {
    MethodDispatcher dispatcher = new MethodDispatcher();
    SpyMethod method = new SpyMethod();

    dispatcher.addHandler(RequestMethod.GET, method);
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");
    dispatcher.process(request);

    assertTrue(method.hasBeenCalled());
  }

  @Test
  public void returnsNotImplementedResponseForUnsupportedMethods() {
    MethodDispatcher dispatcher = new MethodDispatcher();
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");

    String response = dispatcher.process(request);

    assertThat(response, containsString("Not Implemented"));
  }

  private class SpyMethod implements Method {
    private boolean handlerHasBeenCalled = false;

    @Override
    public String process(Request request) {
      handlerHasBeenCalled = true;
      return "";
    }

    public boolean hasBeenCalled() {
      return handlerHasBeenCalled;
    }
  }
}
