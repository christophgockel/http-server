package de.christophgockel.httpserver.routes;

import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.routes.responders.BaseResponder;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RouterTest {
  @Test
  public void doesNotMatchUnknownRoutes() {
    Router router = new Router(new DummyResponder());

    assertFalse(router.matches("/some/path"));
  }

  @Test
  public void matchesKnownRoutes() {
    Router router = new Router(new DummyResponder());

    router.add("/the/route", new BaseResponder());

    assertTrue(router.matches("/the/route"));
  }

  private class DummyResponder extends BaseResponder {
    @Override
    public String handle(Request request) {
      return "";
    }
  }
}
