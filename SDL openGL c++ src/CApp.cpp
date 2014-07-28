#include <stdio.h>
#include "CApp.h"
#include "UDPServer.h"

CApp::CApp() {
    Surf_Display = NULL;
    Running = true;
}

int CApp::OnExecute() {
    if(OnInit() == false) return -1; // oenGL and screen init
    //SDL_Event Event;
    UDPServer s(5000);

    while(Running) {
        /*while(SDL_PollEvent(&Event)) {
            OnEvent(&Event); // handling events, chande to receive from UDP
        }*/
        char buf[4];
		s.recieve(buf, 4);
		char ax,ay,az;
		memcpy(&ax, buf, 1);
		memcpy(&ay, buf+1, 1);
		memcpy(&az, buf+2, 1);
		float x = (int) ax;
		float y = (int) ay;
		float z = (int) az;
		x /= 120; if (x>0.6) x = 0.6; if (x<-0.6) x = -0.6;
		y /= 120; if (y>0.6) y = 0.6; if (y<-0.6) y = -0.6;
		z /= 120; if (z>0.6) z = 0.6; if (z<-0.6) z = -0.6;

        OnLoop(); // everything eles
        OnRender(y, -x, z); // printing to screen
    }
    OnCleanup(); // killing app

    return 0;
}

int main(int argc, char* argv[]) {
    CApp theApp;
    return theApp.OnExecute();
}
