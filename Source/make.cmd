"c:\Program Files\java\jdk1.6.0_31\bin\javac.exe" -classpath "c:\program files\java\jdk1.6.0_31\jre\lib\plugin.jar" "JavaSocketBridge.java" "Listener.java"
"c:\Program Files\java\jdk1.6.0_31\bin\jar.exe" cvf JavaSocketBridge.jar "JavaSocketBridge.class" "Listener.class"
"c:\Program Files\java\jdk1.6.0_31\bin\jarsigner.exe" JavaSocketBridge.jar myNewKey