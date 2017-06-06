package com.rohan
import scala.StringContext
import scala.io.Source
import java.lang.String
import java.io.PrintWriter
import java.io.FileWriter
import java.io.File

object SentimentAlgTester {
  def main(args: Array[String]){
    val posWordsFilename = "posWords.txt"
    val negWordsFilename = "negWords.txt"
    val tweetsFilename = "testingSet.txt"
    
    //load map of positive words
    
    val posWordsMap = scala.collection.mutable.Map[String, Int]()
    val negWordsMap = scala.collection.mutable.Map[String, Int]()
    //bufferedSource.getLines.map(_.split("\t")).map(posWordsMap(_(0))=_(1).toInt)
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
    
    var total = 0
    var correct = 0
    val tweetsBufferedSource = io.Source.fromFile(tweetsFilename)
    for(line <- tweetsBufferedSource.getLines){
      val cols = line.split("\t")
      val realScore = cols(0)
      val text = cols(1)
      if(realScore==scoreTweet(text).toString)
        correct += 1
      total +=1
    }
    println("Number correct: " + correct)
    println("Number total: " + total)
    println("Accuracy: " + (correct.toDouble/total.toDouble)*100 + "%")
    
    
    def scoreTweet(text: String) = {
      val words = text.split(" ")
      var finalScore = 0
      var totPoints = 0.0
      var totNegPoints = 0
      for(word <- words){
        val posPoints = posWordsMap.getOrElse(word, 0)
        val negPoints = negWordsMap.getOrElse(word, 0)
        //if(thisPosPoints > 10000) println(word + ": posPoints:" + thisPosPoints)
        //if(thisNegPoints > 10000) println(word + ": negPoints:" + thisNegPoints)
        val wordPoints = (posPoints-negPoints).toDouble/(posPoints+negPoints).toDouble
        //println(word + " points: " + wordPoints)
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