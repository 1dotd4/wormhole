#!/bin/sh

## README BEFORE RUNNING!
# Launch this script from root directory of the project


trap 'finished' INT

finished() {
  trap '' INT TERM
  kill -TERM 0
  wait
}

./bin/C_AES test &
# catch the background process

java -classpath ./bin PipeController

# close C_AES
finished
