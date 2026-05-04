# SWI projekt – Backend

Backendová část semestrálního projektu do předmětu SWI.

Projekt představuje jednoduchý knihovní systém, který umožňuje pracovat s knihami, autory, uživateli, výpůjčkami a hodnocením knih. Backend zajišťuje komunikaci s databází, zpracování požadavků z frontendu a hlavní aplikační logiku.

Frontendová část projektu je v samostatném repozitáři:

- https://github.com/erikafelder/swi.project.FE

## Popis projektu

Cílem projektu je vytvořit jednoduchou vícevrstvou webovou aplikaci s databází.

Aplikace je rozdělena na:

- frontend,
- backend,
- databázovou část.

Backend přijíma požadavky z frontendu pomocí API endpointů, zpracuje je a podle potřeby komunikuje s databází. Výsledky potom vrací zpět frontendu.

## Použité technologie

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MariaDB
- Maven
- XAMPP / phpMyAdmin

## Architektura backendu

Backend je rozdělený do několika vrstev, aby byl kód přehlednější a lépe udržovatelný.

### Entity

Entity reprezentují tabulky v databázi.

Například:

- kniha,
- autor,
- uživatel,
- výpůjčka,
- hodnocení.

### Repository

Repository slouží ke komunikaci s databází.

Pomocí repository backend načítá, ukládá, upravuje a maže data.

### Service

Service vrstva obsahuje hlavní aplikační logiku.

Například při půjčení knihy se zde řeší, jestli je kniha dostupná, komu se půjčuje a jak se má vytvořit výpůjčka.

### Controller

Controller přijíma požadavky z frontendu přes API endpointy.

Controller požadavek předá do service vrstvy a výsledek vrátí zpět frontendu jako odpověď.

## Databáze

Projekt používá relační databázi MariaDB.

Databázové schéma je uložene v souboru:

```text
database.schema.sql


Databáze obsahuje například tabulky pro:

knihy,
autory,
uživatele,
výpůjčky,
hodnocení.

V databázi jsou použité vztahy mezi tabulkami, například vztah mezi knihou a autorem nebo mezi uživatelem a výpůjčkou.

Testovací data 

Při spuštění backendu se automaticky vytvoří základní testovací data.

Součástí inicializace jsou ukázkové knihy, autoři a dva předpřipravení uživatelé:

administrátor: admin / admin123
běžný uživatel: emil / emil123

Uživatel Emil má vytvořenou aktivní výpůjčku knihy Hobit, která je po termínu vrácení.
Tato data slouží pro ukázku práce s výpůjčkami a kontrolu opožděného vrácení knihy.

Spuštění projektu
1. Spuštění databáze

Nejdříve je potřeba spustit MariaDB, například přes XAMPP.

V phpMyAdmin je potřeba vytvořit databázi a případně importovat soubor:

database.schema.sql
2. Nastavení připojení k databázi

V souboru application.properties je potřeba nastavit připojení k databázi.

Příklad:

spring.datasource.url=jdbc:mariadb://localhost:3306/nazev_databaze
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update

Hodnoty se můžou lišit podle lokálního nastavení databáze.

3. Spuštění backendu

Backend je možné spustit v IntelliJ IDEA 

Po spuštění backend běží lokálně a frontend na něj může posílat požadavky přes API.

Hlavní funkce backendu:
načítání knih z databáze,
práce s autory,
práce s uživateli,
vytvoření výpůjčky,
vrácení knihy,
kontrola dostupnosti knihy,
práce s hodnocením knih,
základní přihlášení uživatelů,
ukázka opožděné výpůjčky na testovacím uživateli.



Projekt je vytvořený jako školní semestrální práce.

Cílem bylo ukázat princip vícevrstvé webové aplikace, práci s databází, API komunikaci a základní aplikační logiku na backendu.
