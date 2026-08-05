' RuoYi hidden launcher
' Usage: wscript ruoyi-start.vbs <redis|backend|frontend>
Option Explicit
Dim shell, arg
Set shell = CreateObject("WScript.Shell")
arg = WScript.Arguments(0)

Select Case arg
  Case "redis"
    shell.Run "cmd.exe /c """"C:\Users\29352\dev\redis\redis-server.exe"" ""C:\Users\29352\dev\redis\redis.windows.conf"" > ""C:\Users\29352\dev\ruoyi-redis.log"" 2>&1""", 0, False
  Case "backend"
    shell.Run "cmd.exe /c """"C:\Users\29352\dev\java\jdk-17.0.20+8\bin\java.exe"" -jar ""C:\Users\29352\dev\RuoYi-Vue\ruoyi-admin\target\ruoyi-admin.jar"" --ruoyi.profile=C:/Users/29352/dev/ruoyi-upload > ""C:\Users\29352\dev\ruoyi-backend.log"" 2>&1""", 0, False
  Case "frontend"
    shell.Run "cmd.exe /c cd /d ""C:\Users\29352\dev\nginx-1.26.3"" && start nginx.exe", 0, False
End Select

Set shell = Nothing
