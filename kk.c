/***
$procesos
***/
#include<sys/types.h>
#include<sys/wait.h>
#include<stdlib.h>
#include<stdio.h>
#include<unistd.h>

#define NUM_PROCESOS 5

int I = 0;

void codigo_del_proceso (int id){
	int i;
	for(i = 0; i < 50; i++)
		printf("Proceso%d: i = %d, I = %d\n", id, i, I++);
	exit(id); // el id se almacena en los bits 8 al 15 antes de devolverlo al padre
}

int main(){
	int p;
	int id [NUM_PROCESOS] = {1,2,3,4,5};
	int pid;
	int salida;

	for(p = 0; p < NUM_PROCESOS; p++){
		pid= fork();
		if(pid == -1){
			perror("Error al crear un proceso: ");
			exit(-1);
		}
		else if (pid == 0) // Codigo del hijo
			codigo_del_proceso(id[p]);
	}

	// Codigo del padre
	for (p = 0; p < NUM_PROCESOS; p++){
		pid = wait (&salida);
		printf("Proceso %d con id = %x terminado\n", pid, salida >> 8);
	}
}
