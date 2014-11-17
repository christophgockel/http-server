package de.christophgockel.httpserver.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArgumentsTest {
  @Test
  public void parsesThePort() {
    String[] args = new String[] {"-p", "1234"};
    Arguments arguments = new Arguments(args);

    assertEquals(1234, arguments.getPort());
  }

  @Test
  public void parsesDocumentRoot() {
    String[] args = new String[] {"-d", "/some/document/path"};
    Arguments arguments = new Arguments(args);

    assertEquals("/some/document/path", arguments.getDocumentRoot());
  }

  @Test
  public void uses5000AsDefaultPort() {
    Arguments arguments = new Arguments(new String[] {});

    assertEquals(5000, arguments.getPort());
  }
}
