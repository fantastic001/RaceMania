#ifndef _CAPP_H_
    #define _CAPP_H_

#include <SDL/SDL.h>
//#include <SDL/SDL_opengl.h>
#include <gl/gl.h>
#include <gl/glu.h>

class CApp {
    private:
        bool Running;
        SDL_Surface* Surf_Display;

    public:
        CApp();
        int OnExecute();

    public:
        bool OnInit();
        void OnEvent(SDL_Event* Event);
        void OnLoop();
        void OnRender(float x, float y, float z);
        void OnCleanup();
};

#endif
