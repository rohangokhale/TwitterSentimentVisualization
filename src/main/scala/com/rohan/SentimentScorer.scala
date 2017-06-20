package com.rohan
import scala.StringContext
import scala.io.Source
import java.lang.String
import java.io.PrintWriter
import java.io.FileWriter
import java.io.File

object SentimentScorer {
  def main(args: Array[String]){
    //usage: [cleanTweetsFilename], [scoresOutputFilename]
    val posWordsFilename = "posWords.txt"
    val negWordsFilename = "negWords.txt"
    //val tweetsFilename = "cleanTweets" + java.time.LocalDate.now.toString + ".txt"
    val tweetsFilename = args(0)
    //val scoresFilename = "scores" + java.time.LocalDate.now.toString + ".txt"
    val scoresFilename = args(1)
    val trendCol = 0
    val textCol = 1
    val locCol = 2
    //get yesterday's date to add to 'date' column in the scores file
    val date = java.time.LocalDate.now.minusDays(1).toString
    
    //Load map of positive words
    val posWordsMap = scala.collection.mutable.Map[String, Int]()
    val negWordsMap = scala.collection.mutable.Map[String, Int]()
    val posBufferedSource = io.Source.fromFile(posWordsFilename)
    for(line <- posBufferedSource.getLines){
      var cols = line.split("\t")
      posWordsMap(cols(0))=cols(1).toInt
    }
    val negBufferedSource = io.Source.fromFile(negWordsFilename)
    for(line <- negBufferedSource.getLines){
      var cols = line.split("\t")
      negWordsMap(cols(0))=cols(1).toInt
    }
    

    //Set up the output file
    val scoresFile = new File(scoresFilename)
    scoresFile.createNewFile
    val scoresWriter = new PrintWriter(new FileWriter(scoresFilename, true))
    //Score each tweet, and write the trend, score, and location to a file
    //Format (tab delimited): trend  score  location
    val tweetsBufferedSource = io.Source.fromFile(tweetsFilename)
    for(line <- tweetsBufferedSource.getLines){
      val cols = line.split("\t")
      val text = cols(textCol)
      val score = scoreTweet(text)
      if(score == 0 || score == 1)
        scoresWriter.println(cols(trendCol) + "\t" + score + "\t" + cols(locCol) + "\t" + date)
    }
    scoresWriter.close

    def scoreTweet(text: String) = {
      val words = text.split(" ")
      var finalScore = 0
      var totPoints = 0.0
      var totNegPoints = 0
      for(word <- words){
        val posPoints = posWordsMap.getOrElse(word, 0)
        val negPoints = negWordsMap.getOrElse(word, 0)
        val wordPoints = (posPoints-negPoints).toDouble/(posPoints+negPoints).toDouble
        if(!wordPoints.isNaN) {
          //if(wordPoints > 0.2 || wordPoints < -0.2)
            totPoints += wordPoints
          //if(wordPoints < 0.07 && wordPoints > -0.07) println(word + " points: " + wordPoints)
        }
      }
      if(totPoints > 0) finalScore=1
      else if(totPoints < 0) finalScore=0
      else finalScore= -1
      finalScore
    }
  }
}
