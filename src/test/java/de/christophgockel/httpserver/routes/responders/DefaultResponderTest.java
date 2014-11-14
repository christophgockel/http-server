package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.StatusCode;
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
import static org.junit.Assert.*;

public class DefaultResponderTest {
  @Rule
  public TemporaryFolder documentRoot = new TemporaryFolder();
  private BaseResponder responder;

  @Before
  public void setup() throws IOException {
    responder = new DefaultResponder(new FileSystem(documentRoot.getRoot()));
  }

  @Test
  public void respondsToAllRequests() {
    assertTrue(responder.respondsTo(RequestMethod.GET, "/"));
    assertTrue(responder.respondsTo(RequestMethod.GET, "/something"));
    assertTrue(responder.respondsTo(RequestMethod.GET, "/some/other/path"));
    assertTrue(responder.respondsTo(RequestMethod.POST, "/"));
    assertTrue(responder.respondsTo(RequestMethod.PUT, "/"));
  }

  @Test
  public void respondsWith200OK() throws IOException {
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");

    assertEquals(StatusCode.OK, responder.respond(request).getStatus());
  }

  @Test
  public void containsFileListing() throws IOException {
    documentRoot.newFile("file_1.txt");
    documentRoot.newFile("file_2.txt");
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");

    Response response = responder.respond(request);

    assertContains(response, "file_1.txt");
    assertContains(response, "file_2.txt");
  }

  @Test
  public void listsSubDirectoryContents() throws IOException {
    documentRoot.newFile("file_1.txt");
    documentRoot.newFolder("sub");
    documentRoot.newFile("sub/file.txt");
    documentRoot.newFile("sub/another_file.txt");
    Request request = RequestHelper.requestFor("GET /sub HTTP/1.1");
    Response response = responder.respond(request);

    assertNotContains(response, "file_1.txt");
    assertContains(response, "file.txt");
    assertContains(response, "another_file.txt");
  }

  @Test
  public void servesFiles() throws IOException {
    documentRoot.newFile("picture.jpg");
    Request request = RequestHelper.requestFor("GET /picture.jpg HTTP/1.1");

    assertEquals("image/jpeg", responder.respond(request).getHeaders().get("Content-Type"));
  }

  @Test
  public void returns404ForUnknownFiles() throws IOException {
    Request request = RequestHelper.requestFor("GET /unknown.jpg HTTP/1.1");

    assertEquals(StatusCode.NOT_FOUND, responder.respond(request).getStatus());
  }

  @Test
  public void returns405ForNonGetRequests() {
    Request request = RequestHelper.requestFor("POST /unknown HTTP/1.1");

    assertEquals(StatusCode.NOT_ALLOWED, responder.respond(request).getStatus());
    assertEquals(RequestMethod.GET.toString(), responder.respond(request).getHeaders().get("Allow"));
  }

  @Test
  public void directoryListingContainsLinksToFiles() throws IOException {
    documentRoot.newFile("file_1.txt");
    documentRoot.newFile("file_2.txt");
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");
    Response response = responder.respond(request);

    assertContains(response, "<a href=\"file_1.txt\">file_1.txt</a>");
    assertContains(response, "<a href=\"file_2.txt\">file_2.txt</a>");
  }

  private void assertContains(Response response, String expected) throws IOException {
    assertThat(new String(response.getFullResponse()), containsString(expected));
  }

  private void assertNotContains(Response response, String expected) throws IOException {
    assertThat(new String(response.getFullResponse()), not(containsString(expected)));
  }
}
