package item

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.{
  AttributeValue,
  DynamoDbException,
  PutItemRequest,
  ResourceNotFoundException
}

import java.net.URI
import java.util.UUID
import scala.jdk.CollectionConverters.MapHasAsJava
import scala.util.{Failure, Success, Try}

object AddNewItem {
  def main(args: Array[String]): Unit = {
    val client = DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build()
    putItemInTable(
      client,
      "albums",
      "album_id",
      UUID.randomUUID().toString,
      "album_title",
      "THE BOOK",
      "awards",
      "レコード大賞",
      "songTitle",
      "アンコール"
    )

    client.close()
  }

  def putItemInTable(
      ddb: DynamoDbClient,
      tableName: String,
      key: String,
      keyVal: String,
      albumTitle: String,
      albumTitleValue: String,
      awards: String,
      awardVal: String,
      songTitle: String,
      songTitleVal: String
  ): Unit = {
    val itemValues = Map(
      key        -> AttributeValue.builder().s(keyVal).build(),
      songTitle  -> AttributeValue.builder().s(songTitleVal).build(),
      albumTitle -> AttributeValue.builder().s(albumTitleValue).build(),
      awards     -> AttributeValue.builder().s(awardVal).build()
    )

    val request = PutItemRequest
      .builder()
      .tableName(tableName)
      .item(itemValues.asJava)
      .build()

    Try {
      ddb.putItem(request)
      println(s"$tableName was successfully updated")
    } match {
      case Failure(exception) =>
        exception match {
          case e: ResourceNotFoundException => println(s"Error: Amazon DynamoDB table $tableName can't be found.")
          case e: DynamoDbException         => throw e
        }
      case Success(_) => ()
    }
  }
}
