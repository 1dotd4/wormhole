#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <inttypes.h>
#include <fcntl.h> 
#include <sys/stat.h> 
#include <sys/types.h> 
#include <unistd.h> 


#include "AES.Lib.h"

// In questo caso facciamo il setup dell'array state e
// usiamo l'algoritmo come PRNG di len byte.

uint8_t roundKey[NR_ROUNDS+1][WORDS_IN_KEY][BYTES_IN_WORD];
uint8_t Key[WORDS_IN_KEY][BYTES_IN_WORD];
 uint8_t vec[BLOCK_SIZE];

int main(int argc, char *argv[]) {
	char fileNameIn[20];
	char fileNameOut[20];
	int id = 0;
	uint8_t buf[16];
	/* Using
	 * mkfifo Java2C000
	 * mkfifo C2Java000
	 */
	sprintf(fileNameIn ,"Java2C%03d", id);
    	sprintf(fileNameOut,"C2Java%03d", id); 
	int fpIn;
  	int fpOut;
	fpIn=open(fileNameIn,O_RDONLY);
  	fpOut=open(fileNameOut,O_WRONLY);
	int mode;

	do {
    		read(fpIn,buf,17);
		mode =(uint8_t)buf[0];
		buf[0] = 0x04;
		if(mode != 4)
			printf("C in mode %d\n", mode);
		if (mode == 0) {
			for(uint8_t i = 0; i < 16; i++)
				vec[i] = buf[i+1];
			decryptAES(&buf[1], roundKey);
			CBC(&buf[1], vec);
			write(fpOut,buf,17);

		}
		else if(mode == 1) {
			CBC(&buf[1], vec);
			encryptAES(&buf[1], roundKey);
			for(uint8_t i = 0; i < 16; i++)
				vec[i] = buf[i+1];
			write(fpOut,buf,17);
		}
		else if (mode == 2) {
      			for(uint8_t i = 0; i < 16; i++){
				vec[i] = buf[i+1];
				buf[i+1] = 0x02;};
			write(fpOut,buf,17);
		}
		else if (mode == 3) {
			for(uint8_t i = 0; i < 4; i++)
			for(uint8_t j = 0; j < 4; j++){
				Key[i][j] = buf[i+4*j+1];
				buf[i+4*j+1] = 0x03;};
			roundKeyGen(roundKey,Key);
			write(fpOut,buf,17);
		}
		/*else {*/
		/*}*/
    	} while(1 != 0); //altrimenti esco
	close(fpOut);
  	close(fpIn); 
	return 0;
}
