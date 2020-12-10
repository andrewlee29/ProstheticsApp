#!/usr/bin/python3

import InitDatabase
from multiprocessing import Process
from bluetooth_server import BluetoothServer
from loadsqldata import loadData
from insertdata import InsertData


class DataServer(BluetoothServer):

    def __init__(self):

        BluetoothServer.__init__(self)

    def handleMessage(self, message):

        print(message)
        if message == 'requestSum':
            # Return Summary
            x = loaddata.getCurrentSum()
            print('returning: '+x)
            self.send(x)
        elif message == 'requestRealtime':
            # Return More data about current data
            x = ""
            while(x == ""):
                x = loaddata.testrealtime()
            print('returning: '+x)
            self.send(x)
        elif message == 'requestHis':
            # Return DateList and average temp & humid
            x = loaddata.getHistList()
            print('returning: '+x)
            self.send(x)
        elif 'getHisDate' in message:
            # Return that date message
            text = message.split(':')
            # text[1] = YYYY/MM/DD
            # text[2] = section
            x =loaddata.getHistDetail(text[1],text[2])
            print('returning: '+x)
            self.send(x)
        else:
            self.send('unknown function')

    

if __name__ == '__main__':
    # setup Database
    InitDatabase.setupdatabase()
    loaddata = loadData()
    #insert data
    insertd = InsertData()
    insertd.checktodayexist()
    # start server
    server = DataServer()
    # server.start()
    try:
        p1 = Process(target=server.start)
        p1.start()
        p2 = Process(target=insertd.insertsensordata)
        p2.start()
        p1.join()
        p2.join()
    except:
        print("break")


    
