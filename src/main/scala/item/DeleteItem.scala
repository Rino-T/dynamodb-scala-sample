package item

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.{
  AttributeValue,
  DeleteItemRequest,
  DeleteItemResponse,
  DynamoDbException
}

import java.net.URI
import scala.jdk.CollectionConverters.MapHasAsJava
import scala.util.{Failure, Success, Try}

object DeleteItem {
  def main(args: Array[String]): Unit = {
    val client = DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build()
    deleteDynamoDBItem(client, "users", "user_id", "44244091-f580-4dee-a3c7-b3564d9ff20a")
    client.close()
  }

  def deleteDynamoDBItem(ddb: DynamoDbClient, tableName: String, key: String, keyVal: String): DeleteItemResponse = {
    val keyToGet = Map(key -> AttributeValue.builder().s(keyVal).build())

    val request = DeleteItemRequest.builder().tableName(tableName).key(keyToGet.asJava).build()

    Try(ddb.deleteItem(request)) match {
      case Failure(exception) =>
        exception match {
          case e: DynamoDbException => throw e
        }
      case Success(response) => response
    }
  }
}
