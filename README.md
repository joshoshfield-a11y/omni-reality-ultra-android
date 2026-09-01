# Omni-Reality ULTRA [V7: HYPERDRIVE]

Real-time WebGL2 camera feedback effects engine, wrapped in a native Android WebView shell.
Built for Samsung Galaxy S25 (Snapdragon 8 Elite, 120Hz AMOLED).

## V7 enhancements over V6
- **Fully offline** — Three.js r160 bundled in-app (no CDN dependency)
- **4 new shader effects**: CHROMA BLOOM, EVENT HORIZON (zoom-feedback tunnel), FILM GRAIN, PHOTON STROBE
- **Preset Matrix** — 7 one-tap effect protocols
- **State persistence** — slider state auto-saves and restores across launches
- **Snapshot button** — PNG capture straight to gallery
- **Native recording save** — recordings land in Gallery via JS->Kotlin MediaStore bridge (MP4 preferred, 12 Mbps, 60 fps capture)
- **Adaptive quality engine** — render resolution auto-scales to hold frame rate
- **Front-camera mirror flip**, 1080p60 camera request with graceful fallback
- **Immersive edge-to-edge**, keep-screen-on, safe-area aware UI, haptics
- Resolution-aware edge detection (texel-size uniform)

## Build
GitHub Actions builds both debug and release (debug-key signed) APKs on every push.
Artifacts: `app-debug.apk`, `app-release.apk`.

## Install
Sideload `app-release.apk` (same debug key across builds -> updates install in place;
just bump `versionCode`).
