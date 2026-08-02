# CS 2430 Programming Project 3

**Student:** Sandro Demartin  
**Project:** Space Shuttle Payload Optimization
**Language:** Java 21

## Overview

This program chooses science experiments for a space-shuttle payload with a maximum capacity of 700 kg. It implements and compares:

1. Highest-rating-first greedy selection
2. Lightest-weight-first greedy selection
3. Best rating-to-weight-ratio greedy selection
4. Brute-force exhaustive search across all 4,096 subsets
5. Dynamic programming

The program displays each selected subset, total weight, total rating, the top three exhaustive-search results, and a comparison summary.

## Build and Run in VS Code or a Terminal

Open the `project3` folder in VS Code. In the terminal, run:

### Windows PowerShell

```powershell
cd src
javac *.java
java Project3App
```

Expected test result:

```text
All Project 3 tests passed.
```

## Expected Main Results

| Method | Weight | Rating | Optimal? |
|---|---:|---:|---|
| Highest rating first | 690 kg | 45 | No |
| Lightest weight first | 654 kg | 52 | No |
| Best ratio first | 654 kg | 52 | No |
| Brute force | 692 kg | 53 | Yes |
| Dynamic programming | 692 kg | 53 | Yes |