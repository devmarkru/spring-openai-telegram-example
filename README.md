# spring-openai-telegram-example
Пример telegram-бота для взаимодействия с **OpenAI**.

Для запуска приложения требуется определить следующие переменные окружения:
- `OPEN_AI_API_KEY` - ключ для выполнения запросов к Open AI
- `OPEN_AI_BASE_URL` (опционально) - позволяет переопределить эндпоинт LLM, к которому происходит подключение (при использовании другой LLM с совместимым протоколом)
- `TELEGRAM_BOT_TOKEN` - токен для взаимодействия с telegram-ботом
- `TELEGRAM_BOT_NAME` - имя telegram-бота

Данный проект содержит `Dockerfile`, поэтому его можно легко развернуть в облачном хостинге.

### Полезные ссылки
* [Spring AI: пишем telegram-bot для ChatGPT](https://devmark.ru/article/spring-openai-telegram).
* [dockhost.ru](https://dockhost.ru/?utm_source=devmark&utm_medium=cpa&utm_campaign=devmark&p=z8i9gexg) - облачный хостинг по технологии `Push-to-Deploy`.
* [Новости проекта](https://t.me/+RjrPWNUEwf8wZTMy) и короткие заметки.
* Ещё больше статей по разработке ПО вы можете найти на моём сайте [devmark.ru](https://devmark.ru/).
