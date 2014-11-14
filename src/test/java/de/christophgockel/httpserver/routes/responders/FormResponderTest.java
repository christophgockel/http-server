package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class FormResponderTest {
  @Rule
  public TemporaryFolder documentRoot = new TemporaryFolder();
  private BaseResponder responder;
  private FileSystem fileSystem;

  @Before
  public void setup() throws IOException {
    Files.deleteIfExists(FileSystems.getDefault().getPath("form_data.txt"));
    fileSystem = new FileSystem(documentRoot.getRoot());
    responder = new FormResponder(fileSystem);
  }

  @Test
  public void respondsToGetPostPut() {
    assertTrue(responder.respondsTo(RequestMethod.GET, "/form"));
    assertTrue(responder.respondsTo(RequestMethod.POST, "/form"));
    assertTrue(responder.respondsTo(RequestMethod.PUT, "/form"));
  }

  @Test
  public void clearsExistingDataFileOnInstantiation() throws IOException {
    File file = documentRoot.newFile("form_data.txt");
    FileWriter writer = new FileWriter(file);
    writer.write("old content");
    writer.close();

    String content = "GET /form HTTP/1.1";
    Request request = RequestHelper.requestFor(content);

    responder = new FormResponder(fileSystem);
    Response response = responder.respond(request);

    assertEquals(StatusCode.OK, response.getStatus());
    assertArrayEquals("".getBytes(), response.getBody());
  }

  @Test
  public void writesPostDataToFile() {
    String content = "POST /form HTTP/1.1\r\n" +
      "Content-Length: 11\r\n" +
      "\r\n" +
      "the content";
    Request request = RequestHelper.requestFor(content);

    Response response = responder.respond(request);

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
    responder.respond(request);

    request = RequestHelper.requestFor("GET /form HTTP/1.1");
    Response response = responder.respond(request);

    assertArrayEquals("the content\n".getBytes(), response.getBody());
  }

  @Test
  public void deletesStoredContent() {
    String content = "POST /form HTTP/1.1\r\n" +
      "Content-Length: 11\r\n" +
      "\r\n" +
      "the content";
    Request request = RequestHelper.requestFor(content);
    responder.respond(request);

    content = "DELETE /form HTTP/1.1\r\n";
    request = RequestHelper.requestFor(content);
    responder.respond(request);

    request = RequestHelper.requestFor("GET /form HTTP/1.1");
    Response response = responder.respond(request);

    assertArrayEquals("".getBytes(), response.getBody());
  }
}
