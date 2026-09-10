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

## V8: FORBIDDEN TRANSMISSIONS (2026-09-01)
- Fixed: NERVE ENDING div-by-zero blowout, DEMON CORE dead jitter (now resamples),
  TIME RIPPLE instant death (residual ripples), NEURAL PULSE negative mix weight,
  SPIRIT hard edge cutoff (smooth fade)
- 8 new effects: DREAM RECURSION, GLITCH RAIN, ASTRAL PROJECTION, VOID SINGULARITY,
  DIMENSIONAL TEAR, SOUL ECHO, PIXEL POSSESSION, FLESH CIRCUIT
- 3 new presets: ABYSSAL DREAM, FULL POSSESSION, FLESH REACTOR
- V7 saved state migrates forward automatically

## V9: CHRONO ENGINE (2026-09-01)
Incorporated CHRONO-STUDIO v11's temporal mechanics into the WebGL pipeline (nothing removed):
- 8-layer temporal ring buffer (GPU render targets) + cheap blit pass — GPU refinement of
  chrono's 120-frame CPU ring store
- TIME DEPTH + 8 DEPTH MODES (LUMA/CHROMA/INVERT/RADIAL/QUANTUM/EDGE/POSTER/SOLAR):
  per-pixel depth value selects which past frame to draw from
- TIME VORTEX: deeper layers rotate/zoom harder, mirror-folded coords (refined tile-flip)
- WARP/SEP: row+liquid column waves shift the temporal index
- TEMPORAL GLITCH: RGB channels sampled from different depths in time
- JANUS GATE: |now - deepest past| aura (chrono's fallback mode, GPU-native)
- LAMBDA LOOP: difference-energy injection
- HOLO VEIL: luma-driven oscillating chromatic shear
- DATA CLOUD: depth-displaced dot field (chrono CLOUD, shader-native)
- TIME LOCK: freezes the ring (chrono's HOLD-TO-FREEZE-TIME)
- AUDIO REACT: mic analyser drives depth reach / vortex / holo (RECORD_AUDIO added)
- 2 new presets: CHRONO CANON, JANUS PROTOCOL; saved state migrates V7/V8 -> V9

## V11: MIRROR SPECTRUM (2026-09-09)
Face recognition + spectral analysis layer (nothing removed):
- **Offline face lock** — pico.js (MIT) bundled with embedded cascade (base64 sidecar,
  zero network); 9Hz side channel on a 160px analysis canvas, decoupled from the render
  loop; snap-limited EMA tracking of up to 4 faces; mirror-corrected to shader uv space
- **FACE WARP** — vortex pinch/swirl centered on each detected face
- **FACE MANDALA** — kaleidoscope pivot snaps to the dominant face
- **FACE ECHO** — temporal ghosts anchored to face positions (feeds the feedback buffer)
- **FACE ZOOM** — auto-magnify onto the dominant face
- **DEMON HALO** — chromatic aura of Sobel edge energy, bound to faces
- **NEON SOBEL** — full-res chromatic edge tracer; >0.5 = edges-only X-RAY SPECTRUM
- **SLIT SCAN** — per-column time smear from the feedback buffer
- **FLUID DRAG** — frame-difference flow field smears motion like wet ink
- New preset: MIRROR DEMON; saved state migrates V7/V8/V9/V10 -> V11
