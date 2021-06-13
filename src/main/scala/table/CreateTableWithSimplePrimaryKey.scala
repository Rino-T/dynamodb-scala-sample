package table

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model._

import java.net.URI
import scala.util.{Failure, Success, Try}

object CreateTableWithSimplePrimaryKey {
  def main(args: Array[String]): Unit = {
    val client: DynamoDbClient = DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build()
    createTable(client, "roles", "role_id")
    client.close()
  }

  /** シンプルなプライマリーキー `tableName` をもつテーブルを生成する
    *
    * @param ddb       DynamoDbClient
    * @param tableName テーブル名
    * @param key       プライマリーキー
    * @return
    */
  def createTable(ddb: DynamoDbClient, tableName: String, key: String): String = {
    val dbWaiter = ddb.waiter()
    val request = CreateTableRequest
      .builder()
      .attributeDefinitions(
        AttributeDefinition.builder().attributeName(key).attributeType(ScalarAttributeType.S).build()
      )
      .keySchema(KeySchemaElement.builder().attributeName(key).keyType(KeyType.HASH).build())
      .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(10L).writeCapacityUnits(10L).build())
      .tableName(tableName)
      .build()

    Try {
      val response = ddb.createTable(request)
      val tableRequest = DescribeTableRequest
        .builder()
        .tableName(tableName)
        .build()

      // Amazon DynamoDB table が生成されるまで待つ。
      val waiterResponse = dbWaiter.waitUntilTableExists(tableRequest)
      waiterResponse.matched().response().ifPresent(println)

      val newTable = response.tableDescription().tableName()
      newTable
    } match {
      case Failure(exception) =>
        exception match {
          case e: DynamoDbException => throw e
        }
      case Success(value) => value
    }
  }
}
