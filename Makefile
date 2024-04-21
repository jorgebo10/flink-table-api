PHONY: start

status:
	docker-compose ps
start:
	docker-compose -f ./docker/docker-compose.yaml up

stop:
	docker-compose -f ./docker/docker-compose.yaml  down

clobber:
	docker-compose -f ./docker/docker-compose.yaml down
	$(shell docker volume ls  | docker volume rm --all)
	docker-compose rm
