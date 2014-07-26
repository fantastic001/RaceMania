#include "UDPServer.hpp"

#include <iostream>
#include <cstring>

using namespace std;

int main(int argc, char * argv[])
{
	UDPServer s(5000);
	cout <<"TestServer" <<endl;
	while (true)
	{
		char buf[4];
		s.recieve(buf, 4);
		char ax,ay,az,ox,oy,oz;
		int n = sizeof(int);
		memcpy(&ax, buf, 1);
		memcpy(&ay, buf+1, 1);
		memcpy(&az, buf+2, 1);
		char indicator = buf[3];
		cout <<(int)ax <<" " <<(int)ay <<" " <<(int)az <<" " <<indicator <<endl;
	}
	return 0;
}
