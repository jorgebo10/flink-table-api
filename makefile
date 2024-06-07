PHONY: start status check-dependencies stop clobber

# Start all dependencies as local docker containers using docker-compose.
start:
	docker-compose up -d --build

# Show the status of all running docker containers
status:
	docker-compose ps

# Stop all running containers
stop:
	docker-compose stop

list-topics:
	docker exec -it kafka kafka-topics \
               --bootstrap-server localhost:9092 \
               --list

create-my-topic:
	docker exec -it kafka kafka-topics \
		--create \
  		--bootstrap-server kafka:9092 \
 		--topic my-topic \


create-my-output:
	docker exec -it kafka kafka-topics \
		--create \
  		--bootstrap-server kafka:9092 \
 		--topic my-output \

consume-my-topic:
	docker exec -it kafka kafka-console-consumer \
                  --bootstrap-server localhost:9092 \
                  --topic my-topic \


consume-my-output:
	docker exec -it kafka kafka-console-consumer \
                  --bootstrap-server localhost:9092 \
                  --topic my-output \

delete-my-topic:
	docker exec -it kafka kafka-topics \
				  --delete \
                  --bootstrap-server localhost:9092 \
                  --topic my-topic \

# Delete all docker containers.
clobber:
	docker-compose down
	$(shell docker volume ls  | docker volume rm --all)
	docker-compose rm
