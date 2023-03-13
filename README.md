# Basic Jakarta EE 10 and Eclipse MicroProfile Template

This Maven archetype is generating a Jakarta EE 10 project with Eclipse MicroProfile and Jersey 3.1 to be deployed on Payara Server 6.
It can be used in one of the following two modes:
- from the local catalog
- from Maven central

## Using the archetype from the local catalog

In order to use this archetype from the local catalog, the first step is to install it in your Maven local repository:

    $ git clone https://github.com/nicolasduminil/jakartaee10-basic-archetype.git
    $ cd jakartaee10-basic-archetype
    $ mvn clean install
Now the archetype is installed in your catalog in your local Maven repository and you can use it in order to generate a 
project.

## Using the archetype from Maven central
The archetype is already deployed in the Maven central snapshot catalog. In order to use it from there, you need to add
the following to your `settings.xml` file:

    ...
    <server>
      <id>ossrh-snapshots</id>
    </server>
    ...
    <profiles>
    ...
      <profile>
        <id>...</id>
        <repositories>
          ...
          <repository>
            <id>ossrh-snapshots</id>
            <name>Sonatype snaphost repo</name>
            <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
            <layout>default</layout>
            <snapshots>
              <enabled>true</enabled>
            </snapshots>
            <releases>
              <enabled>false</enabled>
            </releases>
          </repository>
          ...
        </repositories>
        ...
      </profile>
    ...
    </profiles>
    ...
A `settings.xml` file has been provided in the repository, for your convenience.

## Generating the Jakarta EE 10 project
Whatever the way you're using the archetype might be, as a locally installed one or from Maven central, you can now
generate your Jakarta EE 10 project as follows:

    $ mkdir basic
    $ cd basic
    $ ../generate.sh
This will execute the following maven command:

    #!/bin/sh
    mvn -B archetype:generate \
      -DarchetypeGroupId=fr.simplex-software.archetypes \
      -DarchetypeArtifactId=jakartaee10-basic-archetype \
      -DarchetypeVersion=1.0-SNAPSHOT \
      -DgroupId=com.exemple \
      -DartifactId=test
Now, you have to build the new generated project:

    $ cd test
    $ ./build.sh

The `build.sh` script executes the following:

    #!/bin/sh
    mvn clean package && docker build -t ${groupId}/${artifactId} .
    docker rm -f ${artifactId} || true && docker run -d -p 8080:8080 -p 4848:4848 --name ${artifactId} ${groupId}/${artifactId}
Here we're packaging the project as a WAR and we build a new Docker image, tagged with the name of the current maven artifact and based on Payara Server 6, in which we deploy our WAR. 

If everythging went well, you can now start the Payara administration console, at http://localhost:4848, and check whether our application is deployed. Then you can test it by running:

    $ curl http://localhost:8080/test/api/myresource

You can use also the script `myresource.sh` which does the same thing.

An integration test is provided as well. You can run it as follows:

    mvn verify

Notice that the integration test, named `MyResourceIT`, is using testcontainers to run a Docker image of `payara/server-full:6.2022.1` in which we deploy our WAR. Accrodingly, the WAR has to exist at that moment, meaning that you should already have run the package Maven goal. This is done by the script build.sh, as explained above, but if you need to execute manually this goal, what ever the reason might be, than you might need to skip the tests execution.

Last but not least, by browsing to http://localhost:8080/test, or by running the `curl get` command to this same URL, you will see the `index.jsp` file, deployed with the web application.

Enjoy !