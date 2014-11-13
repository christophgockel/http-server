package de.christophgockel.httpserver.filesystem;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;
import java.util.HashMap;
import java.util.Map;

public class MimeTypes extends FileTypeDetector {
  private Map<String, String> mimeTypes;

  public MimeTypes() {
    mimeTypes = new HashMap<>();

    mimeTypes.put("txt", "text/plain");
    mimeTypes.put("gif", "image/gif");
    mimeTypes.put("jpg", "image/jpeg");
    mimeTypes.put("jpeg", "image/jpeg");
  }

  @Override
  public String probeContentType(Path path) throws IOException {
    String filePath = path.toString();
    String extension = filePath.substring(filePath.lastIndexOf(".") + 1);

    return mimeTypes.get(extension);
  }
}
