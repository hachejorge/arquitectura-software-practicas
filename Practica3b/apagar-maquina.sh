#!/bin/bash

if [ "$#" -lt 1 ]; then 
    echo "Se necesita una dirección lab102"
    exit 1
fi

/usr/local/etc/shutdown.sh -y $1