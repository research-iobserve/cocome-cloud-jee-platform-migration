#!/bin/bash

export HOSTS="192.168.48.206 192.168.48.232 192.168.48.236 192.168.48.224"

BINDIR=$(cd "$(dirname "$0")"; pwd)/

function remoteGlassfish () {
	ssh glassfish@$1 /opt/glassfish-4.0/glassfish/bin/asadmin $2 $3
}

function localGlassfish () {
	$HOME/glassfish4/glassfish/bin/asadmin -u admin -W $HOME/Projects/iObserve/passwordfile -H $1 $2
}

function service () {
	if [ "$3" == "" ] ; then
		for I in $HOSTS ; do
			remoteGlassfish $I $1 $2
		done
	else
		if [ `echo $HOSTS | grep "$3"` != "" ] ; then
			remoteGlassfish $3 $1 $2
		fi
	fi
}

function localService () {
	if [ "$2" == "" ] ; then
		for I in $HOSTS ; do
			localGlassfish $I $1
		done
	else
		if [ `echo $HOSTS | grep "$2"` != "" ] ; then
			localGlassfish $2 $1
		fi
	fi
}



if [ "$1" == "start" ] ; then
	service start-domain cocome $2
elif [ "$1" == "stop" ] ; then
	service stop-domain cocome $2
elif [ "$1" == "restart" ] ; then
	service stop-domain $2
	service start-domain cocome $2
elif [ "$1" == "status" ] ; then
	localService list-applications $2
elif [ "$1" == "init" ] ; then
	service stop-domain
	rm -rf ${BINDIR}/../../data/web/kieker-*
	rm -rf ${BINDIR}/../../data/logic/kieker-*
	rm -rf ${BINDIR}/../../data/adapter/kieker-*
	service start-domain cocome
else
	echo "Usage: $0 <start|stop|restart|status|init>"
fi
