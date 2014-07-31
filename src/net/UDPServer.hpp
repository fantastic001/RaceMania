#include <boost/array.hpp>
#include <boost/asio.hpp>

#include <string>

using boost::asio::ip::udp;

class UDPServer
{
	boost::asio::io_service m_io_service;
	udp::socket *m_socket;
	int m_port;
public:

	UDPServer(); // Default constructor
	UDPServer(int); // Constructor with specified special port
	~UDPServer();; // destructor

	void send(char* , int, std::string); // send n bytes

	void recieve(char* , int); // recieve data

	void close(); // close connection
};
