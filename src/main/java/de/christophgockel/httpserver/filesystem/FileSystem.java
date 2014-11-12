package de.christophgockel.httpserver.filesystem;

import java.io.File;
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

  private String relativePathFor(File file) {
    return root.toURI().relativize(file.toURI()).getPath();
  }
}
