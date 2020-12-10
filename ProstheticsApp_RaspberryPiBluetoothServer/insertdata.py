import sched, time
import mysql.connector
import random
import Adafruit_ADS1x15
import Adafruit_DHT
from datetime import date

class InsertData:
    def __init__(self):
        self.mydb = mysql.connector.connect(
            host="localhost",
            user="root",
            password="123456",
            database= "prostheticsData"
            )
        self.mycursor = self.mydb.cursor()
        self.todaycid = 0
        self.counter = 0
    
    def checktodayexist(self):
        ##check today is exist in database
        today = date.today()
        d1 = today.strftime("%Y/%m/%d")
        self.mycursor.execute("SELECT cid,date FROM summarydata WHERE date= '" +d1+ "'")
        data = self.mycursor.fetchall()
        if not data:
            print("today not exist, generate new table for today")
            ## if not exist, create one in summary
            self.mycursor.execute("INSERT INTO summarydata (date, environmentStatus, muscleStatus) VALUES (%s,%s,%s)", (d1, "good", "good"))
            ## automatic generate id 
            cid = self.mycursor.lastrowid
            self.todaycid = cid
            self.mydb.commit()
            
        else:
            self.todaycid = data[0][0]
            print ("todaycid = ",self.todaycid)

    def insertsensordata(self):
        self.mycursor.execute("SELECT max(section) FROM sensordata WHERE  cid="+ str(self.todaycid) + "")
        data = self.mycursor.fetchall()
        for row in data:
             temp = row[0]
        if not temp:
            newsection=1
            print("section = 1")
        else:
            newsection = int(temp)+1
            print("section"+str(newsection))

        while(self.counter >-1):
            print("Insert data...")
            ## test data : random int generte
            # if there is 30 data in 1 section, start new section
            if (self.counter ==30):
                newsection = newsection +1
                self.counter = 0
            self.counter = self.counter+1
            #get emg data 
            adc = Adafruit_ADS1x15.ADS1115()
            GAIN = 1
            emgmV = adc.read_adc(3, gain=GAIN)
            #get temp data
            DHT_SENSOR = Adafruit_DHT.DHT11
            DHT_PIN = 4
            humidity, temperature = Adafruit_DHT.read(DHT_SENSOR, DHT_PIN)
            if humidity is not None and temperature is not None:
                # print("Temp={0:0.1f}C Humidity={1:0.1f}%".format(temperature, humidity))
                temphumid = []
                temphumid.append(humidity)
                temphumid.append(temperature)
            else:
                temphumid = []
                temphumid.append(0)
                temphumid.append(0)
            # load data to database 
            a = str(self.counter)
            b = str(emgmV)
            c = str(1)
            d = str(temphumid[0])
            e = str(temphumid[1])
            f = str(newsection)
            self.currmV = b
            self.mycursor.execute("INSERT INTO sensordata (time, mV, emgsensor, temperature, humidity, section, cid) VALUES (%s,%s,%s,%s,%s,%s,%s)", (a, b, c, d, e, f,self.todaycid))
            ## automatic generate id 
            sid = self.mycursor.lastrowid
            ## need commit to apply insert 
            self.mydb.commit()
            # Pretend to work for a second
            time.sleep(0.2)
# get sensor data :
    # def tempRead(self):
    #     DHT_SENSOR = Adafruit_DHT.DHT11
    #     DHT_PIN = 4
    #     humidity, temperature = Adafruit_DHT.read(DHT_SENSOR, DHT_PIN)
    #     if humidity is not None and temperature is not None:
    #     # print("Temp={0:0.1f}C Humidity={1:0.1f}%".format(temperature, humidity))
    #         temp = []
    #         temp.append(humidity)
    #         temp.append(temperature)
    #         return temp
    #     else:
    #         print("Sensor failure. Check wiring.")
    # def EMGread(self):
    #     adc = Adafruit_ADS1x15.ADS1115()
    #     GAIN = 1
    #     # Read the specified ADC channel using the previously set gain value.
    #     emgmV = adc.read_adc(3, gain=GAIN)
    #     return emgmV
# insertd = InsertData()
# temphumid = insertd.tempRead()
# emg = insertd.EMGread()
# print(str(temphumid[0])+" //  "+ str(temphumid[1]))
# print(str(emg))
# insertd.checktodayexist()
# insertd.insertsensordata()
