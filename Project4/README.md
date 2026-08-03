# CS 2430 Programming Project 4 — Monopoly Simulation

## Student
Sandro Demartin  
CS 2430, Section 501  
Programming Project 4 — Fall 2026

## Purpose
This project simulates the movement portion of a single-player 2018 U.S. classic Monopoly game. It measures empirical landing probabilities for all 40 squares and compares two jail-exit strategies.

- **Strategy A:** Use a Get Out of Jail Free card immediately; otherwise pay and leave on the next turn.
- **Strategy B:** Use a Get Out of Jail Free card immediately; otherwise try for doubles for three jail turns and pay to leave on the fourth.

Money, ownership, rent, auctions, and player-versus-player decisions are intentionally excluded.

## Project structure

```text
project4/
├── src/
│   └── monopoly_simulator.py
├── tests/
│   └── test_monopoly_simulator.py
├── data/
│   ├── 80 detailed CSV files after a full run
│   ├── aggregate_summary.csv
│   ├── run_summary.txt
│   ├── run_summary.json
│   └── strategy_comparison_1m.png
└── README.md
```

## Requirements
- Python 3.10 or newer
- Optional: `matplotlib` for the extra-credit chart
- Optional: `pytest` to run the test file

Install optional tools:

```bash
python -m pip install matplotlib pytest
```

## Run the required full simulation
From the project folder:

```bash
python src/monopoly_simulator.py
```

This creates all 80 required datasets:

- 2 strategies
- 4 turn counts: 1,000; 10,000; 100,000; 1,000,000
- 10 independent runs for each strategy/turn-count combination


```bash
python src/monopoly_simulator.py --quick --output data_quick
```

## Reproducibility
The default base seed is `2430`. Every dataset uses a different derived seed, which is written into its CSV file. To use another base seed:

```bash
python src/monopoly_simulator.py --seed 9001
```

## Run tests

```bash
python -m pytest tests -v
```

Without pytest, the program still performs an internal check that every dataset contains exactly one final landing for each simulated turn.

## Output format
Each detailed CSV includes:

- strategy
- run number
- number of turns
- random seed
- square index
- square name
- square type
- raw landing count
- landing percentage

`aggregate_summary.csv` contains the mean, standard deviation, minimum, and maximum percentage across the ten runs for each square.

`run_summary.txt` lists the top five squares for every strategy and turn count, plus the largest differences between the strategies at the largest turn count.