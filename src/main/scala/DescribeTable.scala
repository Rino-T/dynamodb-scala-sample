import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.{
  DescribeTableRequest,
  DynamoDbException,
  ResourceNotFoundException,
  TableDescription
}

import java.net.URI
import scala.jdk.CollectionConverters.IterableHasAsScala
import scala.util.{Failure, Success, Try}

object DescribeTable {

  def main(args: Array[String]): Unit = {
    val client: DynamoDbClient = DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build()
    val description            = describeDynamoDBTable(client, "users")
    description.foreach(describeTable)
    client.close()
  }

  def describeDynamoDBTable(ddb: DynamoDbClient, tableName: String): Option[TableDescription] = {
    val request = DescribeTableRequest.builder().tableName(tableName).build()
    Try {
      ddb.describeTable(request).table()
    } match {
      case Failure(exception) =>
        exception match {
          case e: ResourceNotFoundException => None
          case e: DynamoDbException         => throw e
        }
      case Success(description) => Some(description)
    }
  }

  def describeTable(desc: TableDescription): Unit = {
    println(s"Table name : ${desc.tableName()}")
    println(s"Table ARN : ${desc.tableArn()}")
    println(s"Status : ${desc.tableStatus()}")
    println(s"Item count : ${desc.itemCount().longValue()}")
    println(s"Size (bytes) : ${desc.tableSizeBytes().longValue()}")

    val throughputInfo = desc.provisionedThroughput()

    println("Throughput")
    println(s" Read Capacity : ${throughputInfo.readCapacityUnits().longValue()}")
    println(s" write Capacity : ${throughputInfo.writeCapacityUnits().longValue()}")

    val attributes = desc.attributeDefinitions()

    println("Attributes")
    attributes.asScala.toSeq.foreach { a =>
      println(s" ${a.attributeName()}, ${a.attributeType()}")
    }
  }
}
