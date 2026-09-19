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
- Flyway připravený pro řízené databázové migrace

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

Flyway je zatím ve výchozím stavu vypnutý. Zapněte jej až po vytvoření a ověření
baseline existující produkční databáze:

```shell
export NAVISOFT_FLYWAY_ENABLED=true
```

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
