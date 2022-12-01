# Basic Jakarta EE 10 and Eclipse MicroProfile Template

This Maven archetype is generating a Jakarta EE 10 project with Eclipse MicroProfile and Jersey 3.1 to be deployed on Payara Server 6.

In order to use this archetype, the first step is to install it in your Maven local repository:

    $ git clone ...
    $ cd ....
    $ mvn clean install

Once that the archetype is installed, you can use it in order to generate a project:

    $ mkdir basic
    $ cd basic
    $ ../.../generate.sh

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