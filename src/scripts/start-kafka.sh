#!/bin/bash

echo "KAFKA_HOME is" $KAFKA_HOME
cd $KAFKA_HOME

if [ -z "`ls -l  ~/data/private/data/mnt | grep kafka`" ]; then
  echo "ERROR: mnt dir is not mounted !!"
  exit 1
fi

echo "Starting Zookeeper"
cat config/zookeeper.properties
bin/zookeeper-server-start.sh -daemon config/zookeeper.properties

sleep 4

echo "Starting Kafka"
cat config/server.properties
bin/kafka-server-start.sh -daemon config/server.properties

sleep 4

echo "Starting consumer on ATD_test"
bin/kafka-console-consumer.sh --zookeeper trusty64:2181 --topic ATD_test --from-beginning
