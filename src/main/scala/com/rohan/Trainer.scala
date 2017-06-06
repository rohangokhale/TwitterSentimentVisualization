package com.rohan
import scala.StringContext
import scala.io.Source
import java.lang.String
import java.io.PrintWriter
import java.io.FileWriter
import java.io.File

object Trainer {
  def main(args: Array[String]){
    //filenames
    val inputFilename="trainingSet.txt"
    
    //set up map
    val posWordsDist = scala.collection.mutable.Map[String, Int]()
    val negWordsDist = scala.collection.mutable.Map[String, Int]()
    
    
    val bufferedSource = io.Source.fromFile(inputFilename)
    //for each tweet
    for(line <- bufferedSource.getLines){
        val cols = line.split("\t").map(_.trim)
        var text = cols(1).split(" ")
        text = StatusCleaner.filterStopWords(text)
        //println(cols(0) + "\t" + text.mkString(" "))
        if(cols(0)=="1") {//positive tweet
          for(word <- text) posWordsDist(word)=posWordsDist.getOrElse(word, 0)+1
        }
        else{ //negative tweet
          for(word <- text) negWordsDist(word)=negWordsDist.getOrElse(word, 0)+1
        }
    }
    
    val sortedPosWordsDist = posWordsDist.toSeq.sortWith(_._1 > _._1)
    val sortedNegWordsDist = negWordsDist.toSeq.sortWith(_._1 > _._1)
    
    val posWordsFilename = "posWords.txt"
    val posFile = new File(posWordsFilename)
    posFile.createNewFile
    val posWriter = new PrintWriter(new FileWriter(posWordsFilename, true))
    
    val negWordsFilename = "negWords.txt"
    val negFile = new File(negWordsFilename)
    negFile.createNewFile
    val negWriter = new PrintWriter(new FileWriter(negWordsFilename, true))
    
    //for(word<-sortedPosWordsDist) posWriter.println(word._1 + "\t" + word._2)
    //for(word<-sortedNegWordsDist) negWriter.println(word._1 + "\t" + word._2)
    for ((k, v) <- posWordsDist) {
        posWriter.println(k + "\t" + v)
    }
    
    for ((k, v) <- negWordsDist) {
        negWriter.println(k + "\t" + v)
    }
    
    
    posWriter.close
    negWriter.close
    
  }
}
