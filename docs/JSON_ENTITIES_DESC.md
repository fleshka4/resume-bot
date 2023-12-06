<H3>Таблица полей при добавлении резюме</H3>
<H4>Таблица JSON полей Client, предоставляемых методом добавления резюме со стороны пользователя hh.ru.</H4>


| Name                    | Type                     | Java Analog           | Java Type             | Description                                                                                                                                                                            |
|-------------------------|--------------------------|-----------------------|-----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| access                  | object or null           | access                | Type                  | Тип видимости.<br/>Определяет, кому будет доступно резюме в поиске и по прямой ссылке.                                                                                                 |
| birth_date              | string or null           | birthDate             | String                | День рождения (в формате ГГГГ-ММ-ДД)                                                                                                                                                   |
| business_trip_readiness | object or null           | businessTripReadiness | Id                    | Готовность к командировкам. Элемент справочника business_trip_readiness                                                                                                                |
| certificate             | Array of objects or null | certificate           | List\<Certificate>    | Список сертификатов соискателя                                                                                                                                                         |
| driver_license_types    | Array of objects or null | driverLicenseTypes    | List\<Id>             | Список категорий водительских прав соискателя                                                                                                                                          |
| employments             | Array of objects or null | employments           | List\<Type>           | Список подходящих соискателю типов занятостей. Элементы справочника employment                                                                                                         |
| first_name              | string or null           | firstName             | String                | Имя                                                                                                                                                                                    |
| has_vehicle             | boolean or null          | hasVehicle            | Boolean               | Наличие личного автомобиля у соискателя                                                                                                                                                |
| hidden_fields           | Array of objects or null | hiddenFields          | List\<Type>           | Список скрытых полей.<br/>Возможные значения элементов приведены в поле resume_hidden_fields справочника полей                                                                         |
| last_name               | string or null           | lastName              | String                | Фамилия                                                                                                                                                                                |
| metro                   | object or null           | metro                 | Id                    | Ближайшая станция метро. Элемент справочника metro.<br/>Если передать метро, не принадлежащее переданной area, поле проигнорируется.<br/>Имеет смысл указывать только для area с метро |
| middle_name             | string or null           | middleName            | String                | Отчество                                                                                                                                                                               |
| photo                   | object or null           | photo                 | Photo                 | Фотография пользователя                                                                                                                                                                |
| portfolio               | Array of objects or null | portfolio             | List\<Portfolio>      | Список изображений в портфолио пользователя                                                                                                                                            |
| professional_roles      | Array of objects         | professionalRoles     | List\<Id>             | Массив объектов профролей.<br/>Элемент справочника professional_roles                                                                                                                  |
| recommendation          | Array of objects or null | recommendation        | List\<Recommendation> | Список рекомендаций                                                                                                                                                                    |
| relocation              | object or null           | relocation            | Relocation            | Возможность переезда.<br/>Информация о возможности переезда в другой город                                                                                                             |
| resume_locale           | object or null           | resumeLocale          | Type                  | Язык, на котором составлено резюме (локаль).<br/>Элемент справочника локали резюме.                                                                                                    |
| salary                  | object or null           | salary                | Salary                | Зарплата                                                                                                                                                                               |
| schedules               | Array of objects or null | schedules             | List\<Type>           | Список подходящих соискателю графиков работы.<br/>Элементы справочника schedule.                                                                                                       |
| site                    | Array of objects or null | site                  | List\<Site>           | Профили в соц. сетях и других сервисах                                                                                                                                                 |
| skill_set               | Array of strings or null | skillSet              | Set\<String>          | Ключевые навыки (список уникальных строк)                                                                                                                                              |
| skills                  | string or null           | skills                | String                | Дополнительная информация, описание навыков в свободной форме                                                                                                                          |
| title                   | string or null           | title                 | String                | Желаемая должность                                                                                                                                                                     |
| total_experience        | object or null           | totalExperience       | TotalExperience       | Общий опыт работы в месяцах, с округлением до месяца                                                                                                                                   |
| travel_time             | object or null           | travelTime            | Id                    | Желательное время в пути до работы. Элемент справочника travel_time.                                                                                                                   |
| work_ticket             | Array of objects or null | workTicket            | List\<Id>             | Список регионов, в который соискатель имеет разрешение на работу.<br/>Элементы справочника регионов.                                                                                   |
| area                    | object                   | area                  | Id                    | Город проживания. Элемент справочника areas                                                                                                                                            |
| citizenship             | Array of objects         | citizenship           | List\<Id>             | Список гражданств соискателя. Элементы справочника регионов.                                                                                                                           |
| contact                 | Array of objects         | contact               | List\<Contact>        | Список контактов соискателя.                                                                                                                                                           |
| education               | object                   | education             | Education             | Образование соискателя.                                                                                                                                                                |
| experience              | Array of objects         | experience            | List\<Experience>     | Опыт работы                                                                                                                                                                            |
| gender                  | object                   | gender                | Id                    | Пол. Элемент справочника gender                                                                                                                                                        |
| language                | Array of objects         | language              | List\<Language>       | Список языков, которыми владеет соискатель.<br/>Элементы справочника languages.                                                                                                        |

<H2>Особенности заполнения полей</H2>

<H3>Поле contact</H3>
При заполнении контактов в резюме необходимо учитывать следующие условия:
* В резюме обязательно должен быть указан e-mail. Он может быть только один.
* В резюме должен быть указан хотя бы один телефон, причём можно указывать только один телефон каждого типа.
* Комментарий можно указывать только для телефонов, для e-mail комментарий не сохранится.
* Обязательно указать либо телефон полностью в поле formatted, либо все три части 
  телефона по отдельности в трёх полях: country, city и number.
