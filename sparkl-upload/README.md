# Introduction
Uploading files in a Pentaho Server is not supported in the community edition. Heavily inspirend by Marcello Pontes http://blog.oncase.com.br/easy-uploader-for-pentaho-bi-server/ thismodule makes it posible to upload and proces files with a transformation.

The **vtg-sparkl-upload** is a simple project. The plugin is just a one class in a jar file. But testing is a completely different story. Sparkl is running within Pentaho. Pentaho 7.1 at the moment.
Pentaho is in a migration from ivy to a maven project and uses Jersey 1.19.1 as the servlet container. Both have there own environment.

```
pluginUtils = new PluginUtilsForTesting();
ICpkEnvironment environment = new CpkEnvironmentForTesting( pluginUtils, repAccess );
KettleEnvironment.init();
```

This module (sub-project) has only unit testing and no testing environment available. See the other modules for testing the environment. For JUnit testing see more info https://jersey.github.io/documentation/1.19.1/test-framework.html for the current Jersey 1.x version. 

# Development
In ***src.main.java*** there is a class **com.virtorg.bi.sparkl.ws.UploaderREST** which is tested with the test class **com.virtorg.test.jersey.UploaderRestTest**.
In the ***src.test.java*** there is a test class fot testing the functionality with JUnit testing. This is testing without the Pentaho/sparkl environment, but just for jersey and plain jaava tests.

# Installation


# Usage


