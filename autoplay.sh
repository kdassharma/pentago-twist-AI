count=0
for ((i=1; i<=$1;i++))
do
	win=`java -cp bin autoplay.Autoplay 1 | grep GAMEOVER | grep -c 1`
    if [ $win = 1 ]
	then
		((count++))
	fi	
	statement="${count}/${i}"
	echo "${statement}"
done
echo $count