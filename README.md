# Scalable Capital Coding Challenge

## create an executable jar and run it
```
git clone
cd scalable-capital-coding-challenge
mvn clean install
java -jar target/coding-challenge-0.0.1-SNAPSHOT-jar-with-dependencies.jar "your search string"
```

```
# behind a proxy
git clone
cd coding-challenge-scalable-capital
mvn -Dhttp.proxyHost=PROXY_HOST -Dhttp.proxyPort=PROXY_PORT clean install
java -Dhttp.proxyHost=PROXY_HOST -Dhttp.proxyPort=PROXY_PORT -jar target/coding-challenge-0.0.1-SNAPSHOT-jar-with-dependencies.jar "your search string"
```

## which libraries I choose and why

* [slf4j](http://www.slf4j.org/)

  this is an logging facade for other logging providers for example logback or log4j

* [Apache HTTPClient](https://hc.apache.org/httpcomponents-client-4.5.x/index.html)

  Used for execute GET Requests, simplest HTTPClient

* [jsoup](https://jsoup.org/)

  used for finding elements in html response

* [JUnit4](https://junit.org/junit4/)

  for testing

* [hamcrest](http://hamcrest.org/JavaHamcrest/)

  for comparing results

* [mockito](https://site.mockito.org/)

  for mocking

* [powermock](https://github.com/powermock/powermock)

  for mocking static methods

* [logback](http://logback.qos.ch/)

  for logging during tests
