import socket
import sys

#create a socket object
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# enter the IP address and the Port of the server
dest_ip = 'server'
dest_port = 8080

# if the user entered the Port of the server
print(len(sys.argv))
if len(sys.argv) == 3:
	dest_ip = sys.argv[1]
	dest_port = int(sys.argv[2])


# connect to the server
s.connect((dest_ip, dest_port))

# start the chat with the server
msg = input()
msg += '\n'
# the newLine is the sign to the server that the message is ended
while True:
	try:
		s.send(msg.encode())
		data = s.recv(4096)
		print (data.decode())
		msg = input()
		msg += '\n'
	# if got a ctrl+c signal, break the loop
	except KeyboardInterrupt:
		break

# close the connection
s.close()