#!/bin/bash
sudo service tomcat stop && ps aux | grep [t]omcat | awk '{print $2}' | xargs -I PID sudo kill -9 PID
