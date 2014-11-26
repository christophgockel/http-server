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

import static org.junit.Assert.*;

public class PartialResponderTest {
  @Rule
  public TemporaryFolder documentRoot = new TemporaryFolder();
  private BaseResponder responder;
  private FileSystem fileSystem;

  @Before
  public void setup() throws IOException {
    fileSystem = new FileSystem(documentRoot.getRoot());
    responder = new PartialResponder(fileSystem);
  }

  @Test
  public void respondsToGetPostPut() {
    assertTrue(responder.respondsTo(RequestMethod.GET, "/partial_content.txt"));
  }

  @Test
  public void readsPartsOfTheFileContent() throws IOException {
    prepareFileWithContent("the_file.txt", "ABC");

    String content = "GET /the_file.txt HTTP/1.1\r\n" +
                     "Range: bytes=0-1\r\n\r\n";
    Request request = RequestHelper.requestFor(content);

    Response response = responder.respond(request);
    assertEquals(StatusCode.PARTIAL_CONTENT, response.getStatus());

    assertEquals("AB", new String(response.getBody()));
  }

  @Test
  public void returnsFullContentIfHeaderIsNotPresent() throws IOException {
    prepareFileWithContent("the_file.txt", "ABC");

    String content = "GET /the_file.txt HTTP/1.1\r\n";
    Request request = RequestHelper.requestFor(content);

    Response response = responder.respond(request);

    assertEquals(StatusCode.OK, response.getStatus());
    assertEquals("ABC", new String(response.getBody()));
  }

  @Test
  public void returnsFullContentIfRangeHeaderIsNotAsExpected() throws IOException {
    prepareFileWithContent("the_file.txt", "ABC");

    String content = "GET /the_file.txt HTTP/1.1\r\n" +
                     "Range: bytes=0\r\n\r\n";
    Request request = RequestHelper.requestFor(content);

    Response response = responder.respond(request);

    assertEquals(StatusCode.OK, response.getStatus());
    assertEquals("ABC", new String(response.getBody()));
  }

  private void prepareFileWithContent(String fileName, String content) throws IOException {
    File file = documentRoot.newFile(fileName);
    FileWriter fw = new FileWriter(file);
    fw.write(content);
    fw.close();
  }
}
