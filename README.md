wormhole - file transfer

# Utilizzo

Per utilizzare il progetto per prima cosa bisogna modificare la prima riga del `Makefile` in modo tale che punti alla directory di java corretta:

```
JAVAHOME=/directory/corretta
```

e eseguire `make`. Dopo questo bisogna generare la key e l'initial vector con openssl:

```
openssl rand 16 > key
openssl rand 16 > iv
```

e si può procedere a lanciare il server e il client (in due terminali diversi)usando `./scripts/run_server.sh` per il server e `./scripts/run_client.sh` per il client.

A questo punto per il client si aprirà una finestra con un pulsante da utilizzare per selezionare un file da caricare. Il file si troverà all'interndo della cartella `uploaded`.


# Analisi dei requisiti

Si intende realizzare un semplice file transfer tra computer.
Per fare cio' si utilizzera' AES e un socket.
AES verra' gestito da un processo C, mentre l'interfaccia grafica si occupera' di recuperare il file, cifrarlo con il modulo AES tramite PIPE e inviarlo al server.
Il server, ricevuto il file cifrato, provvedera' a decifrarlo e salvarlo.
Si intende realizzare le interfacce grafiche in JavaFX con fxml.

## Requisiti funzionali

- trasferire un file cifrato;
- interfaccia in JavaFX con fxml;
- utilizzo dei pipe per comunicare con un processo in background per la cifratura dei messaggi;
- utilizzo dei socket per comunicazione client-server;
- utilizzo di AES128.

## Requisiti non funzionali

- usare un algoritmo implementato (ARC4, A5/1, AES128)
- il sistema deve avere almenodue processi in background che comunicano con pipe verso Java.
- Interfaccia in Java (console, JavaFX, JavaFX + fxml)
- Si possono usare i socket
- Deve contenere una breve descrizione dell'utilizzo

# Descizione dell'architettura

```
[C_AES] =2xpipe= [Client] --socket--> [Server] =2xpipe= [C_AES]
```

## `C_AES`

`C_AES` e' un modulo crittografico che riceve comandi da pipe e restituisce il risultato su una pipe di output.
Questo implementa sia le funzioni di cifratura che decifratura che vanno impostati correttamente.

Il protocollo permette le seguenti funzioni che vengono comunicate con un byte per il codice e i restanti sono fissi.
- `3[key_to_set]`: imposta la chiave per cifrare e decifrare i blocchi;  
In questo caso la pipe di lettura è settata a `43333333333333333`
- `2[iv_to_set]`: imposta l'initial vector da utilizzare per cifrare i vari blocchi;  
In questo caso la pipe di lettura è settata a `42222222222222222`
- `1[chunk_to_encrypt]`: cifra un chunk di lunghezza 16byte;  
In questo caso la pipe di lettura è settata a `4[chunk_encrypted]`
- `0[chuck_to_decrypt]`: decifra un chunk.  
In questo caso la pipe di lettura è settata a `4[chunk_decrypted]`

Nello specifico, questo modulo tiene in memoria la chiave e l'IV impostati e li aggiorna rispettivamente quando richiesto e quando viene cifrato/decifrato un blocco.

### Come eseguire C

### Come eseguire C
Eseguire il comando `./pipe id` per creare una pipe e un programma in ascolto.
A questo punto basta eseguire in java:

```
pipe_in.pipe_set(id); // per settare la pipe
pipe_in.write_read(buf); // ogni volta che voglio scirvere e eseguire 17 byte
```


## Client

Il client e' un'interfaccia grafica che, dopo aver inserito la chiave di sessione, permette di effettuare drag-and-drop di file.
Questo file verra' letto, paddato (con padding PKCS#7), cifrato e inviato al server seguendo il protocollo sotto indicato.

## Server

Il server e' un'interfaccia grafica che prende da file la chiave.
Ci si aspetta che il client utilizzi la stessa chiave per cifrare il file.
Il server si aspetta che il client comunichi seguendo il seguente flusso.

- Scelto il file, il Client si collega e invia `Hello there`.
- Il Server risponde `General Kenobi`.
- Il Client una volta scelto il file inviera' il comando `U:foo.txt:123:` (32 byte al massimo in chiaro) dove `foo.txt` (quanti byte mi aspetto?) e' il file che si intende caricare e `123` (2 byte) sono i byte da trasferire.
- Il Server risponde con `NO` se il file esiste gia' o se e' troppo grande, altrimenti `OK`.
- Il Client inizia a trasferire i byte e il Server li riceve.
- Il Client e il Server, una volta inviati e ricevuti i byte indicati, rispettivamente, chiudono il socket e viene reimpostato l'IV di default.

Nel caso il Client o il Server si discostino da questo comportamento, la connessione viene chiusa e le eccezioni generate vengono mostrate all'utente.
Si noti che queste operazioni sono single thread.
