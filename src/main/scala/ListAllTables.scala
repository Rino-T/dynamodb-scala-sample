import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.{DynamoDbException, ListTablesRequest}

import java.net.URI
import scala.jdk.CollectionConverters.ListHasAsScala
import scala.util.{Failure, Success, Try}

object ListAllTables {
  var methodCallCount = 0 // 確認用。listAllTablesが何回コールされたか

  def main(args: Array[String]): Unit = {
    val client: DynamoDbClient = DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build()
    listAllTables(client, None).foreach(println)
    client.close()

    println(methodCallCount)
  }

  def listAllTables(ddb: DynamoDbClient, lastName: Option[String]): Seq[String] = {
    methodCallCount += 1
    Try {
      val response = lastName match {
        case Some(ln) =>
          // デフォルトでは最大100件のテーブルを取得する。limit で一度に取得するtable件数を制限している。
          val request = ListTablesRequest.builder().exclusiveStartTableName(ln).limit(1).build()
          ddb.listTables(request)
        case None =>
          val request = ListTablesRequest.builder().limit(1).build()
          ddb.listTables(request)
      }

      val tableNames: Seq[String]      = response.tableNames().asScala.toSeq
      val nextLastName: Option[String] = Option(response.lastEvaluatedTableName())

      nextLastName match {
        case Some(value) => tableNames ++ listAllTables(ddb, Some(value))
        case None        => tableNames
      }
    } match {
      case Failure(exception) =>
        exception match {
          case e: DynamoDbException => throw e
        }
      case Success(value) => value
    }
  }
}
