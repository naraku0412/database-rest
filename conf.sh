kubectl create  configmap -n database database-rest-pack  --from-file=./scripts/pack.sh
kubectl create  configmap -n database database-rest-server  --from-file=./scripts/server.js
docker build -t gmt.reg.me/frame/maven-rest:v1  -f ./Dockerfiles/dockerfile.maven .
docker push gmt.reg.me/frame/maven-rest:v1
docker build -t gmt.reg.me/frame/jn-rest:v1 -f ./Dockerfiles/dockerfile.jdk-node .
docker push gmt.reg.me/frame/jn-rest:v1
#镜像更新
ansible node -m shell -a "docker pull gmt.reg.me/frame/maven-rest:v1"
ansible node -m shell -a "docker pull gmt.reg.me/frame/jn-rest:v1"
kubectl create -f  ./manifest/deployment.yaml  -n database
#kubectl create -f  ./manifest/service.yaml  -n database

