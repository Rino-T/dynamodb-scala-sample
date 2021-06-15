package item

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.{AttributeValue, DynamoDbException, GetItemRequest}

import java.net.URI
import java.util
import scala.jdk.CollectionConverters.{IterableHasAsScala, MapHasAsJava}
import scala.util.{Failure, Success, Try}

object GetItemAsync {
  def main(args: Array[String]): Unit = {
    val client = DynamoDbAsyncClient.builder().endpointOverride(URI.create("http://localhost:8000")).build()
    getItem(client, "users", "user_id", "44244091-f580-4dee-a3c7-b3564d9ff20a")
    client.close()
  }

  def getItem(client: DynamoDbAsyncClient, tableName: String, key: String, keyVal: String): Unit = {

    val keyToGet = Map(key -> AttributeValue.builder().s(keyVal).build())

    // create a GetItemRequest instance
    val request = GetItemRequest.builder().key(keyToGet.asJava).tableName(tableName).build()

    Try {
      // Invoke the DynamoDbAsyncClient object's getItem
      val returnedItem: util.Collection[AttributeValue] = client.getItem(request).join().item().values()
      returnedItem
    } match {
      case Failure(exception) =>
        exception match {
          case e: DynamoDbException => throw e
        }
      case Success(returnedItem) =>
        // Convert Set to Map
        val map: Map[String, AttributeValue] = returnedItem.asScala.map(v => v.s -> v).toMap
        val keys: Set[String]                = map.keySet

        keys.foreach { sinKey =>
          println(s"$sinKey : ${map.get(sinKey)}")
        }
    }
  }
}
