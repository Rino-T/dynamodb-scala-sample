package table

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.{
  DeleteTableRequest,
  DeleteTableResponse,
  DynamoDbException,
  ResourceNotFoundException
}

import java.net.URI
import scala.util.{Failure, Success, Try}

object DeleteTable {

  def main(args: Array[String]): Unit = {
    val client: DynamoDbClient = DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build()
    deleteDynamoDbTable(client, "roles")
  }

  def deleteDynamoDbTable(ddb: DynamoDbClient, tableName: String): Option[DeleteTableResponse] = {
    val request = DeleteTableRequest.builder().tableName(tableName).build()

    Try {
      ddb.deleteTable(request)
    } match {
      case Failure(exception) =>
        exception match {
          case e: ResourceNotFoundException =>
            println(s"table $tableName is not found")
            None
          case e: DynamoDbException => throw e
        }
      case Success(response) => Some(response)
    }
  }

}
