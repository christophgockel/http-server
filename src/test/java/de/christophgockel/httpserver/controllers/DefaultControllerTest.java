package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;

public class DefaultControllerTest {
  private DefaultController controller;
  @Rule
  public TemporaryFolder documentRoot = new TemporaryFolder();

  @Before
  public void setup() {
    controller = new DefaultController(new FileSystem(documentRoot.getRoot()));
  }

  @Test
  public void returnsFileListing() throws IOException {
      documentRoot.newFile("file_1.txt");
      documentRoot.newFile("file_2.txt");
      Request request = RequestHelper.requestFor("GET / HTTP/1.1");

      Response response = controller.get(request);

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

    Response response = controller.get(request);

    assertNotContains(response, "file_1.txt");
    assertContains(response, "file.txt");
    assertContains(response, "another_file.txt");
  }

  @Test
  public void directoryListingContainsLinksToFiles() throws IOException {
    documentRoot.newFile("file_1.txt");
    documentRoot.newFile("file_2.txt");
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");
    Response response = controller.get(request);

    assertContains(response, "<a href=\"file_1.txt\">file_1.txt</a>");
    assertContains(response, "<a href=\"file_2.txt\">file_2.txt</a>");
  }

  @Test
  public void returns404ForUnknownFiles() throws IOException {
    Request request = RequestHelper.requestFor("GET /unknown.jpg HTTP/1.1");

    assertEquals(StatusCode.NOT_FOUND, controller.get(request).getStatus());
  }

  private void assertContains(Response response, String expected) throws IOException {
    assertThat(new String(response.getFullResponse()), containsString(expected));
  }

  private void assertNotContains(Response response, String expected) throws IOException {
    assertThat(new String(response.getFullResponse()), not(containsString(expected)));
  }
}
