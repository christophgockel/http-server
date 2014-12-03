package de.christophgockel.httpserver.http;

import java.io.IOException;

public class StubServerSocket extends ServerSocket {
  private boolean closed;
  private boolean alreadyAccepted;
  private final StubClientSocket clientSocket;

  public StubServerSocket(String clientContent) {
    super(null);
    clientSocket = new StubClientSocket(clientContent);
    closed = false;
    alreadyAccepted = false;
  }

  @Override
  public ClientSocket accept() throws IOException {
    if (alreadyAccepted) {
      return null;
    }

    alreadyAccepted = true;

    return clientSocket;
  }

  @Override
  public void close() throws IOException {
    closed = true;
  }

  public boolean isClosed() {
    return closed;
  }
}
