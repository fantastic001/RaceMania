#include "../Racemania.hpp"

float* CApp::OnLoop(char* buf) {
    char a[3];
    memcpy(&a[0], buf, 1);
    memcpy(&a[1], buf+1, 1);
    memcpy(&a[2], buf+2, 1);
    float b[3];
    b[0] = (int) a[0];
    b[1] = (int) a[1];
    b[2] = (int) a[2];
    b[0] /= 120; if (b[0]>0.6) b[0] = 0.6; if (b[0]<-0.6) b[0] = -0.6;
    b[1] /= 120; if (b[1]>0.6) b[1] = 0.6; if (b[1]<-0.6) b[1] = -0.6;
    b[2] /= 120; if (b[2]>0.6) b[2] = 0.6; if (b[2]<-0.6) b[2] = -0.6;
    return b;
}
