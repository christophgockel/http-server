package de.christophgockel.httpserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.*;

public class HttpServerTest {
  private HttpServer server;
  private int port;
  private String documentRoot;

  @Before
  public void setup() throws IOException {
    port = 8084;
    documentRoot = "some/directory";
    server = new HttpServer(port, documentRoot);
  }

  @After
  public void teardown() throws IOException {
    if (server.isRunning()) {
      server.stop();
    }
  }

  @Test
  public void hasPortAndRootDirectory() {
    assertEquals(port, server.getPort());
    assertEquals(documentRoot, server.getDocumentRoot());
  }

  @Test
  public void canBeStarted() throws IOException {
    connectClient();
    server.start();
    assertTrue(server.isRunning());
  }

  @Test
  public void canBeStopped() throws IOException {
    connectClient();
    server.start();
    server.stop();
    assertFalse(server.isRunning());
  }

  private void connectClient() throws IOException {
    Socket client = new Socket("localhost", port);
  }
}
