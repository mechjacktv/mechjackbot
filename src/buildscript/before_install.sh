#!/bin/bash

PROTOBUF3_DIR=~/protobuf3
pushd .
if [ -d "$PROTOBUF3_DIR" ] && [ -e "$PROTOBUF3_DIR/src/protoc" ]; then
  echo "Using cached protobuf3 build ..."
  cd $PROTOBUF3_DIR
else
  echo "Building protobuf3 from source ..."
  rm -rf $PROTOBUF3_DIR
  mkdir $PROTOBUF3_DIR

  apt-get update
  apt-get install --no-install-recommends \
    autoconf \
    automake \
    libtool \
    curl \
    make \
    g++ \
    unzip \
    wget

  wget https://github.com/protocolbuffers/protobuf/releases/download/v3.6.1/protobuf-java-3.6.1.tar.gz -O protobuf3.tar.gz
  tar -xzf protobuf3.tar.gz -C $PROTOBUF3_DIR --strip 1
  rm protobuf3.tar.gz
  cd $PROTOBUF3_DIR
  ./autogen.sh
  ./configure --prefix=/usr
  make
fi
make install
popd
