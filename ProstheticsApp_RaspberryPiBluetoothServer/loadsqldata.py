import json
import mysql.connector
import random

class loadData:
    def __init__(self):
        self.mydb = mysql.connector.connect(
            host="localhost",
            user="root",
            password="123456",
            database= "prostheticsData",
            autocommit=True
            )
        self.mycursor = self.mydb.cursor()

        # testing
        self.realtime=0.2
        self.currsid =0
        
    def getCurrentSum(self):
        self.mycursor.execute("SELECT * FROM summarydata WHERE cid=(SELECT max(cid) FROM summarydata)")
        data = self.mycursor.fetchall()
        for row in data:
            # to json
            x = {
                "date": row[1],
                "environmentStatus": row[2],
                "muscleStatus" : row[3]
            }
            jsonstring = json.dumps(x)
            # return string
        return jsonstring

    # def getCurrentMore(self):
    #     self.mycursor.execute("SELECT * FROM sensordata WHERE cid=(SELECT max(cid) FROM summarydata) ORDER BY sid DESC LIMIT 10")
    #     data = self.mycursor.fetchall()
    #     if not data:
    #         x = {
    #                 "temp": "",
    #                 "humid": "",
    #                 "emgdata1" : []
    #             }
    #     else:
    #         x = {
    #                 "temp": data[len(data)-1][4],
    #                 "humid": data[len(data)-1][5],
    #                 "emgdata1" : []
    #             }
    #         for row in data:
    #             # to json
    #             add = {
    #                 "time":row[1], 
    #                 "mV":row[2]
    #                 # "section":[6]
    #             }
    #             x['emgdata1'].append(add)
    #     jsonstring = json.dumps(x)
    #             # return string
    #     return jsonstring



    def getHistList(self):
        self.mycursor.execute("SELECT summarydata.date, AVG(sensordata.temperature), AVG(sensordata.humidity) FROM summarydata JOIN sensordata ON summarydata.cid = sensordata.cid GROUP BY summarydata.date")
        data = self.mycursor.fetchall()
        
        # currsum = []
        # message = ""
        x = {
                "historydate": []
            }
        for row in data:
            add = {
                "date":row[0], 
                "temp":round(row[1],2), 
                "humid":round(row[2],2)
            }
            x['historydate'].append(add)
        #     # date
        #     currsum.append(row[0])
        #     # temp
        #     currsum.append(round(row[1],2))
        #     # humid
        #     currsum.append(round(row[2],2))
            
        #  # return string
        # for ele in currsum:
        #     message += str(ele)
        #     message += "#"
        jsonstring = json.dumps(x)
        return jsonstring
        
    def getHistDetail(self,getdate,getsection):
        #get the avg temp & humid & that day cid
        self.mycursor.execute("SELECT summarydata.date, summarydata.cid, AVG(sensordata.temperature), AVG(sensordata.humidity) FROM summarydata JOIN sensordata ON summarydata.cid = sensordata.cid GROUP BY summarydata.date")
        data = self.mycursor.fetchall()
        # currsum = []
        # message = ""
        cid = -1
        for row in data:
            if (str(row[0]) == getdate):
                # # temp
                # currsum.append(round(row[2],2))
                # # humid
                # currsum.append(round(row[3],2))
                
                cid = row[1]
                x = {
                "temp": round(row[2],2),
                "humid": round(row[3],2),
                "emgdata1" : []
            }
        if (cid == -1):
            return "error!! Didn't get the cid"
        
        #get the emg data (sensor1)
        self.mycursor.execute("SELECT time,mV FROM sensordata WHERE cid= " +str(cid)+ " AND section= "+ str(getsection))
        data = self.mycursor.fetchall()
        for row in data:
            # # time
            # currsum.append(row[0])
            # # mV
            # currsum.append(row[1])
            add = {
                "time":row[0], 
                "mV":row[1]
            }
            x['emgdata1'].append(add)
        # return string
        # for ele in currsum:
        #     message += str(ele)
        #     message += "#"
        
        # load all possible section to the data
        self.mycursor.execute("SELECT DISTINCT section FROM sensordata WHERE cid= " +str(cid))
        data = self.mycursor.fetchall()
        temp = []
        for row in data:
            temp.append(row[0])
        y = {"section":temp}
        x.update(y)

        jsonstring = json.dumps(x)
        return jsonstring

    def testrealtime(self):
        msg = str(self.realtime)+"#"
        self.mycursor.execute("SELECT mV, humidity,temperature,sid FROM sensordata ORDER BY sid DESC LIMIT 1 ")
        # self.mycursor.execute("SELECT mV, humidity,temperature FROM sensordata WHERE sid= (SELECT max(sid) FROM sensordata) ")
        data = self.mycursor.fetchall()
        temp = []
        for row in data:
            if (self.currsid ==row[3]):
                return ""
            else:
                temp.append(row[0])
                temp.append(row[1])
                temp.append(row[2])
                currsid=(row[3])
        for ele in temp:
            msg += str(ele)
            msg += "#"
        
        

        # mV = random.randint(1,5)
        # msg = str(self.realtime)+"#"+str(mV)
        self.realtime= self.realtime+0.2
        return msg
    
# open database 
# loaddata = loadData()
# x = loaddata.testrealtime()
# print(x)
