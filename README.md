# AWS DynamoDB using from Scala sample

## ローカル開発用 Docker 起動

```shell
docker-compose up
```

次の2つが立ち上がります。

* [dynamodb local](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html)
  : AWSが提供するローカル実行可能なDynamoDB 
* [dynamodb admin](https://github.com/aaronshaf/dynamodb-admin)
  : サードパーティ製のDynamoDB用GUIクライアント

localhost:8001 にアクセスすると、dynamodb-admin を見れます。

参考：[【DynamoDB Local】快適なDynamoDBのローカル開発環境を構築する](https://hackers-high.com/aws/dynamodb-local-development/)

## AWSへのアクセス

* [AWS認証情報の利用 (v1) ](https://docs.aws.amazon.com/ja_jp/sdk-for-java/v1/developer-guide/credentials.html)
* [Using Credentials (v2) ](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials.html)

ローカルで実行できるようにサンプルでは下記のようにendpointをローカルに向けているが、

```scala
val client: DynamoDbClient = DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build()
```

実際にAWS上のリソースに影響を与えたい場合は

```scala
val client = DynamoDbClient.create()
```

のようにしてあげれば、実行環境から必要な認証情報を取得してDynamoDBClientを生成してくれる。

## 参考コードサンプル

AWS公式のサンプルコードを真似ている。

* [Working with DynamoDB](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-dynamodb.html)