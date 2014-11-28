package de.christophgockel.httpserver.routing;

import de.christophgockel.httpserver.controllers.Controller;

import java.util.HashMap;
import java.util.Map;

public class Router {
  private final Controller defaultController;
  private Map<String, Controller> routes;

  public Router(Controller defaultController) {
    this.defaultController = defaultController;
    routes = new HashMap<>();
  }

  public void addRoute(String path, Controller controller) {
    routes.put(path, controller);
  }

  public Controller getController(String path) {
    Controller controller = routes.get(path);

    if (controller == null) {
      controller = defaultController;
    }

    return controller;
  }
}
