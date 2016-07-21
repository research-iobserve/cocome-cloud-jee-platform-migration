#!/bin/bash

GLASSFISH="$HOME/glassfish4/glassfish"

BASE="$HOME/Projects/iObserve"
SUPPL="cocome-cloud-jee-platform-migration/cocome-maven-project
cocome-cloud-jee-service-adapter"
PASSWORD_FILE="$HOME/Projects/iObserve/passwordfile"

REVISION="1.1"

if [ "$1" == "" ] ; then
	echo "Usage: $0 <deploy|undeploy> [container]"
	exit 1
else
	mode="$1"
fi

if [ "$2" != "" ] ; then
	specific_container="$2"
fi

function undeploy() {
	if isDeployed $1 $2 ; then
		echo "Undeploy from $1: $2"
		$GLASSFISH/bin/asadmin --host "$1" -p 4848 --user admin \
			-W ${PASSWORD_FILE} undeploy "$2-$REVISION"
	else
		echo "Already undeployed from $1: $2"
	fi
}

function deploy() {
	if isDeployed $1 $2 ; then
		echo "Already deployed on $1: $2"
 	else 
		DIRS=""
		for DIR in $SUPPL ; do
			RES=`find $BASE/$DIR -name "$2-$REVISION.[ew]ar"`
			if [ "$RES" != "" ] ; then
				echo "Deploy $2 to $1"
				$GLASSFISH/bin/asadmin --host "$1" -p 4848 --user admin \
					-W ${PASSWORD_FILE} deploy "$RES"
				return
			fi
		done

		echo "Container $2 not found"
	fi
}

function isDeployed() {
	result=`$GLASSFISH/bin/asadmin --host "$1" -p 4848 --user admin -W ${PASSWORD_FILE} list-applications | grep "$2-$REVISION"`
	if [ "$result" == "" ] ; then
		return 1
	else
		return 0
	fi
}

function status() {
	echo -n "Status of $1: "
	if isDeployed $1 $2 ; then
		echo "deployed     $2"
	else
		echo "NOt deployed $2"
	fi
}


IFS=$'\n'

# 192.168.48.224 service-adapter-ear

for I in `cat << EOF
192.168.48.206 cloud-web-frontend
192.168.48.236 store-logic-ear
192.168.48.236 cloud-registry-service
192.168.48.232 enterprise-logic-ear
192.168.48.224 service-adapter-ear
EOF` ; do
	ip=`echo $I | awk '{ print $1 }'`
	container=`echo $I | awk '{ print $2 }'`

	if [ "$specific_container" == "" -o "$specific_container" = "$container" ] ; then

		if [ "$mode" == "deploy" ] ; then
			deploy "$ip" "$container"
		elif [ "$mode" == "undeploy" ] ; then
			undeploy "$ip" "$container"
		elif [ "$mode" == "status" ] ; then
			status "$ip" "$container"
		else
			echo "$ip - $container"
		fi
	fi
done

# end

