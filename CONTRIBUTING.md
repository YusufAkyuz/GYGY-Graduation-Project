# Katkı Rehberi

## Branch Modeli

- `main` — daima deploy edilebilir durumda; sadece `develop`'tan merge ile güncellenir.
- `develop` — entegrasyon dalı; tüm feature dalları buraya merge edilir.
- `feature/<faz>-<kisa-aciklama>` — örn. `feature/faz2-customer-kyc`.
- `fix/<kisa-aciklama>` — hata düzeltmeleri.
- `chore/<kisa-aciklama>` — build, dokümantasyon, bağımlılık güncellemeleri gibi iş dışı değişiklikler.

Feature dalları `develop`'tan açılır, PR ile tekrar `develop`'a merge edilir. Faz sonu milestone'larında `develop` → `main` merge edilir ve etiketlenir (`v0.<faz>.0`).

## Commit Konvansiyonu

[Conventional Commits](https://www.conventionalcommits.org/) kullanılır:

```
<tip>(<kapsam>): <özet>

[opsiyonel gövde]
```

Tipler: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `ci`, `perf`.
Kapsam servis adı olmalı (örn. `customer-service`, `api-gateway`, `common`).

Örnekler:
- `feat(customer-service): KYC onay endpoint'i eklendi`
- `fix(payment-service): idempotency-key kontrolü düzeltildi`
- `chore(root): parent pom Spring Cloud 2025.1.2'ye güncellendi`

## Definition of Done

Her iş kalemi için: kod + birim test + Flyway migration (varsa) + OpenAPI dokümantasyonu + outbox/idempotency uyumu (event üreten/tüketen servislerde) + code review + yeşil pipeline.
