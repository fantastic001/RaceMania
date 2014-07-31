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
	
	UDPServer c(5001);

	SDL_Surface* screen = NULL;
	SDL_Init(SDL_INIT_EVERYTHING);

	screen = SDL_SetVideoMode( 640, 480, 32, SDL_SWSURFACE );
	
	Image* img = load_image("a.bmp"); 

	while (true)
	{
		char buf[4];
		s.recieve(buf, 4);
		char ax,ay,az;
		memcpy(&ax, buf, 1);
		memcpy(&ay, buf+1, 1);
		memcpy(&az, buf+2, 1);
		
		char tosend[3];
		tosend[0] = ax; 
		tosend[1] = ay; 
		tosend[2] = az; 
		c.send(tosend, 3, "192.168.1.102");

		int x = (int) ax; 
		int y = (int) ay; 
		int z = (int) az;
		x /= 8; 
		y /= 8; 
		z /= 8; 

		SDL_FillRect(screen, &screen->clip_rect, SDL_MapRGB(screen->format,0x00, 0x00, 0x00));
		apply_surface( 240-x*20, 120+y*20, img, screen );

		char indicator = buf[3];
		cout <<(int)ax <<" " <<(int)ay <<" " <<(int)az <<" " <<indicator <<endl;

		// Update screen 
		SDL_Flip( screen );
	}

	SDL_Quit();
	return 0;
}
