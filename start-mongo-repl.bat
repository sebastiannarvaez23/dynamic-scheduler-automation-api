@echo off
echo Starting MongoDB replica set...
mongod --dbpath "C:\data\db" --replSet "rs0"
pause