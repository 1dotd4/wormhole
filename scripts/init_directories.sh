#!/bin/sh

## README BEFORE RUNNING!
# Launch this script from root directory of the project

mkdir -p run

mkfifo run/client_in
mkfifo run/client_out

mkfifo run/server_in
mkfifo run/server_out
