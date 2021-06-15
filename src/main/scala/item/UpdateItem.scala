package item

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.{
  AttributeAction,
  AttributeValue,
  AttributeValueUpdate,
  DynamoDbException,
  ResourceNotFoundException,
  UpdateItemRequest,
  UpdateItemResponse
}

import java.net.URI
import scala.jdk.CollectionConverters.MapHasAsJava
import scala.util.{Failure, Success, Try}

object UpdateItem {
  def main(args: Array[String]): Unit = {
    val client = DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build()
    updateTableItem(client, "users", "user_id", "44244091-f580-4dee-a3c7-b3564d9ff20a", "user_name", "山田二郎")
    client.close()
  }

  def updateTableItem(
      ddb: DynamoDbClient,
      tableName: String,
      key: String,
      keyVal: String,
      name: String,
      updateVal: String
  ): Option[UpdateItemResponse] = {
    val itemKey = Map(key -> AttributeValue.builder().s(keyVal).build())
    val updatedValues = Map(
      name -> AttributeValueUpdate
        .builder()
        .value(AttributeValue.builder().s(updateVal).build())
        .action(AttributeAction.PUT)
        .build()
    )

    val request = UpdateItemRequest
      .builder()
      .tableName(tableName)
      .key(itemKey.asJava)
      .attributeUpdates(updatedValues.asJava)
      .build()

    Try(ddb.updateItem(request)) match {
      case Failure(exception) =>
        exception match {
          case e: ResourceNotFoundException => println(e.getMessage); None
          case e: DynamoDbException         => throw e
        }
      case Success(response) => Some(response)
    }
  }
}
