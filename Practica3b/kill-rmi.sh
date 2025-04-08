#!/bin/bash

# Buscar el PID del proceso 'rmiregistry'
pid=$(ps | grep '[r]miregistry' | awk '{print $1}')

if [ -z "$pid" ]; then
    echo "No se encontró ningún proceso rmiregistry ejecutándose."
else
    echo "Matando proceso rmiregistry con PID: $pid"
    kill -9 "$pid"
    echo "Proceso rmiregistry terminado."
fi
