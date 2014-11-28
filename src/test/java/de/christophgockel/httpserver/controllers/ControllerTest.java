package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ControllerTest {
  @Test
  public void doesNotAllowAnyRequestByDefault() {
    Controller controller = new Controller();

    assertNotAllowed(controller.dispatch(getGetRequest()));
    assertNotAllowed(controller.dispatch(getPostRequest()));
    assertNotAllowed(controller.dispatch(getPutRequest()));
    assertNotAllowed(controller.dispatch(getHeadRequest()));
    assertNotAllowed(controller.dispatch(getDeleteRequest()));
    assertNotAllowed(controller.dispatch(getTraceRequest()));
    assertNotAllowed(controller.dispatch(getConnectRequest()));
    assertNotAllowed(controller.dispatch(getOptionsRequest()));
    assertNotAllowed(controller.dispatch(getPatchRequest()));
  }

  private void assertNotAllowed(Response response) {
    assertEquals(StatusCode.NOT_ALLOWED, response.getStatus());
  }

  private Request getGetRequest() {
    return RequestHelper.requestFor("GET / HTTP/1.1");
  }

  private Request getPostRequest() {
    return RequestHelper.requestFor("POST / HTTP/1.1");
  }

  private Request getConnectRequest() {
    return RequestHelper.requestFor("CONNECT / HTTP/1.1");
  }

  private Request getTraceRequest() {
    return RequestHelper.requestFor("TRACE / HTTP/1.1");
  }

  private Request getDeleteRequest() {
    return RequestHelper.requestFor("DELETE / HTTP/1.1");
  }

  private Request getHeadRequest() {
    return RequestHelper.requestFor("HEAD / HTTP/1.1");
  }

  private Request getPutRequest() {
    return RequestHelper.requestFor("PUT / HTTP/1.1");
  }

  private Request getPatchRequest() {
    return RequestHelper.requestFor("PATCH / HTTP/1.1");
  }

  private Request getOptionsRequest() {
    return RequestHelper.requestFor("OPTIONS / HTTP/1.1");
  }
}
