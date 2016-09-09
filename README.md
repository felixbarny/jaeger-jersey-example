# Basic example showing distributed tracing across Jersey apps
This is an example app where two Jersey (Java) services collaborate on an http request. Notably, timing of these requests are recorded into [Zipkin](http://zipkin.io/), a distributed tracing system. This allows you to see the how long the whole operation took, as well how much time was spent in each service.

Here's an example of what it looks like
<img width="972" alt="zipkin screen shot" src="https://cloud.githubusercontent.com/assets/64215/18389420/76adb814-76d8-11e6-83f1-3d5bc0c85206.png">

# Implementation Overview

Web requests are served by JAX-RS controllers, and tracing is automatically performed for you by [Jaeger](https://github.com/uber/jaeger-client-java/).

# Running the example
This example has two services: frontend and backend. They both report trace data to zipkin. To setup the demo, you need to start Frontend, Backend and Zipkin.

Once the services are started, open http://localhost:8081/
* This will call the backend (http://localhost:9000/api) and show the result, which defaults to a formatted date.

Next, you can view traces that went through the backend via http://localhost:9411/?serviceName=backend
* This is a locally run zipkin service which keeps traces in memory

## Starting the Services
Open this project in IntelliJ and run [jaeger.jersey.Frontend](/src/main/java/jaeger/jersey/Frontend.java) and [jaeger.jersey.Backend](/src/main/java/jaeger/jersey/Backend.java)

Next, run [Zipkin](http://zipkin.io/), which stores and queries traces reported by the above services.

```bash
wget -O zipkin.jar 'https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec'
java -jar zipkin.jar
```
