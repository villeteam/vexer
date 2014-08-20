# Ville Exercise Plugin (vexer) project

Interfaces defining how a ViLLE-plugin project should work (vexer-model), 
some helpers that can be used to implement new exercise-types 
(vexer-helpers), stub-implementation of ville-exercise-system for 
testing exercise types (vexer-stub). Also a simple template exercise 
(vexer-template) and a simple ui for testing the stub (vexer-stub-demo).

For more information on ViLLE see ville.cs.utu.fi

Project is licensed under MIT-license (see LICENSE.txt).

## To give it a test run
1. Install maven to your system ( http://maven.apache.org/users/index.html )
2. On command line, navigate to the directory you want the ville-exercise-type project to be created in
3. Run:

    mvn archetype:generate -DarchetypeGroupId=fi.utu.ville.exercises -DarchetypeArtifactId=vexer-archetype 
    -DarchetypeVersion=0.1.6-SNAPSHOT -DarchetypeRepository=https://raw.github.com/villeteam/vexer/mvn-repo/

4. After a while you will be asked to provide some information, namely:
    - groupId: you can use "fi.utu.ville.exercises"
    - artifactId: you can use anything here when testing (eg. "testexer");
      if you were to seriously start developing a new Ville-exercise, please
      contact VilleTeam to ensure that your artifactId is unique
    - version: some version number ( the suggested 1.0-SNAPSHOT is fine)
    - package: you can use "fi.utu.ville.exercises.'artifactId you used'"
    - VilleJavaClassPrefix: if you for example used testexer for artifactId you can use TestExer here

5. Change working directory to the newly generated directory (named after 'artifactId you used')
6. Run:

    mvn install

7. Change to "artifactId"-stub sub-directory
8. Run:

    mvn jetty:run

9. After you see "[INFO] Started Jetty Server" Open a browser to address http://localhost:8080/
10. You should see Ville-Exercise Stub loaded with the newly generated exercise-type (if nothing
    shows, try refreshing the browser window)

## To start developing
0. Complete the steps in "give it a test run"
1. You'll find the Java source files for the generated Ville-exercise type 
    in folder "installation dir"/"artifactId/"artifactId"/src/main/java
2. Compile the classes after you have made changes. Compiled .class files must 
    go to "installation dir"/"artifactId"/"artifactId"/target/classes . You can 
    do this (for example) by running:
    
    mvn compile ( in dir "installation dir"/"artifactId"/"artifactId" )

3. The Jetty-server should be configured in such a way, that you can leave it running 
    and it should pick up any changes after compilation of source files to .class files
    is done

4. Start using some IDE as this will definetely speed-up Java development
    - To import the generated project to Eclipse you can use "File>Import>Existing Maven Projects", 
      then select as root directory "installation dir"/"artifactId" and press "Finish" 
    - There are lots of resources on the internet on integrating Maven to different IDEs
    - You can still leave Jetty running in the background, and it should pick up changes you
      make to Java-sources through an IDE once the IDE has compiled the sources

## NOTES
You can of course use any of your Maven and IDE -skills to set up the project in a different manner.
A few points you should notice:
- Exercise types are at the moment looked automatically only from packages starting with fi.utu.ville, 
    if you use some other package for your exercise-type, you must override VilleExerStubUI's method
    getTypesToLoad() in generated {package}.stub.StubUI in {artifactId}-stub module
- As we hit a more stable state with the project we will hopefully be migrating the artifacts
    to some other place than github...
