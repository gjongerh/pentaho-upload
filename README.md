# Introduction
This is a multi module maven project (**vtg-sparkl-upload-parent**) for developing a Pentaho sparkl plugin. 

The **vtg-sparkl-upload** is a simple project. The plugin is just a one class in a jar file. But testing is a completely different story. Sparkl is running within Pentaho. Pentaho 7.1 at the moment.
Pentaho is in a migration from ivy to a maven project and uses Jersey 1.19.1 as the servlet container. Both have there own environment.

```
TODO three lines of code for loading the sparkl and kettle environment
```

To build a testing environment for Jersey 1.x we need a WAR file running on tomcat (or other). This is done in the **vtg-sparkl-upload-jersey-test** project. I havent studied the solution with Jersey and a glassfish serverwith grizzly.

Because of my own knowledge if have chosen to use a spring project with jersey support. So i have setup the **vtg-sparkl-upload-spring-test** project for testing with Spring boot and Jersey 2.x project. I may asume that Pentaho will migrata to Jersey 2.x in the neer future.

# Testing
The best way was to have an Pentaho BI testing environment. Because of the migration of Pentaho from ivy to maven. Its best to wait after that conversion. Pentaho 8.0 will be completely maven. Then is the moment to build a testing environment.
Documentation for buildign a testing environment is found here (http://wiki.pentaho.com/display/ServerDoc2x/Pentaho+Platform+7.0).
Till that moment build jersey services in a **Jersey** testing environment. Build knowledge a then as late is posible test the nwe component in running Pentaho server.
