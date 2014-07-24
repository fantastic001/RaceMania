#include "UDPServer.hpp" 

#include <iostream> 
#include <cstring>

using namespace std;  

int main(int argc, char * argv[]) 
{
	UDPServer s(5000); 
	while (true) 
	{
		char buf[25]; 
		s.recieve(buf, 25); 
		int ax,ay,az,ox,oy,oz; 
		int n = sizeof(int);
		memcpy(&ax, buf, n);
		memcpy(&ay, buf+n, n);
		memcpy(&az, buf+2*n, n);
		memcpy(&ox, buf+3*n, n);
		memcpy(&oy, buf+4*n, n);
		memcpy(&oz, buf+5*n, n);
		char indicator = buf[24];
		cout << ax << " " << ay << " " << az << " " << ox << " " << oy << " " << oz << indicator << endl; 
	}
	return 0; 
}
