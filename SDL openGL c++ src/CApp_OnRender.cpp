#include "CApp.h"

void CApp::OnRender(float x, float y, float z) { // rendering screen
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glLoadIdentity();

    glBegin(GL_TRIANGLES);
        glColor3f(1.0f, 0.0f, 0.0f);
        glVertex3f(x, y+0.4f, z);
        glVertex3f(x+0.4f, y-0.4f, z);
        glVertex3f(x-0.4f, y-0.4f, z);
    glEnd();
    SDL_GL_SwapBuffers();
}
