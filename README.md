# Mega Tetris

An Android Tetris game built with **Java** and **XML layouts** in **Android Studio**.

**IT LEC - Group 6**

## Screenshots

<p align="center">
  <img src="1000005312.jpg" alt="Mega Tetris screenshot 1" width="200">
  <img src="1000005315.jpg" alt="Mega Tetris screenshot 2" width="200">
  <img src="1000005314.jpg" alt="Mega Tetris screenshot 3" width="200">
  <img src="1000005316.jpg" alt="Mega Tetris screenshot 4" width="200">
</p>

## Features

- Classic Tetris gameplay with 7 tetrominoes (I, J, L, O, S, T, Z)
- **Next piece** preview, **score**, and **level** display
- **Pause / Resume** button. All blocks blink while the game is paused
- **Game Over** shown inside the grid: *"Game Over! Click to try again"*. Tap the grid to restart with the score reset to 0
- **4 difficulty levels:** Easy, Medium, Hard, Lunatic
- **Top Score** screen (saved on the device)
- Background music and button / line-clear / level-up sound effects
- Hold the ▼ button for continuous soft drop

## Controls

| Button | Action |
|--------|--------|
| ◀ | Move left |
| ▶ | Move right |
| ⟳ | Rotate |
| ▼ | Soft drop (hold for continuous drop) |
| Pause | Pause / resume the game |

## Scoring

| Lines cleared at once | Points (× current level) |
|-----------------------|--------------------------|
| 1 | 40 |
| 2 | 100 |
| 3 | 300 |
| 4 (Tetris) | 1200 |

Hard drop adds 2 points per row. The level goes up every 10 lines cleared.

## Project Structure

| File | Description |
|------|-------------|
| `MainActivity.java` | Main menu (Play, Set Difficulty, Top Score, Credits) |
| `GameActivity.java` | Game screen: buttons, sounds, pause, game over handling |
| `GameView.java` | Draws the grid, runs the drop loop, blinking pause, in-grid Game Over |
| `Board.java` | Game logic: grid, collisions, line clearing, score, level, difficulty |
| `Tetromino.java` | The 7 piece shapes, rotations, and colors |
| `NextPieceView.java` | Next piece preview |
| `DifficultyActivity.java` | Difficulty selection (saved with SharedPreferences) |
| `TopScoreActivity.java` / `ScoreManager.java` | Saving and showing the top score |
| `CreditsActivity.java` | Credits screen |

## How to Run

1. Clone the repository:
   ```
   git clone https://github.com/rakka-m/IT-LEC-GROUP-6-Tetris-MEGA-TETRIS-.git
   ```
2. Open the project in **Android Studio**.
3. Wait for Gradle to finish syncing.
4. Run the app on an emulator or a real Android phone.

## Group 6 Members

Leader: Medina, Emmanuel Jacinto M.
Members:
- Jusayan Alwin Gabriel
- Camaclang, Kimberly
- Galenzoga, Keisha Chloe
- Gerodias, Eisen
- Seguron, Nicole Alfaro
- Espela, Kurt Edcel
   
