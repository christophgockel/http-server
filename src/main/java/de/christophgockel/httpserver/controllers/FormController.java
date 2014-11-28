package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

public class FormController extends Controller {
  private final String DATA_FILE = "form_data.txt";
  private final FileSystem fileSystem;

  public FormController(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  protected Response get(Request request) {
    Response response = new Response(StatusCode.OK);
    response.setBody(fileSystem.getFileContent(DATA_FILE));
    return response;
  }

  @Override
  protected Response post(Request request) {
    Response response = new Response(StatusCode.OK);
    byte[] content = request.getBody().getBytes();

    fileSystem.setFileContent(DATA_FILE, content);

    return response;
  }

  @Override
  protected Response delete(Request request) {
    Response response = new Response(StatusCode.OK);
    fileSystem.deleteFile(DATA_FILE);
    return response;
  }
}
