#!/bin/bash

host="127.0.0.1"
port="6379"
redis-cli -h $host -p $port
#clusterNodes=`echo `CLUSTER NODES` | grep master |cut -d' ' -f2 | cut -d'@' -f1 | cut -d':' -f1` 
#clusterPorts=`echo `CLUSTER NODES` | grep master |cut -d' ' -f2 | cut -d'@' -f1 | cut -d':' -f2`

clusterNodes=`cat ./text | grep master |cut -d' ' -f2 | cut -d'@' -f1 | cut -d':' -f1`
clusterPorts=`cat ./text | grep master |cut -d' ' -f2 | cut -d'@' -f1 | cut -d':' -f2`

lengthCluster=echo $clusterPort |grep  wc -l
export CLUSTER_NODES=$clusterNodes
export CLUSTER_PORTS=$clusterPorts
echo $clusterNodes
echo $clusterPorts
echo $CLUSTER_NODES
echo $CLUSTER_PORTS

