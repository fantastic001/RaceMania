#include <stdio.h>
#include "Racemania.hpp"
#include "net/UDPServer.hpp"

CApp::CApp() {
    Surf_Display = NULL;
    Running = true;
}

int CApp::OnExecute() {
    if(OnInit() == false) return -1; // oenGL and screen init
    SDL_Event Event;
    UDPServer s(5000);
	UDPServer c(5001);

    while(Running) {
        while(SDL_PollEvent(&Event)) {
            OnEvent(&Event); // Only SDL events like program terminations
        }
        char buf[4];
		s.recieve(buf, 4);
        c.send(buf, 3, "192.168.0.144");
        float* cor = OnLoop(buf); // everything eles
        OnRender(cor[1], -cor[0], cor[2]); // printing to screen
    }
    OnCleanup(); // killing app

    return 0;
}

int main(int argc, char* argv[]) {
    CApp theApp;
    return theApp.OnExecute();
}
