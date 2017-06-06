package com.rohan
import scala.StringContext
import scala.io.Source
import java.lang.String
import java.io.PrintWriter
import java.io.FileWriter
import java.io.File

object TrainingTestingCreator {
  def main(args: Array[String]){
    val fullsetFilename = "SentimentDataset.txt"
    val trainFilename = "trainingSet.txt"
    val testFilename = "testingSet.txt"
    
    val trainFile=  new File(trainFilename)
    trainFile.createNewFile
    val trainWriter = new PrintWriter(new FileWriter(trainFilename, true))
    
    val testFile= new File(testFilename)
    testFile.createNewFile
    val testWriter = new PrintWriter(new FileWriter(testFilename, true))
    
    val bufferedSource = io.Source.fromFile(fullsetFilename)
    var counter = 0
    for(line <- bufferedSource.getLines){
      if(counter%10==0 || counter%10==1) testWriter.println(line)
      else trainWriter.println(line)
      counter += 1
    }
  }
}