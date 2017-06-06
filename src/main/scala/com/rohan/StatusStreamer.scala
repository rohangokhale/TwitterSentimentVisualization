package com.rohan
import scala.StringContext
import scala.io.Source
import twitter4j._
import java.lang.String
import java.io.PrintWriter
import java.io.FileWriter
import java.io.File
import java.time.LocalDate



object StatusStreamer {
  def main(args: Array[String]){
      val timeToCollect = args(0).toInt
      //open file to record all tracked trends for the day
      val date = java.time.LocalDate.now.toString
      //val trendsfilename = "trackedTrends.txt"
      val trendsfilename = "trackedTrends" + date + ".txt"
      val trackedTrendsFile = new File(trendsfilename)
      trackedTrendsFile.createNewFile
    
      val uswoeid = 23424977 //united states woid

      //val twitter = new twitter4j.TwitterFactory(Util.config).getInstance
      val twitter = new twitter4j.TwitterFactory(t4jCredentials.config).getInstance
      val topTrends = twitter.getPlaceTrends(uswoeid)
      val topTrendsArr = topTrends.getTrends
      val topTrendsStrings: Array[String] = for(trend <- topTrendsArr) yield trend.getName
      for(trend <- topTrendsStrings) println(trend)
      
      
      
      //add all new trends to a file containing the tracked trends for the day
      val trackedTrendsSource = Source.fromFile(trendsfilename, "UTF-8")
      val lines = trackedTrendsSource.getLines.toArray
      val writer = new PrintWriter(new FileWriter(trendsfilename, true)) //prepare trends file for appending
      for(trend <- topTrendsStrings){
        var isTracked: Boolean = false
        for(line <- lines){
          //println(trend + "==" + line + "?")
          if(trend == line) isTracked = true
        }
        if(isTracked == false) writer.println(trend)
      }
      for(trend <- topTrendsStrings) println(trend)
      trackedTrendsSource.close
      writer.close
      
      val myFilter = new FilterQuery
      //myFilter.track("rangers", "cavs") //track will take strings separated by commas
      //val terms: Array[String] = Array("New York Rangers", "Ottawa Senators") //track won't take an array of strings

      myFilter.track(topTrendsStrings(0), topTrendsStrings(1), topTrendsStrings(2), topTrendsStrings(3), topTrendsStrings(4), topTrendsStrings(4), topTrendsStrings(5), topTrendsStrings(6), topTrendsStrings(7), topTrendsStrings(8), topTrendsStrings(9))
      //myFilter.track(for(trend <- topTrendsStrings) yield trend)
      
      //val twitterStream = new TwitterStreamFactory(Util.config).getInstance
      val twitterStream = new TwitterStreamFactory(t4jCredentials.config).getInstance
      val myListener = Util.simpleStatusListener
      twitterStream.addListener(myListener)
      twitterStream.filter(myFilter)
      Thread.sleep(timeToCollect)
      twitterStream.cleanUp
      twitterStream.shutdown
      println("Streaming Ended")
      println("Raw Count: " + myListener.rawCount)
      println("Good Count: " + myListener.goodCount)
      println("Tracked Trends: " + topTrendsStrings(0) + topTrendsStrings(1) + topTrendsStrings(2) + topTrendsStrings(3) + topTrendsStrings(4) + topTrendsStrings(5) + topTrendsStrings(6) + topTrendsStrings(7) + topTrendsStrings(8) + topTrendsStrings(9))
      //println("Tracked Trends:")
      //topTrendsStrings.map(println(_))
  }
}



object Util {
  
  def simpleStatusListener = new StatusListener() {
    //val outputfilename = "rawTweets.txt"
    val outputfilename = "rawTweets" + java.time.LocalDate.now.toString + ".txt"
    var rawCount = 0
    var goodCount = 1
    def onStatus(status: Status){
      rawCount += 1
      //println(status.getText)
      if(status.getUser.getLocation != null && status.getLang == "en"){
        val writer = new PrintWriter(new FileWriter(outputfilename, true))
        writer.println(status.getText.filter(x => x!='\n' && x!='\t' && x!='\r') + "\t" + "User Location: " + status.getUser.getLocation)
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
  

}
