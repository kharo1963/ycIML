Перевел интерпретатор модельного языка программирования (https://teach-in.ru/course/programming-systems/about) с С++ на Java Spring Boot (IntelliJ IDEA ). Добавил обработку инструкции присваивания с семантикой похожей на семантику языка C. В результате доработки верно интрерпретируюстя инструкции вида x := y := 1 ;. Добавил функцию выполнения преобразованного запроса - функция get для получение значений из таблицы iml_param (PostgreSQL).   Пример использования в программе ext-gcd.txt: get (m,ext-gcd/m) присваивает переменной m значение param_val из таблицы iml_param, где prog_name = ext-gcd и param_name = m. Добавил в проект open source JCodec (удалив почти все не относящееся к созданию видео в формате mp4).  
Разместил интерпретатор на хостинге Yandex Cloud в качестве сервиса Serverless Containers. Для запуска сервиса нужно перейти по адресу https://bbaqndbutut7tp2bjrh3.containers.yandexcloud.net/.   
Ввести исходный текст программы в окне редактирования или выбрать файл на локальный компьютере для интерпретации или загрузить образец программы и интерпретировать. Примеры программных файлов m-ext-gcd.txt, m-assignment.txt, m-spin-cube.txt вложены в git.Сервис разберет исходный текст программы, переведет в польскую инверсную запись и выполнит программу. Результат выведет на экран.  
Вложенные образцы программ:  
m-assignment.txt - пример с оператором x := y := 1;  
m-ext-gcd.txt - расширенный алгоритм Евклида;  
m-spin-cube.txt - создание видеофайла в формате mp4.  

