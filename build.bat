cd webapp
call npm run build
cd ..
echo "webpack build succ"
set source_dir=webapp\dist
set destination_dir=admin\src\main\resources\static
 
xcopy /E /I /C /Y "%source_dir%" "%destination_dir%" 
echo "copy to server resource"

cd admin
call mvn clean package
echo "server build succ"

copy admin\target\gamekeeper-1.0-SNAPSHOT.jar .

pause