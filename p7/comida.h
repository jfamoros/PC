#ifndef _COMIDA_H_
#define _COMIDA_H_

#include <sys/types.h>
#include <pthread.h>
#include <stdint.h>
#include <stdio.h>
#include <unistd.h>
#include <stdio.h>

#define PHILOSOPHERS 5    //Numero de filosofos
#define EAT_COUNT 20      //Numero de veces que come cada filosofo


//Estructura filosofo, contiene su indice y el de los filosofos adyacentes
typedef struct {
    uint8_t id;
    uint8_t right;
    uint8_t left;
}philosopher_t;

//Estructura que contiene todos los filosofos, sus palillos y los monitores
typedef struct {
    uint8_t picks[PHILOSOPHERS];
    pthread_cond_t canEat[PHILOSOPHERS];
    pthread_mutex_t lock;
    philosopher_t philosophers[PHILOSOPHERS];
}comida_t;

//Macro para inicializar un filosofo
#define PHILOSOPHER_INIT(id) {\
    (id),\
    (id) - 1 % PHILOSOPHERS,\
    (id) + 1 % PHILOSOPHERS\
}

//Funcion que inicializa la estructura comida_t
void setup(comida_t *comida);

void pick(uint8_t id, comida_t *comida);
void release(uint8_t id, comida_t *comida);
void eat(uint8_t id);

#endif
