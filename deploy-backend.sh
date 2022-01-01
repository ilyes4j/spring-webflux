#!/bin/bash

export GOOGLE_APPLICATION_CREDENTIALS=../gcp/virtual-api-com-backend-316a346bc3ba.json
gcloud app deploy target/appengine-staging/app.yaml --quiet --project=virtual-api-com-backend