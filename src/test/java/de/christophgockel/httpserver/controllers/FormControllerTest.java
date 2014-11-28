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
import java.nio.file.FileSystems;
import java.nio.file.Files;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FormControllerTest {
  @Rule
  public TemporaryFolder documentRoot = new TemporaryFolder();
  private FormController controller;
  private FileSystem fileSystem;

  @Before
  public void setup() throws IOException {
    Files.deleteIfExists(FileSystems.getDefault().getPath("form_data.txt"));
    fileSystem = new FileSystem(documentRoot.getRoot());
    controller = new FormController(fileSystem);
  }

  @Test
  public void writesPostDataToFile() {
    String content = "POST /form HTTP/1.1\r\n" +
      "Content-Length: 11\r\n" +
      "\r\n" +
      "the content";
    Request request = RequestHelper.requestFor(content);

    Response response = controller.dispatch(request);

    assertEquals(StatusCode.OK, response.getStatus());
    assertTrue(fileSystem.isFile("form_data.txt"));
  }
  @Test
  public void getRequestReturnsContentFromPreviousPost() {
    String content = "POST /form HTTP/1.1\r\n" +
      "Content-Length: 11\r\n" +
      "\r\n" +
      "the content";
    Request request = RequestHelper.requestFor(content);
    controller.dispatch(request);

    request = RequestHelper.requestFor("GET /form HTTP/1.1");
    Response response = controller.dispatch(request);

    assertArrayEquals("the content\n".getBytes(), response.getBody());
  }

  @Test
  public void deletesStoredContent() {
    String content = "POST /form HTTP/1.1\r\n" +
      "Content-Length: 11\r\n" +
      "\r\n" +
      "the content";
    Request request = RequestHelper.requestFor(content);
    controller.dispatch(request);

    content = "DELETE /form HTTP/1.1\r\n";
    request = RequestHelper.requestFor(content);
    controller.dispatch(request);

    request = RequestHelper.requestFor("GET /form HTTP/1.1");
    Response response = controller.dispatch(request);

    assertArrayEquals("".getBytes(), response.getBody());
  }
}
