#include "UDPServer.hpp" 

#include <iostream> 

using namespace std;  

int main(int argc, char * argv[]) 
{
	UDPServer s(5000); 
	while (true) 
	{
		char buf[24]; 
		s.recieve(buf, 24); 
		float* x = (float*) buf; 
		float *y = (float*) buf+8;
		float* z = (float*) buf+16;
		cout << *x << " " << *y << " " << *z << endl;
	}
	return 0; 
}
