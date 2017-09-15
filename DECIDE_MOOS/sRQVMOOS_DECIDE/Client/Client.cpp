#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdlib.h>
#include <cstring>
#include <unistd.h>
//#include <string>
#include <iostream>


using namespace std;

int sockfd, portno, n;

struct sockaddr_in serv_addr;
struct hostent *server;


//-------------------------------
void error(const char *msg)
{
    perror(msg);
    exit(0);
}


//-------------------------------
int initialiseClient(int portNumber){
	const char * argv[]={"socket","localhost", "56567"};
    portno = portNumber;//atoi(argv[2]);
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0)
        error("ERROR opening socket");


    server = gethostbyname(argv[1]);
    if (server == NULL) {
        fprintf(stderr,"ERROR, no such host\n");
        exit(0);
    }


    memset((char *) &serv_addr, 0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr,
          (char *)&serv_addr.sin_addr.s_addr,
          server->h_length);
    serv_addr.sin_port = htons(portno);
    if (connect(sockfd,(struct sockaddr *)&serv_addr,sizeof(serv_addr)) < 0)
        error("ERROR connecting");

    return (1);
}

//-------------------------------
float runPrism(char *variables){
	n = write(sockfd,variables,strlen(variables));
	if (n < 0)
		error("ERROR writing to socket");

	memset(variables, 0, sizeof(char)*256);
	n = read(sockfd, variables, 255);
    return (1.0);
}
