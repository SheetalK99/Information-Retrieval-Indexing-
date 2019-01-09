#!/bin/sh
javac sak170006_hw1/Stemmer.java
javac sak170006_hw1/TokenisationStemmer.java
javac sak170006_hw1/ValCustomComparator.java
java sak170006_hw1/TokenisationStemmer $1
