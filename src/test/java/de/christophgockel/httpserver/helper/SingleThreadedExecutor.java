package de.christophgockel.httpserver.helper;

import java.util.concurrent.Executor;

public class SingleThreadedExecutor implements Executor {
  private boolean hasExecuted;

  public SingleThreadedExecutor() {
    hasExecuted = false;
  }

  @Override
  public void execute(Runnable command) {
    hasExecuted = true;

    command.run();
  }

  public boolean hasExecuted() {
    return hasExecuted;
  }
}
