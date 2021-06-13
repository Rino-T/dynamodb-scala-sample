package table

import software.amazon.awssdk.services.dynamodb.DynamoDbClient

object DefaultCredential {
  def main(args: Array[String]): Unit = {
    // 実行環境に存在する認証情報を利用してAWSリソースへアクセスする
    // 参考：https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials.html
    val client = DynamoDbClient.create()
    CreateTableWithSimplePrimaryKey.createTable(client, "users", "user_id")
    client.close()
  }
}
