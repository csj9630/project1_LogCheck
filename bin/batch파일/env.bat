rem 주석 : XXX_HOME은 프로그램끼리 경로를 참조하기 위해 설정하는 PATH
rem PATH : 설치된 프로그램을 경로에 상관 없이 쓰기 위해서 설정하는 PATH
rem 이클립스 경로를 추가함.

set DEV_HOME=c:\dev

set JAVA_HOME=%DEV_HOME%\java-17-openjdk-17.0.3.0.6-1
set ECLIPSE_HOME=%DEV_HOME%\eclipse

set PATH=%PATH%;%JAVA_HOME%\bin;%ECLIPSE_HOME%