package de.christophgockel.httpserver.routes;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.routes.responders.BaseResponder;
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

  @Test
  public void dispatchesRequestToMatchingResponder() {
    Router router = new Router(new NonRespondingResponder());
    SpyingResponder spyingResponder = new SpyingResponder();

    router.add("/the/route", spyingResponder);

    Request request = RequestHelper.requestFor("GET /the/route HTTP/1.1");
    router.dispatch(request);

    assertTrue(spyingResponder.respondHasBeenCalled());
  }

  @Test
  public void dispatchesToDefaultResponderItNoRouteMatches() {
    SpyingResponder spyingResponder = new SpyingResponder();
    Router router = new Router(spyingResponder);

    Request request = RequestHelper.requestFor("GET /some/route HTTP/1.1");
    router.dispatch(request);

    assertTrue(spyingResponder.respondHasBeenCalled());
  }

  public class SpyingResponder extends BaseResponder {
    private boolean respondHasBeenCalled = false;

    @Override
    protected boolean respondsTo(RequestMethod method, String path) {
      return true;
    }

    @Override
    protected Response respond(Request request) {
      respondHasBeenCalled = true;
      return new Response();
    }

    public boolean respondHasBeenCalled() {
      return respondHasBeenCalled;
    }
  }
}
