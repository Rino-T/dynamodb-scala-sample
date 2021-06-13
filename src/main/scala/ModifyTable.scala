import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.{
  DynamoDbException,
  ProvisionedThroughput,
  ResourceNotFoundException,
  UpdateTableRequest,
  UpdateTableResponse
}

import java.net.URI
import scala.util.{Failure, Success, Try}

object ModifyTable {
  def main(args: Array[String]): Unit = {
    val client: DynamoDbClient = DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build()
    updateDynamoDBTable(client, "users", 8, 8).foreach(println)
    client.close()
  }

  def updateDynamoDBTable(
      ddb: DynamoDbClient,
      tableName: String,
      readCapacity: Long,
      writeCapacity: Long
  ): Option[UpdateTableResponse] = {
    println(s"updating $tableName with new provisioned throughput values")
    println(s"Read capacity: $readCapacity")
    println(s"Write capacity: $writeCapacity")

    val tableThroughput = ProvisionedThroughput
      .builder()
      .readCapacityUnits(readCapacity)
      .writeCapacityUnits(writeCapacity)
      .build()

    val request = UpdateTableRequest
      .builder()
      .provisionedThroughput(tableThroughput)
      .tableName(tableName)
      .build()

    Try {
      ddb.updateTable(request)
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
