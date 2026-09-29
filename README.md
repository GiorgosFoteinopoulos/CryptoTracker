CryptoTracker

A production-ready Android cryptocurrency tracking application built as the individual project assessment (70% of the module mark) for CCS6212 – Mobile Application Development, part of the MSc in Web & Mobile Development at ATH Tech College / University of York Europe Campus.

CryptoTracker lets users monitor cryptocurrency market trends and manage personalised watchlists. It integrates live data from the CoinGecko API, persists data locally through Room, and delivers a Material Design 3 interface with smooth animations and responsive layouts.

Features
Market Overview — dashboard of trending and top coins with category tabs (Top 100, Trending, Gainers, Losers). Each row shows icon, name, symbol, current price, 24h change with colour coding, and a mini sparkline chart.
Advanced Search & Filter — real-time search across thousands of coins, with filters such as price range and switchable grid / list view modes.
Detailed Coin View — collapsing toolbar with coin icon and name; displays current price, market cap, volume, supply, all-time high/low, 24h statistics, and coin description. A FAB adds the coin to a watchlist.
Watchlist Management — multiple customisable watchlists with drag-to-reorder, swipe-right to add to portfolio, swipe-left to delete, and full offline access via cached data.
Offline access — all core features remain usable without a network connection thanks to intelligent local caching.
Tech Stack
Language: Java
UI: XML layouts with Material Design 3, including Material You dynamic colour theming and tablet-responsive layouts
Architecture: MVVM (ViewModel + LiveData)
Networking: Volley for API communication, with request/response interceptors, retry logic with exponential backoff, HTTP response caching, and graceful rate-limit handling
Persistence: Room with TypeConverters for complex data types (Date, List, custom objects), normalised schema across multiple entities (Coin, Watchlist, Portfolio, Alert), and database migrations
Lists: RecyclerView with multiple ViewTypes, ListAdapter + DiffUtil with payloads for efficient partial updates, dynamic switching between linear and grid layout managers, item animations, and loading / empty / error states rendered inline
Image loading: Coil / Glide with caching
Minimum SDK: 24 (Android 7.0)
API

Live market data is provided by the CoinGecko API. Endpoints used include markets, coin details, and historical price data. Rate limits on the free tier (10–50 calls/min) are handled gracefully with retry and caching.

Project Context
Module: CCS6212 – Mobile Application Development
Programme: MSc in Web & Mobile Development
Institution: ATH Tech College / University of York Europe Campus
Instructor: Vasileios Pigadas
Assessment: Individual project, 70% of the module final mark
Academic year: 2025–2026
Submission

This repository is submitted under the branch assessment_{student name} in accordance with the assessment brief.

Author

Giorgos Foteinopoulos (George) — MSc student, Web & Mobile Development
