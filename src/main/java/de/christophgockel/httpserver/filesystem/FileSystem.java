package de.christophgockel.httpserver.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class FileSystem {
  private final File root;

  public FileSystem(String directory) {
    this(new File(directory));
  }

  public FileSystem(File documentRoot) {
    root = documentRoot;
  }

  public List<File> getFiles() {
    return new ArrayList<>(asList(root.listFiles()));
  }

  public List<File> getFiles(String directory) {
    return getFiles(new File(root.getPath() + File.separator + directory));
  }

  public List<String> getAvailableFiles() {
    return getAvailableFiles(root);
  }

  public List<String> getAvailableFiles(File folder) {
    List<String> files = new ArrayList<>();

    for (File file : folder.listFiles()) {
      if (file.isDirectory()) {
        files.addAll(getAvailableFiles(file));
      } else {
        files.add(relativePathFor(file));
      }
    }

    return files;
  }

  public boolean isFile(String pathToFile) {
    String fileName = pathToFile.startsWith("/") ? pathToFile.substring(1) : pathToFile;
    String fullPath = root.getPath() + File.separator + fileName;
    File file = new File(fullPath);

    return file.exists() && file.isFile();
  }

  public boolean isDirectory(String pathToDirectory) {
    String fullPath = root.getPath() + File.separator + pathToDirectory;
    File file = new File(fullPath);

    return file.exists() && file.isDirectory();
  }

  public String getMimeType(String pathToFile) {
    try {
      Path path = FileSystems.getDefault().getPath(root.getPath() + File.separator + pathToFile);

      return Files.probeContentType(path);
    } catch (IOException e) {
      return "";
    }
  }

  public byte[] getFileContent(String pathToFile) {
    try {
      return Files.readAllBytes(Paths.get(root.getPath() + File.separator + pathToFile));
    } catch (IOException e) {
      return "ERR".getBytes();
    }
  }

  private String relativePathFor(File file) {
    return root.toURI().relativize(file.toURI()).getPath();
  }

  private List<File> getFiles(File directory) {
    return getFilesInDirectory(directory);
  }

  private List<File> getFilesInDirectory(File directory) {
    return new ArrayList<>(asList(directory.listFiles()));
  }
}
