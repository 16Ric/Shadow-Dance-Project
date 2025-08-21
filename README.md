# ShadowDance – Music Arcade Game (Java)

**ShadowDance** is a Java-based music arcade game where players hit musical notes in sync with the rhythm to score points. The game consists of three levels, each with increasing complexity:

## Levels

- **Level 1:** Four lanes of notes. Press the corresponding arrow key when notes overlap the stationary target. Includes hold notes. Target score: 150.
- **Level 2:** Adds a special lane with notes that trigger effects like *Double Score*, *Speed Up*, *Slow Down*, or *Bomb*. Target score: 400.
- **Level 3:** All previous features plus enemies that steal notes and a guardian that shoots projectiles to stop them. Target score: 350.

## Gameplay Features

- Notes descend from the top at a fixed speed.  
- Scoring is based on timing accuracy: *Perfect*, *Good*, *Bad*, or *Miss*.  
- Special notes modify gameplay temporarily.  
- Players can select any level from the start screen and replay levels after completion.

## Development

- Implemented in **Java** using the **Bagel game engine**.  
- Level data defined in CSV "world files" for lanes, notes, and timing.  
- Class design submitted in **Project 2A** (UML diagram); implementation completed in **Project 2B**.  
- Start/end screens, score display, and entity rendering fully implemented.

## Objective

Achieve the target score in each level to win while managing increasingly complex challenges.
