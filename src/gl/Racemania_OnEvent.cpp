#include "../Racemania.hpp"

void CApp::OnEvent(SDL_Event* Event) {
    if(Event->type == SDL_QUIT) { // handled on red X press
        Running = false;
    }
}
