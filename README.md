# AWS DynamoDB using from Scala sample

## ローカル開発用 Docker 起動

```shell
docker-compose up
```

次の2つが立ち上がります。

* dynamodb local ・・・AWSが提供するローカル開発用のDynamoDB
* dynamodb admin ・・・サードパーティ製のDynamoDB用GUI

localhost:8001 にアクセスすると、dynamodb-admin を見れます。