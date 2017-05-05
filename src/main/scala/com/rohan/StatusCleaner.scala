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
    val trackedTrends = trackedTrendsSource.getLines.toArray
    trackedTrendsSource.close
    
    val bufferedSource = io.Source.fromFile("rawTweets.txt")
    var rawCount = 0
    var cleanCount = 0
    for (line <- bufferedSource.getLines) {
          val cols = line.split("\t").map(_.trim)
          rawCount += 1
          for(trend <- trackedTrends) {
            if(cols(0).contains(trend)){
              cleanWriter.println(trend)
              cleanCount += 1
            }
          }
     }
    println("rawCount: " + rawCount + "cleanCount: " + cleanCount)
    bufferedSource.close
    cleanWriter.close
    
  }
}
  