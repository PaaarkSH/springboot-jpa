docker run --name my-postgres-container -e POSTGRES_USER=psh950 -e POSTGRES_PASSWORD=02070325 -e POSTGRES_DB=mydatabase -p 5432:5432 -d postgres:16

# bash 로 접근할때
# docker exec -it my-postgres-container bash
# 이후에 conf 파일 수정해서 방화벽 뚫어줘야함