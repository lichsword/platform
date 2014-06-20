PARM=$1
FILE_NAME=${PARM}".1"

if [ -z "$PARM" ]
then 
    echo "请指定man文件参数(不含.1)"
    echo "eg: $:re_man platform"
    exit
else
groff -Tutf8 -man $FILE_NAME
sudo cp $FILE_NAME /usr/share/man/man1/
man $PARM
fi
