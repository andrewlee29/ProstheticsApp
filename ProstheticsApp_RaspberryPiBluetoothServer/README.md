# ProstheticsApp_RaspberryPiBluetoothServer


## File Describtion

### bluetooth_server.py :

Build the bluetooth server and start accepting request from other devices. It also handle the write message and read message.

### InitDatabase.py :
Initiate the database and table if the database or table is missing.

### Loaddatabase.py :
Use Query to read data from database and return data. 

### insertdata.py :
Read the sensors' data and use Query to insert data to the database.

### lowhigh_server.py :

the main file of the program. It will run the server and make different actions when the server receives a message from the slave. It also automatically inserts sensor data into the MySQL database.




## Setup on Raspberry pi (Ubuntu and other Debian):
1. Install the BlueZ Linux Bluetooth library and pybluez
```
sudo apt-get install bluetooth libbluetooth-dev
sudo python3 -m pip install pybluez
```

2. In Raspberry pi file, edit /lib/systemd/system/bluetooth.service and add -C after bluetoothd.

3. Run the bluetooth server
```
sudo apt-get python3 lowhigh_server.py
```

## Use:
To run the server:
```
sudo python3 lowhigh_server.py
```
