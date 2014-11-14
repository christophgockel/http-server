package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

public class FormResponder extends BaseResponder {
  private final String DATA_FILE = "form_data.txt";
  private final FileSystem fileSystem;

  public FormResponder(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
    initialize();
  }

  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return  path.equals("/form");
  }

  @Override
  protected Response respond(Request request) {
    Response response = new Response(StatusCode.OK);

    if (isPostOrPut(request)) {
      byte[] content = request.getBody().getBytes();
      fileSystem.setFileContent(DATA_FILE, content);
    } else if (isDelete(request)) {
      fileSystem.deleteFile(DATA_FILE);
    } else {
      response.setBody(fileSystem.getFileContent(DATA_FILE));
    }
    return response;
  }

  private void initialize() {
    fileSystem.deleteFile(DATA_FILE);
  }

  private boolean isPostOrPut(Request request) {
    return request.getMethod() == RequestMethod.POST || request.getMethod() == RequestMethod.PUT;
  }

  private boolean isDelete(Request request) {
    return request.getMethod() == RequestMethod.DELETE;
  }
}
