# Парсинг csv файла

В файле SCVToSQLite.java парсим файл, для этого его пришлось переименовать на английский язык. Создаем 2 таблицы: States и Earthquakes. В поле с местоположением были указаны еще и регионы штата, в котором произошло землятрясение, эти данные отбросили для удобства, так как далее они не нужны. 

В таблице Earthquakes создаем поля: EarthquakeID - Id (ключ), StateID - Id штата, DepthInMeters - глубина в метрах, MagnitudeType - тип магнитуды, Magnitude - магнитуда, Time - время.
В таблице States: StateID - Id (ключ), StateName - название штата.

# Задание 1: Постройте график по среднему количеству землетрясений для каждого года

Визуализация данных о землетресениях по годам, код в файле CSVToSQLite.java:
![image](https://github.com/mewdy-sys/earthquakes1/assets/58120540/d04928c5-117b-43a8-8fbc-e3be7925976a)

# Задание 2: Выведите в консоль среднюю магнитуду для штата "West Virginia"

Результат работы AverageMagnitude.java: Средняя магнитуда в Западной Вирджинии: 2.9133333444595335

![image](https://github.com/mewdy-sys/earthquakes1/assets/58120540/e38c3160-b2fa-45ee-91d4-330738c0226e)

# Задание 3: Выведите в консоль название штата, в котором произошло самое глубокое землетрясение в 2013 году

Результат работы DeeepestEearthquake.java: Штат с самым глубоким землетрясением в 2013: WEST VIRGINIA

![image](https://github.com/mewdy-sys/earthquakes1/assets/58120540/f0ada2bf-5669-497e-a811-a214f0b25182)