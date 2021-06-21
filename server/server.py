import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import paho.mqtt.client as mqtt
import urllib.request
from http.server import BaseHTTPRequestHandler, HTTPServer
import threading
from threading import Thread
import time
import json
import urllib.parse
import requests
from datetime import datetime


cred = credentials.Certificate('firebaseSDK.json')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://temperatureandhumidity-14a04-default-rtdb.firebaseio.com/'
})

ref = db.reference('/')
#setting database for testing - remember to comment this when using the real database
'''ref.set({
    'Buildings':{
        'H1':{
            '101':{
                'deviceModel':{
                    'RELAY':{
                        '1':{
                            'building':'H1',
                            'data': '0',
                            'id':'1',
                            'name':'RELAY',
                            'offThreshold':'25',
                            'onThreshold':'30',
                            'room':'101',
                            'unit':''
                        },
                        '2':{
                            'building':'H1',
                            'data': '0',
                            'id':'2',
                            'name':'RELAY',
                            'offThreshold':'25',
                            'onThreshold':'30',
                            'room':'101',
                            'unit':''
                        }
                    },
                    'TEMP-HUMID':{
                        '1':{
                            'building':'H1',
                            'data': '40-30',
                            'id':'1',
                            'name':'TEMP-HUMID',
                            'offThreshold':'',
                            'onThreshold':'',
                            'room':'101',
                            'unit':'C-%'
                        }
                    }
                }
            }
        }
    },
    'Devices':{
        'RELAY':{
            '1':{
                'building':'H1',
                'data': '0',
                'id':'1',
                'name':'RELAY',
                'offThreshold':'25',
                'onThreshold':'30',
                'room':'101',
                'unit':''
            },

            '2':{
                'building':'H1',
                'data': '0',
                'id':'2',
                'name':'RELAY',
                'offThreshold':'25',
                'onThreshold':'30',
                'room':'101',
                'unit':''
            }
        },
        'TEMP-HUMID':{
            '1':{
                'building':'H1',
                'data': '40-30',
                'id':'1',
                'name':'TEMP-HUMID',
                'offThreshold':'',
                'onThreshold':'',
                'room':'101',
                'unit':'C-%'
            }
        }
    }
})'''
#done setting up the database
path_relay = "dadn/feeds/bk-iot-relay" 
path_temp_humid = "dadn/feeds/bk-iot-temp-humid"

hostName = "192.168.1.101"
serverPort = 8080

client = mqtt.Client()

def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))

    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.

# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    now = datetime.now().strftime('%d/%m/%Y %H:%M:%S')
    data = msg.payload.decode("utf-8")
    message = json.loads(data)
    if (msg.topic == path_relay):
        ref = db.reference('/Devices/RELAY')
        child = ref.child(message['id'])
        #update RELAY id in Devices
        if (child.get()):
            child.update({
                'data': message['data'],
                'id':message['id'],
                'name':message['name'],
                'unit':message['unit']    
            })
        else:
            ref.update({
                message['id']:{
                    'building':'',
                    'data': message['data'],
                    'id': message['id'],
                    'name': message['name'],
                    'offThreshold':'',
                    'onThreshold':'',
                    'room':'',
                    'unit':message['unit']
                }
            })
        #update RELAY id in Buildings
        relay = child.get()
        if (relay['building'] != '' and relay['room'] != ''):
            path = db.reference('/Buildings/%s/%s/deviceModel/RELAY/%s' % (relay['building'],relay['room'],relay['id']))
            path.update({
                'building':relay['building'],
                'data': relay['data'],
                'id': relay['id'],
                'name': relay['name'],
                'offThreshold': relay['offThreshold'],
                'onThreshold': relay['onThreshold'],
                'room': relay['room'],
                'unit': relay['unit']
            })
    elif(msg.topic == path_temp_humid):
        ref = db.reference('/Devices/TEMP-HUMID')
        child = ref.child(message['id'])
        #update TEMP-HUMID in Devices
        if (child.get()):
            child.update({
                'data': message['data'],
                'id':message['id'],
                'name':message['name'],
                'unit':message['unit']    
            })
        else:
            ref.update({
                message['id']:{
                    'building':'',
                    'data': message['data'],
                    'id': message['id'],
                    'name': message['name'],
                    'offThreshold':'',
                    'onThreshold':'',
                    'room':'',
                    'unit':message['unit']
                }
            })
        #update TEMP-HUMID in Buildings
        temp_humid = child.get()
        if (temp_humid['building'] != '' and temp_humid['room'] != ''):
            path = db.reference('/Buildings/%s/%s/deviceModel/TEMP-HUMID/%s' % (temp_humid['building'],temp_humid['room'],temp_humid['id']))
            path.update({
                'building': temp_humid['building'],
                'data': temp_humid['data'],
                'id': temp_humid['id'],
                'name': temp_humid['name'],
                'offThreshold': temp_humid['offThreshold'],
                'onThreshold': temp_humid['onThreshold'],
                'room': temp_humid['room'],
                'unit': temp_humid['unit']
            })
        
        building = child.get()['building']
        room = child.get()['room']
        data = child.get()['data']
        data_unit = child.get()['unit']
        #print(data)
        if (building == '' or room == '' or data == ''):
            pass
        else:
            split = data.split('-')
            temp = split[0]
            humid = split[1]
            #auto part of RELAY in Devices
            relay = db.reference('/Devices/RELAY')
            for x in relay.get():
                if (x):
                    if (x['building'] == building and x['room'] == room):
                        if (x['offThreshold'] != '' and int(x['offThreshold']) >= int(temp) and x['data'] == '1'):
                            send_message = '{ "id":"%s", "name":"%s", "data":"%s", "unit":"%s" }' % (x['id'],x['name'],'0',x['unit'])
                            client.publish(path_relay,send_message)
                            history = db.reference('/History')
                            history.push({
                                'id': x['id'],
                                'name': x['name'],
                                'data': '0',
                                'unit': x['unit'],
                                'building': x['building'],
                                'room': x['room'],
                                'user': '',
                                'time': now,
                                'mode': 'auto',
                                'temp_humid': data,
                                'temp_humid_unit': data_unit
                            })
                        elif (x['onThreshold'] != '' and int(x['onThreshold']) < int(temp) and x['data'] == '0'):
                            send_message = '{ "id":"%s", "name":"%s", "data":"%s", "unit":"%s" }' % (x['id'],x['name'],'1',x['unit'])
                            client.publish(path_relay,send_message)
                            history = db.reference('/History')
                            history.push({
                                'id': x['id'],
                                'name': x['name'],
                                'data': '1',
                                'unit': x['unit'],
                                'building': x['building'],
                                'room': x['room'],
                                'user': '',
                                'time': now,
                                'mode': 'auto',
                                'temp_humid': data,
                                'temp_humid_unit': data_unit
                            })
                else:
                    pass
            #auto part of RELAY in Buildings
            relay_b = db.reference('/Buildings/%s/%s/deviceModel/RELAY' % (building,room))
            for x in relay_b.get():
                if(x):
                    if (x['building'] == building and x['room'] == room):
                        if (x['offThreshold'] != '' and int(x['offThreshold']) >= int(temp) and x['data'] == '1'):
                            send_message = '{ "id":"%s", "name":"%s", "data":"%s", "unit":"%s" }' % (x['id'],x['name'],'0',x['unit'])
                            client.publish(path_relay,send_message)
                        elif (x['onThreshold'] != '' and int(x['onThreshold']) < int(temp) and x['data'] == '0'):
                            send_message = '{ "id":"%s", "name":"%s", "data":"%s", "unit":"%s" }' % (x['id'],x['name'],'1',x['unit'])
                            client.publish(path_relay,send_message)
                else:
                    pass
        
        
    
    #print(message)
    #print(data)
    #message = json.loads(data)
    #print(msg.topic+" "+str(msg.payload))



# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.


def MQTT():
    client.on_connect = on_connect
    client.on_message = on_message
    client.username_pw_set("dadn","aio_CiZQ86qgiNxOgledRBr5iPI7KKrW")
    client.connect("io.adafruit.com", 1883, 60)
    client.subscribe(path_relay)
    client.subscribe(path_temp_humid)
    client.loop_forever()
    


class MyServer(BaseHTTPRequestHandler):
    def do_GET(self):
        #now = datetime.today().strftime('%Y/%m/%d-%H:%M:%S')
        now = datetime.now().strftime('%d/%m/%Y %H:%M:%S')
        self.send_response(200)
        self.send_header("Content-type", "text/html")
        self.end_headers()
        content = self.path[1:]
        print('ahi' + content)
        message = urllib.parse.unquote(content)
        if (message == 'favicon.ico' or message == ''):
            pass
        else:
            try:
                receive = json.loads(message)
                send_message = '{ "id":"%s", "name":"%s", "data":"%s", "unit":"%s" }' % (receive['id'],receive['name'],receive['data'],receive['unit'])
                client.publish(path_relay,send_message)
                temp_humid = db.reference('/Devices/TEMP-HUMID')
                data = ''
                data_unit = ''
                for x in temp_humid.get():
                    if (x):
                        if (x['building'] == receive['building'] and x['room'] == receive['room']):
                            data = x['data']
                            data_unit = x['unit']
                            break
                ref = db.reference('/History')
                ref.push({
                    'id': receive['id'],
                    'name': receive['name'],
                    'data': receive['data'],
                    'unit': receive['unit'],
                    'building': receive['building'],
                    'room': receive['room'],
                    'user': receive['user'],
                    'time': now,
                    'mode': 'manual',
                    'temp_humid': data,
                    'temp_humid_unit': data_unit
                })
            except:
                print('its not json bro')
        #print(message)
        #new = json.loads(message)
        #message = urllib.parse.unquote(content)
        #print(message)
        #self.wfile.write(bytes("<html><head><title>GloryOrNot</title></head>", "utf-8"))
        # self.wfile.write(bytes("<p>Request: %s</p>" % self.path, "utf-8"))
        #self.wfile.write(bytes("<body>", "utf-8"))
        #self.wfile.write(bytes("<p>Glorious Success</p>", "utf-8"))
        #self.wfile.write(bytes("</body></html>", "utf-8"))

    

if __name__ == "__main__":        
    webServer = HTTPServer((hostName, serverPort), MyServer)
    print("Server started http://%s:%s" % (hostName, serverPort))

    try:
        thread = Thread(target=MQTT)
        thread.start()
        webServer.serve_forever()
    except KeyboardInterrupt:
        client.disconnect()
        webServer.server_close()

    
    print("Server stopped.")
