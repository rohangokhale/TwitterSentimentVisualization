Twitter Sentiment Visualization

This a project that collects and cleans tweets relating to current trending topics, and performs sentiment analysis on them using a
naive bayes algorithm. All of this is code is written in Scala. These scores are stored in a SQL database, and a visualization of the
data can be found at www.twitterfeels.com (updated daily). The visualization was done using D3.js.


Collection
Each hour, the top 10 current trending twitter topics are obtained, and then tweets are collected that pertain to those trends. This
is done with the help of the twitter4j streaming API for twitter.
Finding the current top twitter trends and then collecting tweets that pertain to those trends is done using twitter4j API. Each hour,
the current trending topics are 

Cleaning

Sentiment Analysis
