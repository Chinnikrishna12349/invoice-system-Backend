$env:JAVA_HOME = "$env:USERPROFILE\.gemini\jdk\jdk-17.0.13+11"
$env:PATH = "$env:JAVA_HOME\bin;$env:USERPROFILE\.gemini\maven\apache-maven-3.9.9\bin;$env:PATH"
& "$env:USERPROFILE\.gemini\maven\apache-maven-3.9.9\bin\mvn.cmd" @args
