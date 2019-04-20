kubectl delete  configmap -n database database-rest-pack
kubectl delete  configmap -n database database-rest-server
kubectl delete -f  ./manifest/deployment.yaml  -n database
#kubectl delete -f  ./manifest/service.yaml  -n database
