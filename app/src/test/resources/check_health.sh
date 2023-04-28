#!/usr/bin/env bash
res=$(curl -k --request POST --header "Content-Type: application/json"  --data '{"method":"getServerPublicInformation","id":"1", "jsonrpc":"2.0"}' "$1/openbis/openbis/rmi-application-server-v3.json")
code=$?
if [[ $res =~ jsonrpc ]];
then
  exit 0
else
  exit $code
fi