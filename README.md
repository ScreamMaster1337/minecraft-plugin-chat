# Chat Control Plugin

Chat Control Plugin to plugin do Minecrafta, który pozwala na zarządzanie dostępem do czatu na serwerze. Umożliwia administratorom włączanie, wyłączanie, czyszczenie czatu oraz ograniczanie dostępu dla graczy. Dodatkowo można ustawić cooldown na wysyłanie wiadomości na czacie.

## Funkcje
- **Włączanie/wyłączanie czatu**: Pozwala administratorom na pełną kontrolę nad tym, kto może korzystać z czatu.
- **Tryb VIP**: Ogranicza możliwość korzystania z czatu tylko dla graczy z uprawnieniami VIP oraz administracji.
- **Cooldown**: Umożliwia ustawienie przerwy czasowej pomiędzy wiadomościami na czacie dla zwykłych graczy.
- **Czyszczenie czatu**: Administratorzy mogą wyczyścić czat.
- **Przeładowanie konfiguracji**: Plugin obsługuje przeładowanie konfiguracji bez konieczności restartu serwera.

## Użycie Komend

- `/chat on`  
  **Opis:** Włącza czat dla wszystkich graczy.  
  **Przykład:** `/chat on` – Włącza czat.

- `/chat off`  
  **Opis:** Wyłącza czat, pozwalając na pisanie tylko administratorom.  
  **Przykład:** `/chat off` – Wyłącza czat dla graczy, administracja może pisać.

- `/chat vip`  
  **Opis:** Włącza czat tylko dla graczy z uprawnieniami premium i administracji.  
  **Przykład:** `/chat vip` – Czat dostępny tylko dla rang premium i administracji.

- `/chat clear`  
  **Opis:** Czyści wszystkie wiadomości z czatu.  
  **Przykład:** `/chat clear` – Wyczyści czat.

- `/chat cooldown <czas>`  
  **Opis:** Ustawia cooldown (opóźnienie) pomiędzy wiadomościami dla zwykłych graczy.  
  **Przykład:** `/chat cooldown 10s` – Ustawia 10-sekundowy cooldown dla wiadomości na czacie.

- `/chat reload`  
  **Opis:** Przeładowuje konfigurację pluginu.  
  **Przykład:** `/chat reload` – Przeładowuje pliki konfiguracyjne.

## Konfiguracja

Plugin automatycznie tworzy pliki konfiguracyjne, w których można dostosować:

- **Zarządzanie czatem**: Można włączać/wyłączać czat, ustawić tryb tylko dla VIP-ów oraz cooldown na wiadomości.
- **Wiadomości**: Umożliwia dostosowanie treści wiadomości, które gracze widzą podczas interakcji z czatem.

### Przykład konfiguracji (`config.yml`):

```yaml
chat:
  enabled: true
  vipOnly: false
  cooldown: 5

messages:
  usage: "&4Błąd &cPoprawne użycie: &7/chat <reload|on|off|cooldown|vip|clear>"
  reload: "&aKonfiguracja została przeładowana!"
  no_permission: "&cNie masz uprawnień do używania tej komendy!"
  chat_enabled: "&7Czat został &a&nwłączony&r &7przez &a&n%admin%"
  chat_disabled_by_admin: "&7Czat został &c&nwyłączony&r &7przez &a&n%admin%"
  chat_vip: "&7Czat został &a&nwłączony&r &7tylko dla rang PREMIUM przez &a&n%admin%"
  chat_cleared: "&7Czat został &e&nwyczyszczony&r &7przez &a&n%admin%!"
  usage_cooldown: "&4Błąd &cPoprawne użycie: &7/chat cooldown <czas>"
  cooldown_set: "&aCooldown czatu ustawiony na %time% sekund!"
  invalid_time_format: "&cNieprawidłowy format czasu!"
  chat_disabled: "&cCzat jest obecnie wyłączony!"
  chat_vip_only: "&cTylko rangi &a&nPREMIUM&r mogą teraz pisać na czacie!"
  cooldown_wait: "&cMusisz poczekać %time% sekund!"
  unknown_subcommand: "&cNieznana podkomenda!"
```

**Wymagania:**
- Serwer Minecraft (wersja 1.16+)
- Java 8+

**Autor:** [ScreamMaster1337](https://github.com/ScreamMaster1337)  
**Licencja:** MIT
