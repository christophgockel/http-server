package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
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
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultResponderTest {
  @Rule
  public TemporaryFolder documentRoot = new TemporaryFolder();
  private BaseResponder responder;

  @Before
  public void setup() throws IOException {
    responder = new DefaultResponder(new FileSystem(documentRoot.getRoot()));
  }

  @Test
  public void respondsToAllGetRequests() {
    assertTrue(responder.respondsTo(RequestMethod.GET, "/"));
    assertTrue(responder.respondsTo(RequestMethod.GET, "/something"));
    assertTrue(responder.respondsTo(RequestMethod.GET, "/some/other/path"));
  }

  @Test
  public void doesNotRespondToNonGetRequest() {
    assertFalse(responder.respondsTo(RequestMethod.POST, "/"));
    assertFalse(responder.respondsTo(RequestMethod.PUT, "/"));
  }

  @Test
  public void respondsWith200OK() throws IOException {
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");

    assertThat(responder.respond(request), containsString("200 OK"));
  }

  @Test
  public void containsFileListing() throws IOException {
    documentRoot.newFile("file_1.txt");
    documentRoot.newFile("file_2.txt");
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");

    assertThat(responder.respond(request), containsString("file_1.txt"));
    assertThat(responder.respond(request), containsString("file_2.txt"));
  }

  @Test
  public void listsSubDirectoryContents() throws IOException {
    documentRoot.newFile("file_1.txt");
    documentRoot.newFolder("sub");
    documentRoot.newFile("sub/file.txt");
    documentRoot.newFile("sub/another_file.txt");
    Request request = RequestHelper.requestFor("GET /sub HTTP/1.1");

    assertThat(responder.respond(request), not(containsString("file_1.txt")));
    assertThat(responder.respond(request), containsString("file.txt"));
    assertThat(responder.respond(request), containsString("another_file.txt"));
  }
}
