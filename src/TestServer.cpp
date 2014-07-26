#include "UDPServer.hpp"

#include <iostream>
#include <cstring>

#include "SDL/SDL.h" 

using namespace std;

typedef SDL_Surface Image; 

SDL_Surface *load_image( string filename ) 
{
	SDL_Surface* loadedImage = NULL;
	SDL_Surface* optimizedImage = NULL;
	loadedImage = SDL_LoadBMP( filename.c_str() );
	optimizedImage = SDL_DisplayFormat( loadedImage );
	SDL_FreeSurface( loadedImage );
	return optimizedImage;
}

void apply_surface( int x, int y, SDL_Surface* source, SDL_Surface* destination) 
{ 
	//Make a temporary rectangle to hold the offsets 
	SDL_Rect offset; //Give the offsets to the rectangle 
	offset.x = x; 
	offset.y = y;
	SDL_BlitSurface( source, NULL, destination, &offset );
}

int main(int argc, char * argv[])
{
	UDPServer s(5000);
	cout <<"TestServer" <<endl;
	
	SDL_Surface* screen = NULL;
	SDL_Init(SDL_INIT_EVERYTHING);

	screen = SDL_SetVideoMode( 640, 480, 32, SDL_SWSURFACE );
	
	Image* img = load_image("a.bmp"); 

	while (true)
	{
		char buf[4];
		s.recieve(buf, 4);
		char ax,ay,az;
		int n = sizeof(int);
		memcpy(&ax, buf, 1);
		memcpy(&ay, buf+1, 1);
		memcpy(&az, buf+2, 1);

		int x = (int) ax; 
		int y = (int) ay; 
		int z = (int) az; 
		
		apply_surface( 300+x*10, 200+y*10, img, screen );

		char indicator = buf[3];
		cout <<(int)ax <<" " <<(int)ay <<" " <<(int)az <<" " <<indicator <<endl;

		// Update screen 
		SDL_Flip( screen );
	}

	SDL_Quit();
	return 0;
}