* Если указано и то, и то, используются данные из трёх полей.
  В поле formatted допустимо использовать пробелы, скобки и дефисы.
  В остальных полях допустимы только цифры

<H3>Поле education</H3>
Особенности сохранения образования:
* Если передать и высшее и среднее образование и уровень образования "средний",
  то сохранится только среднее образование.
* Если передать и высшее и среднее образование и уровень образования "высшее",
  то сохранится только высшее образование

<H3>Поле education</H3>


<H2>Таблицы константных ресурсов и ссылок</H2>
<H3>Тип видимости</H3>

| id        | name                                                   |
|-----------|--------------------------------------------------------|
| no_one    | не видно никому                                        |
| whitelist | видно выбранным компаниям                              |
| blacklist | скрыто от выбранных компаний                           |
| clients   | видно всем компаниям, зарегистрированным на HeadHunter |
| everyone  | видно всему интернету                                  |
| direct    | доступно только по прямой ссылке                       |

Установить значение параметра можно при создании или редактировании резюме.
Возможные значения приведены в поле resume_access_type справочника полей.
С 1 сентября 2021 года тип видимости everyone стал недоступен для сохранения
из-за законодательных ограничений.

<H3>Готовность к командировкам</H3>

| id        | name                         |
|-----------|------------------------------|
| ready     | готов к командировкам        |
| sometimes | готов к редким командировкам |
| never     | не готов к командировкам     |


<H3>Тип водительских прав</H3>

| id |
|----|
| A  |
| B  |
| C  |
| D  |
| E  |
| BE |
| CE |
| DE |
| TM |
| TB |

<H3>Тип занятости</H3>

| id        | name                |
|-----------|---------------------|
| full      | Полная занятость    |
| part      | Частичная занятость |
| project   | Проектная работа    |
| volunteer | Волонтерство        |
| probation | Стажировка          |

<H3>Скрытые поля</H3>

| id              | name                                  |
|-----------------|---------------------------------------|
| names_and_photo | ФИО и фотографию                      |
| phones          | Все указанные в резюме телефоны       |
| email           | Электронную почту                     |
| other_contacts  | Прочие контакты (Skype, ICQ, соцсети) |
| experience      | Все места работы                      |

<H3>Графики работы</H3>

| id          | name             |
|-------------|------------------|
| fullDay     | Полный день      |
| shift       | Сменный график   |
| flexible    | Гибкий график    |
| remote      | Удаленная работа |
| flyInFlyOut | Вахтовый метод   |

<H3>Желаемое время в пути до работы</H3>

| id                        | name                    |
|---------------------------|-------------------------|
| any                       | Не имеет значения       |
| less_than_hour            | Не более часа           |
| from_hour_to_one_and_half | Не более полутора часов |

<H3>Пол человека</H3>

| id     | name    |
|--------|---------|
| male   | Мужской |
| female | Женский |

<H3>Тип готовности к переезду</H3>

| id                   | name                |
|----------------------|---------------------|
| no_relocation        | не готов к переезду |
| relocation_possible  | готов к переезду    |
| relocation_desirable | хочу переехать      |

<H3>Валюта</H3>

| abbr      | code | default | in_use | name              | rate       |
|-----------|------|---------|--------|-------------------|------------|
| AZN       | AZN  | false   | false  | Манаты            | 0.027728   |
| бел. руб. | BYR  | false   | false  | Белорусские рубли | 0.042685   |
| EUR       | EUR  | false   | true   | Евро              | 0.015982   |
| GEL       | GEL  | false   | false  | Грузинский лари   | 0.0344     |
| KGS       | KGS  | false   | false  | Киргизский сом    | 1.356611   |
| KZT       | KZT  | false   | false  | Тенге             | 7.809145   |
| руб.      | RUR  | true    | true   | Рубли             | 1          |
| грн.      | UAH  | false   | false  | Гривны            | 0.596833   |
| USD       | USD  | false   | true   | Доллары           | 0.016311   |
| сум       | UZS  | false   | false  | Узбекский сум     | 177.916948 |

<H3>Тип сайта</H3>

| id          | name        |
|-------------|-------------|
| personal    | Другой сайт |
| moi_krug    | Мой круг    |
| livejournal | LiveJournal |
| linkedin    | LinkedIn    |
| freelance   | Free-lance  |
| skype       | Skype       |
| icq         | ICQ         |

<H3>Тип контакта</H3>

| id    | name              |
|-------|-------------------|
| home  | Домашний телефон  |
| work  | Рабочий телефон   |
| cell  | Мобильный телефон |
| email | Эл. почта         |

<H3>Уровень образования</H3>

| id                | name                |
|-------------------|---------------------|
| secondary         | Среднее             |
| special_secondary | Среднее специальное |
| unfinished_higher | Неоконченное высшее |
| higher            | Высшее              |
| bachelor          | Бакалавр            |
| master            | Магистр             |
| candidate         | Кандидат наук       |
| doctor            | Доктор наук         |

<H3>Уровень владения языком</H3>

| id  | name                    |
|-----|-------------------------|
| a1  | A1 — Начальный          |
| a2  | A2 — Элементарный       |
| b1  | B1 — Средний            |
| b2  | B2 — Средне-продвинутый |
| c1  | C1 — Продвинутый        |
| c2  | C2 — В совершенстве     |
| l1  | Родной                  |

<H2>Получение остальных данных с помощью запросов</H2>
* metro - https://api.hh.ru/metro
* area - https://api.hh.ru/areas
* resume_locale - https://api.hh.ru/locales
* work_ticket = area
* citizenship = area
* industries - https://api.hh.ru/industries
