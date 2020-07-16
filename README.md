## A simple Java REST client for the Allocine API

It's based on the work done by Gromez and his [PHP client](https://github.com/gromez/allocine-api)

Just run the tests in order to try the client
```
./gradlew test
```

For now just a movie search and a theater search are available but it's quite easy to add more methods using [the documentation](https://wiki.gromez.fr/dev/api/allocine_v3)

The client's methods return a browsable `JsonObject` but it could be improved, generating a model based on the API, and returning matching POJOs.
