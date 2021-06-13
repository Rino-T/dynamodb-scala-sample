import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.{
  AttributeDefinition,
  CreateTableRequest,
  DynamoDbException,
  KeySchemaElement,
  KeyType,
  ProvisionedThroughput,
  ScalarAttributeType
}

import java.net.URI
import scala.util.chaining.scalaUtilChainingOps
import scala.util.{Failure, Success, Try}

object CreateTableWithCompositePrimaryKey {
  def main(args: Array[String]): Unit = {
    val client: DynamoDbClient = DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build()
    createTableComKey(client, "Greetings").pipe(println)
    client.close()
  }

  /** Composite Key（キー結合）を含むテーブルを作成する
    * @param ddb DynamoDbClient
    * @param tableName テーブル名
    * @return テーブルID
    */
  def createTableComKey(ddb: DynamoDbClient, tableName: String): String = {
    val request = CreateTableRequest
      .builder()
      .attributeDefinitions(
        AttributeDefinition.builder().attributeName("Language").attributeType(ScalarAttributeType.S).build(),
        AttributeDefinition.builder().attributeName("Greeting").attributeType(ScalarAttributeType.S).build()
      )
      .keySchema(
        KeySchemaElement
          .builder()
          .attributeName("Language")
          .keyType(KeyType.HASH)
          .build(),
        KeySchemaElement
          .builder()
          .attributeName("Greeting")
          .keyType(KeyType.RANGE)
          .build()
      )
      .provisionedThroughput(
        ProvisionedThroughput
          .builder()
          .readCapacityUnits(10L)
          .writeCapacityUnits(10L)
          .build()
      )
      .tableName(tableName)
      .build()

    Try {
      val result = ddb.createTable(request)
      result.tableDescription().tableId()
    } match {
      case Failure(exception) =>
        exception match {
          case e: DynamoDbException => throw e
        }
      case Success(tableId) => tableId
    }
  }
}
