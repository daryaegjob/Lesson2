# Task Manager (занятие 4: сериализация и файлы)

## Сборка и запуск

```bash
mvn compile
mvn exec:java -Dexec.args="sample-data/tasks.json"
```

или собрать jar и запускать напрямую:

```bash
mvn package
java -jar target/task-manager.jar sample-data/tasks.json
```

## Путь до файла с задачами

Можно передать двумя способами (аргумент командной строки приоритетнее):

```bash
java -jar target/task-manager.jar sample-data/tasks.csv
# или
TASKS_FILE=sample-data/tasks.csv java -jar target/task-manager.jar
```

Также можно загрузить файл прямо во время сессии командой:

```
> import sample-data/tasks.json
```

## Консольные команды

```
add <название>|<описание>|<статус>
remove <id>
find <название>
update <id>|<название>|<описание>|<статус>
import <путь до .json или .csv>
list
help
exit
```
