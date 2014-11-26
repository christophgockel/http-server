package de.christophgockel.httpserver.routes;

import de.christophgockel.httpserver.routes.responders.NonRespondingResponder;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RouterTest {
  @Test
  public void doesNotMatchUnknownRoutes() {
    Router router = new Router(new NonRespondingResponder());

    assertFalse(router.matches("/some/path"));
  }

  @Test
  public void matchesKnownRoutes() {
    Router router = new Router(new NonRespondingResponder());

    router.add("/the/route", new NonRespondingResponder());

    assertTrue(router.matches("/the/route"));
  }
}
