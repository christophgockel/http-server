package de.christophgockel.httpserver.routes;

import de.christophgockel.httpserver.routes.responders.DummyResponder;
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

    router.add("/the/route", new DummyResponder());

    assertTrue(router.matches("/the/route"));
  }
}
