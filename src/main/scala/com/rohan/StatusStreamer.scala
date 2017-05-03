package com.rohan
import scala.StringContext
import twitter4j._
import java.lang.String
import java.io.PrintWriter
import java.io.FileWriter
//import scala.util.parsing.json._
//import scala.util.


object StatusStreamer {
  def main(args: Array[String]){
      val uswoeid = 23424977 //united states woid

      val twitter = new twitter4j.TwitterFactory(Util.config).getInstance
      val topTrends = twitter.getPlaceTrends(uswoeid)
      val topTrendsArr = topTrends.getTrends
      val topTrendsStrings: Array[String] = for(trend <- topTrendsArr) yield trend.getName
      for(trend <- topTrendsStrings) println(trend)
      
      val myFilter = new FilterQuery
      myFilter.track("rangers", "cavs") //track will take strings separated by commas
      //val terms: Array[String] = Array("New York Rangers", "Ottawa Senators") //track won't take an array of strings
      myFilter.track(topTrendsStrings(0), topTrendsStrings(1), topTrendsStrings(2), topTrendsStrings(3), topTrendsStrings(4))
      
      
      val twitterStream = new TwitterStreamFactory(Util.config).getInstance
      val myListener = Util.simpleStatusListener
      twitterStream.addListener(myListener)
      twitterStream.filter(myFilter)
      Thread.sleep(60000)
      twitterStream.cleanUp
      twitterStream.shutdown
      println("Streaming Ended")
      println("Raw Count: " + myListener.rawCount)
      println("Good Count: " + myListener.goodCount)
      println("Tracked Trends: " + topTrendsStrings(0) + topTrendsStrings(1) + topTrendsStrings(2) + topTrendsStrings(3) + topTrendsStrings(4))
      
  }
}



object Util {
  
  def simpleStatusListener = new StatusListener() {
    val outputfilename = "output.txt"
    var rawCount = 0
    var goodCount = 1
    def onStatus(status: Status){
      rawCount += 1
      println(status.getText)
      if(status.getGeoLocation != null || status.getPlace != null){
        val writer = new PrintWriter(new FileWriter(outputfilename, true))
        writer.println(status.getText + "\t" + status.getPlace + "\t" + status.getGeoLocation + "\t" + status.getUser)
        writer.close
        goodCount += 1
      }
    }
    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
    def onException(ex: Exception) { ex.printStackTrace }
    def onScrubGeo(arg0: Long, arg1: Long) {}
    def onStallWarning(warning: StallWarning) {}
  }
  
  
  
  
  val config = new twitter4j.conf.ConfigurationBuilder()
    .setOAuthConsumerKey("GDLlQrENYS4aCosCRYnNTja5Y")
    .setOAuthConsumerSecret("BzfDMr8NLctrLHyNsuVLo5QbgNmde44YmGnSMXc1JpkQnJnNZ8")
    .setOAuthAccessToken("1494587119-X1tfkKvE94zrjQPZjMQfjCqMuyLiVPfMpVM0wWX")
    .setOAuthAccessTokenSecret("VQa2MhYfXaGMgv1iC8XhxzsnsP9xDAKzsr5MRcZVZEZ27")
    .build
}