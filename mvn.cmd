@echo off
set JAVA_HOME=%USERPROFILE%\.gemini\jdk\jdk-17.0.13+11
set PATH=%JAVA_HOME%\bin;%USERPROFILE%\.gemini\maven\apache-maven-3.9.9\bin;%PATH%
call "%USERPROFILE%\.gemini\maven\apache-maven-3.9.9\bin\mvn.cmd" %*
