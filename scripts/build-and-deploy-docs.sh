#!/bin/bash

set -e

script_dir=$(dirname $0)

# generate docs
lein codox

# remove old docs.
rm ../codox.docs/*html
rm -r ../codox.docs/css
rm -r ../codox.docs/js

#copy in the new stuff.
cp -r ../target.docs/ ../codox.docs

git add -u
git commit -m "update cortex docs"
git push
