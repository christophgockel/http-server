package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.filesystem.FileSystem;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.util.HtmlPage;

import java.io.File;

import static de.christophgockel.httpserver.StatusCode.NOT_FOUND;
import static de.christophgockel.httpserver.StatusCode.OK;

public class DefaultController extends Controller {
  private final FileSystem fileSystem;

  public DefaultController(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  protected Response get(Request request) {
    String requestedResource = request.getURI();

    if (fileSystem.isFile(requestedResource)) {
      return serveFile(requestedResource);
    } else if (fileSystem.isDirectory(requestedResource)) {
      return serveDirectoryListing(requestedResource);
    } else {
      return new Response(NOT_FOUND);
    }
  }

  private Response serveFile(String requestedResource) {
    String mimeType = fileSystem.getMimeType(requestedResource);
    byte[] content = fileSystem.getFileContent(requestedResource);

    Response response = new Response(OK);
    response.addHeader("Content-Type", mimeType);
    response.setBody(content);

    return response;
  }

  private Response serveDirectoryListing(String requestedResource) {
    HtmlPage page = new HtmlPage(requestedResource);

    for (File file : fileSystem.getFiles(requestedResource)) {
      page.addParagraph(page.createLink(file.getName(), file.getName()));
    }

    Response response = new Response(OK);
    response.addHeader("Content-Type", "text/html");
    response.setBody(page.getContent());

    return response;
  }
}
