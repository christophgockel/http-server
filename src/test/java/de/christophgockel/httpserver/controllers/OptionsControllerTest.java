package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.assertEquals;

public class OptionsControllerTest {
  @Test
  public void respondsWithPossibleOptions() throws IOException {
    OptionsController controller = new OptionsController();
    Request request = RequestHelper.requestFor("OPTIONS /path HTTP/1.1");

    Response response = controller.dispatch(request);
    String header = response.getHeaders().get("Allow");

    assertEquals(StatusCode.OK, controller.dispatch(request).getStatus());
    assertThat(header, not(isEmptyString()));
  }
}
