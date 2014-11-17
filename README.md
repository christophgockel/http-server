# Java HTTP Server

Implementation verified by a [FitNesse suite](https://github.com/8thlight/cob_spec).

Running

```
mvn clean package
```

will create two `.jar` files in the `target` directory. One file called `http-server-1.0-SNAPSHOT.jar` which contains the server implementation only, and one file named `http-server-1.0-SNAPSHOT-jar-with-dependencies.jar` which contains the server implementation _and_ all runtime dependencies. That's the `.jar` file that should be used by FitNesse.
