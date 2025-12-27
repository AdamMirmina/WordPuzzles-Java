# Word Puzzles

A collection of classic word-based console games built in Java — including Wordle, Spelling Bee, and Anagrams — all playable from a single interactive menu.

## Features
- Wordle: Guess the hidden five-letter word with color-coded feedback for each letter.
- Spelling Bee: Form as many valid words as possible in 2 minutes using a letter set with a required center letter.
- Anagrams: Unscramble a word as quickly as possible, with optional shuffles and hints (with time penalties).
- Color-coded terminal output for a more engaging play experience.
- Keeps track of player statistics (streaks, high scores, best times) between games.

## Technologies Used
- Java (Core language)
- ANSI escape codes for color output
- Threads for real-time game timers
- File I/O for loading word lists and puzzles

## How to Run

### 1) Clone the repo
git clone https://github.com/AdamMirmina/WordPuzzles.git
cd WordPuzzles

### 2) Compile
#### Windows (PowerShell)
javac .\src\*.java

#### macOS/Linux (Terminal)
javac src/*.java

### 3) Run
> Run from the project root (this folder) so the game can find the word list files (`WordleWords`, `WordleGuessable`, `SpellingBee`, `Anagrams`).

#### Windows (PowerShell)
java -cp .\src Driver

#### macOS/Linux (Terminal)
java -cp src Driver
