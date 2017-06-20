package com.rohan

import scala.StringContext
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

import java.lang.String
import java.io.PrintWriter
import java.io.FileWriter
import java.io.File
import java.time.LocalDate


object StatusCleaner {
  def main(args: Array[String]){
    //usage: [rawTweetsFilename, cleanTweetsFilename, trackedTrendsFilename]

    //create clean tweets file
    //val date = java.time.LocalDate.now.toString
    //val rawfilename = "rawTweets" + java.time.LocalDate.now.toString + ".txt"
    val rawfilename = args(0)
    //val cleanfilename = "cleanTweets" + java.time.LocalDate.now.toString + ".txt"
    val cleanfilename = args(1)
    val cleanFile = new File(cleanfilename)
    cleanFile.createNewFile
    val cleanWriter = new PrintWriter(new FileWriter(cleanfilename, true))
    
    //get the trends that were tracked
    //val trendsfilename = "trackedTrends" + java.time.LocalDate.now.toString + ".txt"
    val trendsfilename = args(2)
    val trackedTrendsSource = Source.fromFile(trendsfilename, "UTF-8")
    var trackedTrends = trackedTrendsSource.getLines.toArray
    trackedTrends = trackedTrends.map(_.toLowerCase)
    trackedTrendsSource.close
    
    //open the file containing the raw tweets
    val bufferedSource = io.Source.fromFile(rawfilename)
    var rawCount = 0
    var cleanCount = 0
    
    
    /////////////////////////////////
    for(line <- bufferedSource.getLines) {
      rawCount += 1
      val cols = line.split("\t").map(_.trim.toLowerCase)
      val trend = trendMatcher(cols(0), trackedTrends)
      if (trend != null){
        val location = locMatcher(cols(1).toUpperCase, LocationList.locMap)
        if (location != null){
          //filter out all punctuation
          var rawtext= filterPunctuation(cols(0))
          //filter out stopwords
          var cleantext = filterStopWords(rawtext.split(" "))
          //write trend to file
          cleanWriter.print(trend + "\t")
          //write only remaining words to the file
          for(word <- cleantext) cleanWriter.print(word + " ")
          //write location to file
          cleanWriter.println("\t" + location)
          cleanCount += 1
        }
      }
    }
    /////////////////////////////
    println("rawCount: " + rawCount + "cleanCount: " + cleanCount)
    bufferedSource.close
    cleanWriter.close
  }
  
  def filterPunctuation(text: String) = {
      var cleanText = text.filter(x => x!='.' && x!= ',' && x!='-' && x!='$' && x!='*' && x!=';' && x!='"' && x!=''')
      cleanText
  }
    
  def trendMatcher(status: String, trends: Array[String]) = {
    var matchedTrend: String = null
    for(trend <- trends if matchedTrend == null){
      if(status.contains(trend)) matchedTrend = trend
    }
    matchedTrend //return null if there are no matches
  }
    
  def locMatcher(tLoc: String, locMap: Map[String, String]) = {
    var state: String = null
    var userLocation = tLoc.filter(x=> x!='.' && x!= ',' && x!=';' && x!=':')
    var words = userLocation.split("\\s+")
    for(word <- words if state == null){
    	for ((k, v) <- locMap if state == null) {
      	if(word == k) state = v //for all abbreviations and locations like "Indiana" with no spaces
    	}
    }
    for((k, v) <- locMap if state == null){//for locations like 'New Jersey', 'New Mexico' with spaces
    	if(k.split("\\s+").length > 1){
    		if(tLoc.contains(k)) state = v
    	}
    }
    state //returns null if no state abbreviation was found
  }
    
  def filterStopWords(text: Array[String]) = {
    text.filter(!StopWordsList.stopWords.contains(_))
  }
}





  