#!/bin/bash

set -e

# generate docs
lein codox

# remove old docs.
rm -f ../codox.docs/*html
rm -rf ../codox.docs/css
rm -rf ../codox.docs/js

#copy in the new stuff.
cp -r ../target/doc/* ../cortex.docs

# commit to the cortex.docs repo/submodule
git add -u
git commit -m "update cortex docs"
git push
