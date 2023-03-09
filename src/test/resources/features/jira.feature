#language: ru

@TEST
Функция: Тестирование jira (iFellow)
  @001
  Сценарий: Перейти в проект TestProject
    Когда Переход к поиску задач
    Когда Выбор списка
    Когда Проверка на количество задач
    Когда Переход к детальному просмотру
  @002
  Сценарий: Проверить данные задачи TestSelenium
    Дано Переход к поиску задач
    Когда Поиск теста по названию "TestSelenium"
    Тогда Проверка данных задачи
  @003
  Структура сценария: Создание задачи с багом
    Когда Создание бага с навигационной панели <theme>, <description>, <environment>
    Дано Проверка созданного бага, по теме, описанию и окружении <theme>, <description>, <environment>
    Примеры:
      | theme                       | description                                        | environment                             |
      | "Наложения текста на текст" | "При генерации пароля происходит наложение текста" | "Супер мега компьютер на celeron -5000" |
  @004
  Сценарий: Изменение и проверка статуса задачи
    Когда Переход к сообщенным багам
    Тогда Проверка статуса бага "СДЕЛАТЬ"
    Когда Выбор статуса В работе
    Тогда Проверка статуса бага "В РАБОТЕ"
    Когда Нажатие кнопки Исполнено
    Тогда Проверка статуса бага "РЕШЕННЫЕ"
    Когда Нажатие кнопок Подтверждено и Выполнено
    Тогда Проверка статуса бага "ГОТОВО"