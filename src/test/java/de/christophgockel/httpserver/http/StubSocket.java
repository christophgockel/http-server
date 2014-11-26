package de.christophgockel.httpserver.http;

import java.io.*;

public class StubSocket implements ClientSocket {
  private final InputStream inputStream;
  private final OutputStream outputStream;

  public StubSocket(String inputContent) {
    inputStream = new ByteArrayInputStream(inputContent.getBytes());
    outputStream = new ByteArrayOutputStream();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return inputStream;
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    return outputStream;
  }

  @Override
  public void close() throws IOException {
  }
}
