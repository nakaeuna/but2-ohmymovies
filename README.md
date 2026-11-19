# Oh My Movies!

> Multilingual Android movie discovery app built with Kotlin, Ktor, Coroutines, and the TMDB API.

![Context](https://img.shields.io/badge/Context-BUT%202%20(S4)-2ea44f?style=flat-square)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white)

## Overview

**Oh My Movies!** is an Android mobile application developed during BUT 2 (S4 — Mobile Development). Powered by **The Movie Database (TMDB)** API, it lets users browse popular films, search by title, filter/sort results, and explore rich movie detail pages in multiple languages.

## Features

- **Movie search & popular movies**: search films by query or browse currently popular titles.
- **Custom results list**: displays poster artwork, title, release date, and tagline in a custom `ListView`.
- **Sorting options**: sort results by popularity, release date (newest/oldest), or alphabetical order.
- **Detailed movie view**: synopsis, poster, genres, release date, original language, adult-content flag, user rating, and vote count.
- **Multilingual support (i18n)**: adapts UI strings and TMDB API requests to the device locale (`EN`, `FR`, `DE`, `ES`, `IT`, `EL`, with French fallback).
- **Responsive orientation**: handles portrait and landscape screen rotations seamlessly.

## Screenshots

| Home / Search | Results | Movie Details |
| :---: | :---: | :---: |
| <img src="images/recherche.png" alt="Home Screen" width="210" /> | <img src="images/resultats.png" alt="Results Screen" width="210" /> | <img src="images/film.png" alt="Movie Screen" width="210" /> |

## Tech Stack

- **Kotlin** & **Android SDK** (`minSdk 33`, `targetSdk 34`)
- **Ktor Client** — asynchronous HTTP calls to the TMDB API
- **Kotlin Coroutines** — non-blocking network execution
- **Kotlinx Serialization** — JSON parsing
- **Picasso** — dynamic poster image loading and caching

## Project Structure

```text
├── .env.example                        # Environment variable template (TMDB_API_KEY)
├── app/src/main/
│   ├── java/com/example/ohmymovies/
│   │   ├── RechercheActivity.kt        # Main search & popular movies screen
│   │   ├── ResultatsActivity.kt        # Search results list & sorting
│   │   ├── FilmActivity.kt             # Movie detail view
│   │   ├── FilmAdapter.kt              # Custom ListView adapter
│   │   ├── DAO.kt                      # TMDB API client & JSON deserialization
│   │   └── Films.kt / FilmSearch.kt / FilmGetById.kt / Genre.kt
│   └── res/
│       ├── layout/                     # XML activity & item layouts
│       └── values-{de,el,en,es,fr,it}/ # Localized string resources
└── images/                             # App screenshots
```

## Getting Started

1. **Create your `.env` file** from the template and add your TMDB API key:
   ```bash
   cp .env.example .env
   ```
   Then edit `.env`:
   ```dotenv
   TMDB_API_KEY=your_tmdb_api_key_here
   ```
2. **Open the project in Android Studio** and sync Gradle dependencies.
3. **Run the application** on an Android emulator or physical device.

## Contributors

- Léa Cortet
- Maxime Bouzonie
- Clément Hubert