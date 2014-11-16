package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.filesystem.FileSystem;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

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
}
