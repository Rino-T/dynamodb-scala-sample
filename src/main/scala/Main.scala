import com.amazonaws.services.dynamodbv2.{AmazonDynamoDB, AmazonDynamoDBClientBuilder}

object Main {
  def main(args: Array[String]): Unit = {
    val client: AmazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient()
  }
}
