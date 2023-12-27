import os
import time
import datetime
import glob
import pymysql
from time import strftime
import RPi.GPIO as GPIO
from time import sleep
 
GPIO.setwarnings(False)


os.system('modprobe w1-gpio')
os.system('modprobe w1-therm')
# temp_sensor = '/sys/bus/w1/devices/28-00000622fd44/w1_slave'
 
# Variables for MySQL
db = pymysql.connect(host="localhost", user="pi",passwd="password", db="mydb")
cur = db.cursor()


GPIO.setmode(GPIO.BCM)
 
#Set buzzer - pin 23 as output
buzzer=23


#set GPIO Pins
GPIO_TRIGGER = 18
GPIO_ECHO = 24
 
#set GPIO direction (IN / OUT)
GPIO.setup(GPIO_TRIGGER, GPIO.OUT)
GPIO.setup(GPIO_ECHO, GPIO.IN)
GPIO.setup(buzzer,GPIO.OUT)


 
def distance():
    # set Trigger to HIGH
    GPIO.output(GPIO_TRIGGER, True)
 
    # set Trigger after 0.01ms to LOW
    time.sleep(0.00001)
    GPIO.output(GPIO_TRIGGER, False)
 
    StartTime = time.time()
    StopTime = time.time()
 
    # save StartTime
    while GPIO.input(GPIO_ECHO) == 0:
        StartTime = time.time()
 
    # save time of arrival
    while GPIO.input(GPIO_ECHO) == 1:
        StopTime = time.time()
 
    # time difference between start and arrival
    TimeElapsed = StopTime - StartTime
    # multiply with the sonic speed (34300 cm/s)
    # and divide by 2, because there and back
    distance = (TimeElapsed * 34300) / 2
    return distance


if __name__ == '__main__':
    try:
        while True:
            dist = distance()
            print ("Measured Distance = %.1f cm" % dist)
            if dist>20:
                GPIO.output(buzzer,GPIO.HIGH)
                print ("Beep")
                sleep(0.3) # Delay in seconds
                GPIO.output(buzzer,GPIO.LOW)
                print ("No Beep")
                sleep(0.3)
            if dist>10:
                GPIO.output(buzzer,GPIO.HIGH)
                print ("Beep")
                sleep(0.5) # Delay in seconds
                GPIO.output(buzzer,GPIO.LOW)
                print ("No Beep")
                sleep(0.5)
            if dist>5:
                GPIO.output(buzzer,GPIO.HIGH)
                print ("Beep")
                sleep(0.8) # Delay in seconds
                GPIO.output(buzzer,GPIO.LOW)
                print ("No Beep")
                sleep(0.8)
            time.sleep(1)
            datetimeWrite = (time.strftime("%Y-%m-%d ") + time.strftime("%H:%M:%S"))
            print (datetimeWrite)
            sql = ("""INSERT INTO data (time,value) VALUES (%s,%s)""",(datetimeWrite,dist))
            try:
                print ("Writing to database...")
                # Execute the SQL command
                cur.execute(*sql)
                # Commit your changes in the database
                db.commit()
                print ("Write Complete")
         
            except:
                # Rollback in case there is any error
                db.rollback()
                print ("Failed writing to database")
         
            cur.close()
            db.close()
            break
 
        # Reset by pressing CTRL + C
    except KeyboardInterrupt:
        print("Measurement stopped by User")
        GPIO.cleanup()
