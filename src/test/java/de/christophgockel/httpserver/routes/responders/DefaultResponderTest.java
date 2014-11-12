package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class DefaultResponderTest {
  @Rule
  public TemporaryFolder documentRoot = new TemporaryFolder();
  private BaseResponder responder;

  @Before
  public void setup() throws IOException {
    responder = new DefaultResponder(new FileSystem(documentRoot.getRoot()));
  }

  @Test
  public void respondsWith200OK() throws IOException {
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");

    assertThat(responder.handle(request), containsString("200 OK"));
  }

  @Test
  public void containsFileListing() throws IOException {
    documentRoot.newFile("file_1.txt");
    documentRoot.newFile("file_2.txt");
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");

    assertThat(responder.handle(request), containsString("file_1.txt"));
    assertThat(responder.handle(request), containsString("file_2.txt"));
  }
}
