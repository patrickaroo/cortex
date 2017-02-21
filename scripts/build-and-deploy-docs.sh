#!/bin/bash

set -e

# udpate submodules, including cortex.docs
git submodule init
git submodule update --remote --merge

# make sure we're on master.
cd ./cortex.docs
git checkout master
#git fetch
#git merge origin/master

# generate docs
cd ..
lein codox

# remove old docs.
rm -f ./cortex.docs/*html
rm -rf ./cortex.docs/css
rm -rf ./cortex.docs/js

#copy in the new stuff.
cp -r ./target/doc/* ./cortex.docs

# commit to the cortex.docs repo/submodule
cd ./cortex.docs
git add -u
git add *
git add css/*
git add js/*
git commit -m "update cortex docs"
git push
