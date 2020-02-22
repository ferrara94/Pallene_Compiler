#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <math.h>

int x_int;
int y_int;
float x_float;
float y_float;
char welcome[30];
int sommaInt(int x, int y);
float sommaFloat(float x, float y);
int moltiplicazioneInt(int x, int y);
float moltiplicazioneFloat(float x, float y);
int divisione(int x, int y);
int potenza(int x, int potenza);
int fibonacci(int i);
void inizializeGlobalVars();

int sommaInt(int x, int y) {
	
	return x + y;

}

float sommaFloat(float x, float y) {
	
	return x + y;

}

int moltiplicazioneInt(int x, int y) {
	
		
int result = 0;	
for( int i = 0; i < y; i++) {
		result = result + x;
	}
	return result;
	

}

float moltiplicazioneFloat(float x, float y) {
	
		
float result = 0.0;	
for( int i = 0; i < y; i++) {
		result = result + x;
	}
	return result;
	

}

int divisione(int x, int y) {
	
	return x / y;

}

int potenza(int x, int potenza) {
	
		
int result = 1;	
for( int i = 1; i <= potenza; i++) {
		result = result * x;
	}
	return result;
	

}

int fibonacci(int i) {
	
	if( i < 0 ) {
	return -1;
	
	}
	if( i == 0 ) {
	return 0;
	
	}
	if( i == 1 ) {
	return 1;
	
	}else {
	
	return fibonacci(i - 1) + fibonacci(i - 2);
	}

}

int main(void) {
	inizializeGlobalVars();
		
int scelta = 0;	
float x_float;	
float y_float;	
int x_int;	
int y_int;	
int somma;	
int prodotto;	
int divRes;	
int power;	
int fibonacciRes;	
char br[30] = "-----------------------------";	
while( ( scelta >= 0) && ( scelta <= 4) ){
		printf("I valori tra 0 e 4 sono usati per scegliere un'operazione");
	printf("\n");
		printf("i) 0 = addizione\nii) 1 = moltiplicazione\niii) 2 = divisione\niv) 3 = potenza\nv) 4 = fibonacci\n scelta diversa da [0-4] per uscire dal ciclo\nInserisci valore valore: ");
	printf("\n");
		scanf("%d", &scelta);
		printf("\n");
	if( scelta == 0 ) {
		printf("Somma di due interi");
	printf("\n");
		printf("Inserisci il Valore x: ");
	printf("\n");
		scanf("%d", &x_int);
		printf("\n");
		printf("Inserisci il Valore y: ");
	printf("\n");
		scanf("%d", &y_int);
		printf("\n");
	somma = sommaInt(x_int, y_int);
		printf("\nSomma = ");
	printf("%d", somma);
	
printf("\n");
		printf("%s", br);
	printf("\n");
	
	}
	if( scelta == 1 ) {
		printf("Moltiplicazioni di due interi con Somma");
	printf("\n");
		printf("Inserisci il Valore x: ");
	printf("\n");
		scanf("%d", &x_int);
		printf("\n");
		printf("Inserisci il Valore y: ");
	printf("\n");
		scanf("%d", &y_int);
		printf("\n");
	prodotto = moltiplicazioneInt(x_int, y_int);
		printf("\nMoltiplicazione = ");
	printf("%d", prodotto);
	
printf("\n");
		printf("%s", br);
	printf("\n");
	
	}
	if( scelta == 2 ) {
		printf("Divisione di due interi");
	printf("\n");
		printf("Inserisci il Valore x: ");
	printf("\n");
		scanf("%d", &x_int);
		printf("\n");
		printf("Inserisci il Valore y: ");
	printf("\n");
		scanf("%d", &y_int);
		printf("\n");
	divRes = divisione(x_int, y_int);
		printf("\nDivisione = ");
	printf("%d", divRes);
	
printf("\n");
		printf("%s", br);
	printf("\n");
	
	}
	if( scelta == 3 ) {
		printf("Elevamento a Potenza");
	printf("\n");
		printf("Inserisci il Valore x: ");
	printf("\n");
		scanf("%d", &x_int);
		printf("\n");
		printf("Inserisci l'elevamento a potenza: ");
	printf("\n");
		scanf("%d", &y_int);
		printf("\n");
	power = potenza(x_int, y_int);
		printf("\nPotenza = ");
	printf("%d", power);
	
printf("\n");
		printf("%s", br);
	printf("\n");
	
	}
	if( scelta == 4 ) {
		printf("Fibonacci");
	printf("\n");
		printf("Inserisci il Valore x: ");
	printf("\n");
		scanf("%d", &x_int);
		printf("\n");
	fibonacciRes = fibonacci(x_int);
		printf("\nFibonacci = ");
	printf("%d", fibonacciRes);
	
printf("\n");
		printf("%s", br);
	printf("\n");
	
	}
	if( ( scelta > 4) || ( scelta < 0) ) {
		printf("Valore inserito: ");
	printf("%d", scelta);
	
printf("\n");
		printf("Fine");
	printf("\n");
		break;
	
	
	}
	
}
	
	return 0;
}

void inizializeGlobalVars() { 

	x_int = 10;
	y_int = 10;
	x_float = 25.5;
	y_float = 10.50;
	strcpy(welcome, "myPallene language");

}