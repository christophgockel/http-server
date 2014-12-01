package de.christophgockel.httpserver.routing;

import de.christophgockel.httpserver.controllers.Controller;
import de.christophgockel.httpserver.controllers.DummyController;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RouterTest {
  private Controller defaultController;
  private Router router;

  @Before
  public void setup() {
    defaultController = new DummyController();
    router = new Router(defaultController);
  }

  @Test
  public void returnsControllerOfSpecificRoute() {
    Controller controller = new DummyController();

    router.addRoute("/path", controller);
    Controller foundController = router.getController("/path");

    assertThat(foundController, is(controller));
  }

  @Test
  public void defaultsToDefaultControllerIfNoSpecificControllerAvailable() {
    Controller foundController = router.getController("/path");

    assertThat(foundController, is(defaultController));
  }
}
