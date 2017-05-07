package com.rohan

import scala.StringContext
import scala.io.Source

import java.lang.String
import java.io.PrintWriter
import java.io.FileWriter
import java.io.File
import java.time.LocalDate


object StatusCleaner {
  def main(args: Array[String]){
    //create clean tweets file
    val date = java.time.LocalDate.now.toString
    val cleanfilename = "cleanTweets.txt"
    val cleanFile = new File(cleanfilename)
    cleanFile.createNewFile
    val cleanWriter = new PrintWriter(new FileWriter(cleanfilename, true))
    
    //get the trends that were tracked
    val trendsfilename = "trackedTrends.txt"
    val trackedTrendsSource = Source.fromFile(trendsfilename, "UTF-8")
    var trackedTrends = trackedTrendsSource.getLines.toArray
    trackedTrends = trackedTrends.map(_.toLowerCase)
    trackedTrendsSource.close
    
    //open the file containing the raw tweets
    val bufferedSource = io.Source.fromFile("rawTweets.txt")
    var rawCount = 0
    var cleanCount = 0
    
    
    
    
    /////////////////////////////////
    for(line <- bufferedSource.getLines) {
      rawCount += 1
      val cols = line.split("\t").map(_.trim.toLowerCase)
      val trend = trendMatcher(cols(0), trackedTrends)
      //println(trend)//
      if (trend != null){
        //println("Checking Location...")//
        val location = locMatcher(cols(1).toUpperCase, LocationList.locMap)
        if (location != null){
          cleanWriter.println(trend + "\t" + cols(0) + "\t" + location)
          cleanCount += 1
        }
      }
    }

    
    /////////////////////////////
    println("rawCount: " + rawCount + "cleanCount: " + cleanCount)
    bufferedSource.close
    cleanWriter.close
    
    
    
    def trendMatcher(status: String, trends: Array[String]) = {
      var matchedTrend: String = null
      for(trend <- trends if matchedTrend == null){
        //println(status + "Trend: " + trend)//
        if(status.contains(trend)) matchedTrend = trend
      }
      matchedTrend //return null if there are no matches
    }
    
    def locMatcher(tLoc: String, locMap: Map[String, String]) = {
      var state: String = null
      for ((k, v) <- locMap if state == null) {
        //println("Key: " + k + " tLoc: " + tLoc)
        if (tLoc.contains(k)) state = v
      }
      state //returns null if no state abbreviation was found
      
    }
  }
}


  