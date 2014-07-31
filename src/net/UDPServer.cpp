
#ifndef RACEMANIA_UDPSERVER_HPP
#define RACEMANIA_UDPSERVER_HPP

#include "UDPServer.hpp"

using boost::asio::ip::udp;
using namespace std;
UDPServer::UDPServer(int port = 5433)
{
	m_socket = new udp::socket(m_io_service, udp::endpoint(udp::v4(), port));
	m_port = port;
}
UDPServer::~UDPServer()
{
	delete m_socket;
	close();
}

void UDPServer::send(char* data, int size, string ip)
{
	udp::endpoint destination(boost::asio::ip::address::from_string(ip), m_port);
	m_socket->send_to(boost::asio::buffer(data, size), destination);
}

void UDPServer::recieve(char* data, int size)
{
	udp::endpoint sender_endpoint;
	m_socket->receive_from(boost::asio::buffer(data, size), sender_endpoint);
}

void UDPServer::close()
{
	m_socket->close();
}

#endif // RACEMANIA_UDPSERVER_HPP
