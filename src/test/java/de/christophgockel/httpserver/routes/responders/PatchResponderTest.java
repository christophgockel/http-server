package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class PatchResponderTest {
  @Rule
  public TemporaryFolder documentRoot = new TemporaryFolder();
  private BaseResponder responder;
  private FileSystem fileSystem;

  @Before
  public void setup() {
    fileSystem = new FileSystem(documentRoot.getRoot());
    responder = new PatchResponder(fileSystem);
  }

  @Test
  public void respondsToParticularResource() {
    assertTrue(responder.respondsTo(RequestMethod.PATCH, "/patch-content.txt"));
  }

  @Test
  public void doesNotRespondToNonPatchRequests() {
    assertFalse(responder.respondsTo(RequestMethod.GET, "/"));
  }

  @Test
  public void patchingAResourceNeedsValidETag() throws IOException {
    File file = documentRoot.newFile("patch-content.txt");
    FileWriter fw = new FileWriter(file);
    fw.write("some content");
    fw.close();

    String content = "PATCH /patch-content.txt HTTP/1.1\r\n" +
                     "If-Match: 94e66df8cd09d410c62d9e0dc59d3a884e458e05\r\n" +
                     "Content-Length: 11\r\n" +
                     "\r\n" +
                     "new content";
    Request request = RequestHelper.requestFor(content);

    assertEquals(StatusCode.NO_CONTENT, responder.respond(request).getStatus());
    assertArrayEquals("new content\n".getBytes(), fileSystem.getFileContent("patch-content.txt"));
  }

  @Test
  public void doesNotPatchWithoutValidETag() throws IOException {
    documentRoot.newFile("patch-content.txt");
    String content = "PATCH /patch-content.txt HTTP/1.1\r\n" +
      "If-Match: 123\r\n" +
      "Content-Length: 11\r\n" +
      "\r\n" +
      "new content";
    Request request = RequestHelper.requestFor(content);

    assertEquals(StatusCode.PRECONDITION_FAILED, responder.respond(request).getStatus());
  }
}
