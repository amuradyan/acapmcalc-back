# Backend for acapm

This is a simple backend to acapm that has a single endpoint accepting an expression and 
returning its value as described below

    curl -X GET \
      'http://<host>:<port>/values?expression=<expression>' \
      -H 'Accept: */*' \
      -H 'Accept-Encoding: gzip, deflate' \
      -H 'Connection: keep-alive' \
      -H 'Host: localhost:8080' \

It also stores the result to mongoDB