#!/bin/sh
javac -classpath "c:\program files\java\jdk1.6.0_31\jre\lib\plugin.jar" "JavaSocketBridge.java" "Listener.java"
jar cvf JavaSocketBridge.jar "JavaSocketBridge.class" "Listener.class"
jarsigner JavaSocketBridge.jar myNewKey
mv JavaSocketBridge.jar ..