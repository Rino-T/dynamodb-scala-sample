package item

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.{AttributeValue, DynamoDbException, GetItemRequest}

import java.net.URI
import scala.jdk.CollectionConverters.{MapHasAsJava, MapHasAsScala}
import scala.util.{Failure, Success, Try}

object GetItem {
  def main(args: Array[String]): Unit = {
    val client: DynamoDbClient = DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build()
    getDynamoDbItem(client, "users", "user_id", "44244091-f580-4dee-a3c7-b3564d9ff20a").foreach(println)
    client.close()
  }

  def getDynamoDbItem(
      ddb: DynamoDbClient,
      tableName: String,
      key: String,
      keyVal: String
  ): Option[Map[String, AttributeValue]] = {
    val keyToGet: Map[String, AttributeValue] = Map(key -> AttributeValue.builder().s(keyVal).build())

    val request = GetItemRequest
      .builder()
      .key(keyToGet.asJava)
      .tableName(tableName)
      .build()

    val response = Try(ddb.getItem(request)) match {
      case Failure(exception) =>
        exception match {
          case e: DynamoDbException => throw e
        }
      case Success(r) => r
    }
    val maybeItem = Option(response.item().asScala.toMap)
    maybeItem match {
      case Some(itemMap) =>
        println("Amazon dynamoDB table attributes: ")
        itemMap.keySet.foreach { k =>
          println(s"$k: ${itemMap.get(k)}")
        }
        Some(itemMap)
      case None =>
        println(s"No item found with the key: $key")
        None
    }
  }
}
