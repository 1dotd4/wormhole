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



int main(int argc, char *argv[]) {
  if (argc != 2) {
    fprintf(stderr, "Usage: %s <pipe-prefix-name>", argv[0]);
    return -1;
  }

  char *pipe_prefix = argv[1];
  char fileNameIn[20];
  char fileNameOut[20];
  sprintf(fileNameIn ,"%s_in", pipe_prefix);
  sprintf(fileNameOut,"%s_out", pipe_prefix); 
  int fpIn = open(fileNameIn,O_RDONLY);
  int fpOut = open(fileNameOut,O_WRONLY);

  uint8_t roundKey[NR_ROUNDS+1][WORDS_IN_KEY][BYTES_IN_WORD];
  uint8_t Key[WORDS_IN_KEY][BYTES_IN_WORD];
  uint8_t vec[BLOCK_SIZE];
  uint8_t vecd[BLOCK_SIZE];
  uint8_t buf[17];
  uint8_t *data;

  // To put the protocol simply:
  // 17 bytes, the first byte is the operation to apply to the following 16bytes.
  // 0 - decrypt
  // 1 - encrypt
  // 2 - set IV
  // 3 - set key
  // 4 - exit
  uint8_t mode = 5;
  while (mode != 4) {
    read(fpIn, buf, 17);
    mode = (uint8_t) buf[0];
    data = buf + 1;
    printf("C in mode %d\n", mode);
    switch (mode) {
      case 0:
	for(uint8_t i = 0; i < 16; i++) {
	  vecd[i] = vec[i];
	}
        for(uint8_t i = 0; i < 16; i++) {
          vec[i] = data[i];
        }
        decryptAES(data, roundKey);
        CBC(data, vecd);
        write(fpOut, buf, 17);
      break;
    case 1:
      CBC(data, vec);
      encryptAES(data, roundKey);
      for(uint8_t i = 0; i < 16; i++) {
        vec[i] = data[i];
      }
      write(fpOut, buf, 17);
      break;
    case 2:
      for(uint8_t i = 0; i < 16; i++){
        vec[i] = data[i];
        data[i] = 0x02;
      }
      write(fpOut, buf, 17);
      break;
    case 3:
      for(uint8_t i = 0; i < 4; i++) {
        for(uint8_t j = 0; j < 4; j++){
          Key[i][j] = data[i+4*j];
          data[i+4*j] = 0x03;
        }
      }
      roundKeyGen(roundKey,Key);
      write(fpOut, buf, 17);
      break;
    default:
      break;
    } // end switch
  } // end while

  close(fpOut);
  close(fpIn); 

  return 0;
}
