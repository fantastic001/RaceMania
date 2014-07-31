#include "../Racemania.hpp"

void CApp::OnRender(float x, float y, float z) { // rendering screen
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glLoadIdentity();
    glTranslatef(x, y, 0.0f);

    glBegin(GL_TRIANGLES);
        glColor3f(1.0f, 0.0f, 0.0f);
        glVertex3f(0.0f, 0.5f, 0.0f);
        glVertex3f(0.5f, -0.5f, 0.0f);
        glVertex3f(-0.5f, -0.5f, 0.0f);
    glEnd();
    SDL_GL_SwapBuffers();
}
