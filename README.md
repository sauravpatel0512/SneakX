# SneakX (Android)



# Defination    

Shoe marketplace app for CSE 3310: Kotlin, Jetpack Compose, Room (SQLite), Material 3.

## Requirements

- Android Studio (latest stable recommended)
- JDK 17
- Android 12+ device or emulator (API 31)

## Open and run

1. Open the `SneakX` folder in Android Studio.
2. Let Gradle sync. If the Gradle wrapper is missing, use **File → Settings → Build, Execution, Deployment → Gradle** and point to a local Gradle, or generate a wrapper via **File → New → Import** from a template that includes `gradlew`.
3. Run the `app` configuration on an emulator or device.

## Seeded admin (first launch)

After install, the database seeds one administrator (see `AppContainer.seedAdminIfNeeded` and `SeedConstants`):

- **Email:** `admin@sneakx.local`
- **Password:** `Admin123!`

Change these in a production build; they are for local demo and testing only.

## Features (course docs scope)

- Register (Buyer / Seller), login, logout; lockout after 5 failed attempts (15 minutes).
- Role dashboards: Buyer (Home, Cart, Profile), Seller (Home, My listings, Profile), Admin (Home, Users, Listings, Profile).
- Listings with photos (picker), search/filter/sort on the marketplace.
- Cart, mock checkout (optional “simulate payment failure” for demos), orders and listing marked sold.
- Admin: disable users, disable/remove listings; actions logged with timestamps.
- Profile edit (unique email) and theme preference (system / light / dark).


## Project layout

- `app/` — Android application module (`com.team3.sneakx`)
