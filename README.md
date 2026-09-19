# NaviSoft / NaviPOS

Desktopová pokladní aplikace postavená na JavaFX a Spring Bootu. Projekt používá
jeden Gradle modul `navisoft`.

## Technologie

- JDK 25 (Gradle toolchain se stáhne automaticky)
- Gradle 9.7.1 Wrapper
- Spring Boot 4.1.1 bez webového serveru
- JavaFX 25.0.4
- Spring Data JPA, Hibernate a HikariCP
- MySQL Connector/J
- Flyway pro řízené databázové migrace

## Databáze

Připojení se konfiguruje proměnnými prostředí. Výchozí hodnoty jsou určené jen
pro lokální vývoj:

```shell
export NAVISOFT_DB_URL='jdbc:mysql://localhost:3306/pricetags?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Europe/Prague'
export NAVISOFT_DB_USERNAME='navisoft'
export NAVISOFT_DB_PASSWORD='change-me'
export NAVISOFT_CASHDESK_PIN='change-me'
```

Příklad je v `.env.example`. Soubor `.env` se neukládá do Gitu.

Flyway je ve výchozím stavu zapnutý. U existující databáze vytvoří baseline a
aplikuje migraci fronty EET 2.0. Databázový uživatel proto musí mít při prvním
spuštění oprávnění vytvořit tabulku a index:

```shell
export NAVISOFT_FLYWAY_ENABLED=true
```

## EET 2.0

EET se zapíná v nastavení aplikace. Odeslání probíhá asynchronně a neblokuje
uložení ani tisk účtenky. Neúspěšná odeslání jsou uložena v databázové frontě a
aplikace je automaticky opakuje. Výchozím prostředím je Playground; produkční
prostředí zapněte až s platným produkčním certifikátem.

Certifikát a heslo je doporučeno předat přes prostředí, aby heslo nemuselo být
uloženo v databázi:

```shell
export NAVISOFT_EET_ENVIRONMENT=playground
export NAVISOFT_EET_CERTIFICATE_PATH=/bezpecna/cesta/certifikat.p12
export NAVISOFT_EET_CERTIFICATE_PASSWORD='change-me'
```

Povolené hodnoty `NAVISOFT_EET_ENVIRONMENT` jsou `playground` a `production`.
Pokud proměnná není nastavena, použije se volba prostředí z UI. Staré nastavení
certifikátu v databázi zůstává kvůli zpětné kompatibilitě podporované jako
záložní varianta.

## Sestavení a testy

```shell
./gradlew clean :navisoft:build
```

## Spuštění ve vývoji

```shell
./gradlew :navisoft:run
```

Spring Boot běží jako ne-webový aplikační kontejner. JavaFX řídí lifecycle UI a
FXML controllery vytváří Spring bean factory.

## Desktopový balíček

```shell
./gradlew :navisoft:jpackage
```

Výsledek je v `navisoft/build/jpackage`. Balíček je platformní, proto se musí
Windows, macOS a Linux distribuce sestavit na odpovídajícím operačním systému.
Na macOS vzniká `NaviPOS-mac.zip` obsahující self-contained `NaviPOS.app`.
