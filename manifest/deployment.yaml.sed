kind: Deployment
apiVersion: extensions/v1beta1
metadata:
  name: database-server
  namespace: database
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: database-server
    spec:
      containers:
        - name: server
          image: gmt.reg.me/frame/jdk-node
          command:
            - node
          args:
            - /workspace/server.js
          volumeMounts:
            - name: database-app
              mountPath: /workspace/project.jar
              subPath: project.jar
              readOnly: true

      volumes:
        - name: database-app
          configMap:
            name: database-app
            defaultMode: 0755

