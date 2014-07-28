#include <stdio.h>
#include "CApp.h"

CApp::CApp() {
    Surf_Display = NULL;
    Running = true;
}

int CApp::OnExecute() {
    if(OnInit() == false) return -1; // oenGL and screen init
    SDL_Event Event;

    while(Running) {
        while(SDL_PollEvent(&Event)) {
            OnEvent(&Event); // handling events, chande to receive from UDP
        }
        OnLoop(); // everything eles
        OnRender(); // printing to screen
    }
    OnCleanup(); // killing app

    return 0;
}

int main(int argc, char* argv[]) {
    CApp theApp;
    return theApp.OnExecute();
}
