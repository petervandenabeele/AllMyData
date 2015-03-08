#!/bin/bash

echo "KAFKA_HOME is" $KAFKA_HOME
cd $KAFKA_HOME

if [ -z "`ls -l  ~/data/private/data/mnt | grep kafka`" ]; then
  echo "ERROR: mnt dir is not mounted !!"
  exit 1
fi

echo "Starting consumer on test_001"
bin/kafka-console-consumer.sh --zookeeper trusty64:2181 --topic test_001 --from-beginning
