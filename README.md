# Twitter Sentiment Visualization

This a project that collects and cleans tweets relating to current trending topics, and performs sentiment analysis on them using a
naive bayes algorithm. All of this is code is written in Scala. These scores are stored in a SQL database, and a visualization of the
data can be found at www.twitterfeels.com (updated daily). The visualization was done using D3.js.


## Collection

Each hour, the top 10 current trending twitter topics are obtained, and then tweets are collected that pertain to those trends. This
is done with the help of the twitter4j streaming API for twitter. At this stage, only tweets that contain a non-empty location field
and are in english are kept. Their text is also filtered for special characters like newlines, tabs, etc. These 'raw' tweets are stored
in a file along with the trend they pertain to and the location of user who wrote the tweet.

## Cleaning

During the cleaning process, only tweets that have a location that matches with one of the locations in LocationList.scala are kept.
From there, tweets are cleaned by removing extra whitespace, certain punctuation characters, and most importantly, filtering out
'stopwords.' Stopwords are words that carry no information about sentiment, and are removed to increase the accuracy and speed of the 
sentiment scoring algorithm. The full list of stopwords can be StopWordsList.scala. The clean tweets are then stored in a file, again 
along with the trend they pertain to and the location of the user who wrote the tweet.

## Sentiment Analysis

Tweets are scored for sentiment (positive or negative) using a naive bayes classifer. The classifier is trained with a dataset from the
University of Michigan that contains real tweets as well as a sentiment score for them. A simple explanation for how the classifer works
is that it creates a two maps, one for the positive tweets and one for the negative tweets. The key for the maps is each unique word that
appears in each set of tweets, and the value is the frequency that the word shows up. This results in the algorithm learning which words
are 'positive' and which ones are 'negative'. The algorithm then uses that information to go through each word in an incoming tweet, 
decide how positive or negative it is, and then arrive at decision for whether the tweet overall expresses positive or negative sentiment.