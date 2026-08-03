"""
Sandro Demartin
CS 2430, Section 501
Programming Project 4 – Fall 2026

Single-player Monopoly movement simulator. It compares two jail-exit strategies,
runs repeatable batch simulations, saves detailed CSV datasets, and creates a
summary suitable for analysis in the formal report.
"""

from __future__ import annotations

import argparse
import csv
import importlib
import json
import random
import statistics
from dataclasses import dataclass, field
from pathlib import Path
from typing import Iterable, Literal

Strategy = Literal["A", "B"]

BOARD = [
    ("GO", "GO"),
    ("Mediterranean Avenue", "Property"),
    ("Community Chest 1", "Community Chest"),
    ("Baltic Avenue", "Property"),
    ("Income Tax", "Tax"),
    ("Reading Railroad", "Railroad"),
    ("Oriental Avenue", "Property"),
    ("Chance 1", "Chance"),
    ("Vermont Avenue", "Property"),
    ("Connecticut Avenue", "Property"),
    ("Jail / Just Visiting", "Jail"),
    ("St. Charles Place", "Property"),
    ("Electric Company", "Utility"),
    ("States Avenue", "Property"),
    ("Virginia Avenue", "Property"),
    ("Pennsylvania Railroad", "Railroad"),
    ("St. James Place", "Property"),
    ("Community Chest 2", "Community Chest"),
    ("Tennessee Avenue", "Property"),
    ("New York Avenue", "Property"),
    ("Free Parking", "Free Parking"),
    ("Kentucky Avenue", "Property"),
    ("Chance 2", "Chance"),
    ("Indiana Avenue", "Property"),
    ("Illinois Avenue", "Property"),
    ("B. & O. Railroad", "Railroad"),
    ("Atlantic Avenue", "Property"),
    ("Ventnor Avenue", "Property"),
    ("Water Works", "Utility"),
    ("Marvin Gardens", "Property"),
    ("Go to Jail", "Go to Jail"),
    ("Pacific Avenue", "Property"),
    ("North Carolina Avenue", "Property"),
    ("Community Chest 3", "Community Chest"),
    ("Pennsylvania Avenue", "Property"),
    ("Short Line", "Railroad"),
    ("Chance 3", "Chance"),
    ("Park Place", "Property"),
    ("Luxury Tax", "Tax"),
    ("Boardwalk", "Property"),
]

RAILROADS = (5, 15, 25, 35)
UTILITIES = (12, 28)
JAIL = 10
GO_TO_JAIL = 30

CHANCE_CARDS = [
    "ADVANCE_GO",
    "ADVANCE_ILLINOIS",
    "ADVANCE_ST_CHARLES",
    "ADVANCE_NEAREST_UTILITY",
    "ADVANCE_NEAREST_RAILROAD",
    "ADVANCE_NEAREST_RAILROAD",
    "BANK_DIVIDEND",
    "GET_OUT_OF_JAIL_FREE",
    "GO_BACK_3",
    "GO_TO_JAIL",
    "GENERAL_REPAIRS",
    "SPEEDING_FINE",
    "ADVANCE_READING_RAILROAD",
    "ADVANCE_BOARDWALK",
    "CHAIRMAN_OF_BOARD",
    "BUILDING_LOAN_MATURES",
]

COMMUNITY_CHEST_CARDS = [
    "ADVANCE_GO",
    "BANK_ERROR",
    "DOCTORS_FEE",
    "SALE_OF_STOCK",
    "GET_OUT_OF_JAIL_FREE",
    "GO_TO_JAIL",
    "HOLIDAY_FUND",
    "INCOME_TAX_REFUND",
    "BIRTHDAY",
    "LIFE_INSURANCE",
    "HOSPITAL_FEES",
    "SCHOOL_FEES",
    "CONSULTANCY_FEE",
    "STREET_REPAIRS",
    "BEAUTY_CONTEST",
    "INHERITANCE",
]


class Deck:
    """Shuffled draw pile with discard pile and a removable GOJF card."""

    def __init__(self, cards: Iterable[str], rng: random.Random) -> None:
        self.rng = rng
        self.draw_pile = list(cards)
        self.rng.shuffle(self.draw_pile)
        self.discard_pile: list[str] = []

    def draw(self) -> str:
        if not self.draw_pile:
            self.draw_pile = self.discard_pile
            self.discard_pile = []
            self.rng.shuffle(self.draw_pile)
        if not self.draw_pile:
            raise RuntimeError("Deck has no available cards. A held card may not have been returned.")
        return self.draw_pile.pop()

    def discard(self, card: str) -> None:
        self.discard_pile.append(card)

    def return_gojf(self) -> None:
        self.discard("GET_OUT_OF_JAIL_FREE")


@dataclass
class PlayerState:
    position: int = 0
    in_jail: bool = False
    failed_jail_rolls: int = 0
    held_gojf: list[str] = field(default_factory=list)  # values: "chance" or "community"


@dataclass
class SimulationResult:
    strategy: Strategy
    run_number: int
    turns: int
    seed: int
    counts: list[int]

    @property
    def percentages(self) -> list[float]:
        return [(count / self.turns) * 100.0 for count in self.counts]


class MonopolySimulation:
    """Movement-only Monopoly simulation for one player."""

    def __init__(self, strategy: Strategy, seed: int) -> None:
        if strategy not in ("A", "B"):
            raise ValueError("Strategy must be 'A' or 'B'.")
        self.strategy = strategy
        self.rng = random.Random(seed)
        self.player = PlayerState()
        self.chance = Deck(CHANCE_CARDS, self.rng)
        self.community = Deck(COMMUNITY_CHEST_CARDS, self.rng)
        self.counts = [0] * len(BOARD)
        self.total_turns = 0

    def roll_dice(self) -> tuple[int, int]:
        return self.rng.randint(1, 6), self.rng.randint(1, 6)

    def send_to_jail(self) -> None:
        self.player.position = JAIL
        self.player.in_jail = True
        self.player.failed_jail_rolls = 0

    def use_gojf_if_available(self) -> bool:
        if not self.player.held_gojf:
            return False
        source = self.player.held_gojf.pop(0)
        if source == "chance":
            self.chance.return_gojf()
        else:
            self.community.return_gojf()
        self.player.in_jail = False
        self.player.failed_jail_rolls = 0
        return True

    @staticmethod
    def next_index(position: int, choices: tuple[int, ...]) -> int:
        for choice in choices:
            if choice > position:
                return choice
        return choices[0]

    def apply_card(self, card: str, source: Literal["chance", "community"]) -> bool:
        """Apply card. Return True when the card must be discarded immediately."""
        if card == "GET_OUT_OF_JAIL_FREE":
            self.player.held_gojf.append(source)
            return False
        if card == "ADVANCE_GO":
            self.player.position = 0
        elif card == "GO_TO_JAIL":
            self.send_to_jail()
        elif card == "ADVANCE_ILLINOIS":
            self.player.position = 24
        elif card == "ADVANCE_ST_CHARLES":
            self.player.position = 11
        elif card == "ADVANCE_NEAREST_UTILITY":
            self.player.position = self.next_index(self.player.position, UTILITIES)
        elif card == "ADVANCE_NEAREST_RAILROAD":
            self.player.position = self.next_index(self.player.position, RAILROADS)
        elif card == "GO_BACK_3":
            self.player.position = (self.player.position - 3) % len(BOARD)
        elif card == "ADVANCE_READING_RAILROAD":
            self.player.position = 5
        elif card == "ADVANCE_BOARDWALK":
            self.player.position = 39
        # All remaining cards affect money only and therefore do not change position.
        return True

    def resolve_square(self) -> None:
        """Resolve movement effects until the player reaches a final square."""
        while True:
            pos = self.player.position
            square_type = BOARD[pos][1]

            if pos == GO_TO_JAIL:
                self.send_to_jail()
                return

            if square_type == "Chance":
                card = self.chance.draw()
                discard_now = self.apply_card(card, "chance")
                if discard_now:
                    self.chance.discard(card)
                if self.player.in_jail:
                    return
                # Only keep resolving if the card moved the player to another action square.
                if self.player.position != pos and BOARD[self.player.position][1] in {
                    "Chance", "Community Chest", "Go to Jail"
                }:
                    continue
                return

            if square_type == "Community Chest":
                card = self.community.draw()
                discard_now = self.apply_card(card, "community")
                if discard_now:
                    self.community.discard(card)
                if self.player.in_jail:
                    return
                if self.player.position != pos and BOARD[self.player.position][1] in {
                    "Chance", "Community Chest", "Go to Jail"
                }:
                    continue
                return

            return

    def play_turn_from_jail(self) -> None:
        # Both strategies use a held card immediately.
        if self.use_gojf_if_available():
            d1, d2 = self.roll_dice()
            self.player.position = (JAIL + d1 + d2) % len(BOARD)
            self.resolve_square()
            return

        if self.strategy == "A":
            # Pay immediately at the beginning of the next turn, then roll normally.
            self.player.in_jail = False
            self.player.failed_jail_rolls = 0
            d1, d2 = self.roll_dice()
            self.player.position = (JAIL + d1 + d2) % len(BOARD)
            self.resolve_square()
            return

        # Strategy B: try for doubles for up to three jail turns. If all fail,
        # pay and leave on the fourth jail turn as specified by the assignment.
        if self.player.failed_jail_rolls >= 3:
            self.player.in_jail = False
            self.player.failed_jail_rolls = 0
            d1, d2 = self.roll_dice()
            self.player.position = (JAIL + d1 + d2) % len(BOARD)
            self.resolve_square()
            return

        d1, d2 = self.roll_dice()
        if d1 == d2:
            self.player.in_jail = False
            self.player.failed_jail_rolls = 0
            self.player.position = (JAIL + d1 + d2) % len(BOARD)
            self.resolve_square()
        else:
            self.player.failed_jail_rolls += 1
            self.player.position = JAIL

    def play_regular_turn(self) -> None:
        consecutive_doubles = 0
        while True:
            d1, d2 = self.roll_dice()
            is_double = d1 == d2
            if is_double:
                consecutive_doubles += 1
                if consecutive_doubles == 3:
                    self.send_to_jail()
                    return

            self.player.position = (self.player.position + d1 + d2) % len(BOARD)
            self.resolve_square()
            if self.player.in_jail or not is_double:
                return
            # A double grants another roll within the same turn.

    def play_turn(self) -> None:
        if self.player.in_jail:
            self.play_turn_from_jail()
        else:
            self.play_regular_turn()

        self.counts[self.player.position] += 1
        self.total_turns += 1

    def run(self, turns: int) -> list[int]:
        if turns <= 0:
            raise ValueError("turns must be positive")
        for _ in range(turns):
            self.play_turn()
        if sum(self.counts) != turns:
            raise AssertionError("Landing counts must equal the requested number of turns.")
        return self.counts.copy()


def write_dataset(path: Path, result: SimulationResult) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", newline="", encoding="utf-8") as file:
        writer = csv.writer(file)
        writer.writerow(["strategy", "run", "turns", "seed", "index", "square", "type", "count", "percentage"])
        for index, ((name, square_type), count, percentage) in enumerate(
            zip(BOARD, result.counts, result.percentages)
        ):
            writer.writerow([
                result.strategy,
                result.run_number,
                result.turns,
                result.seed,
                index,
                name,
                square_type,
                count,
                f"{percentage:.6f}",
            ])


def summarize_results(results: list[SimulationResult], output_dir: Path) -> dict:
    grouped: dict[tuple[str, int], list[SimulationResult]] = {}
    for result in results:
        grouped.setdefault((result.strategy, result.turns), []).append(result)

    summary_rows: list[dict] = []
    summary_json: dict[str, dict[str, dict]] = {"A": {}, "B": {}}

    for (strategy, turns), group in sorted(grouped.items()):
        mean_percentages = [
            statistics.mean(r.percentages[index] for r in group)
            for index in range(len(BOARD))
        ]
        stdev_percentages = [
            statistics.stdev(r.percentages[index] for r in group) if len(group) > 1 else 0.0
            for index in range(len(BOARD))
        ]
        ranked = sorted(range(len(BOARD)), key=lambda i: mean_percentages[i], reverse=True)
        top_five = [
            {
                "index": i,
                "square": BOARD[i][0],
                "mean_percentage": round(mean_percentages[i], 6),
            }
            for i in ranked[:5]
        ]
        summary_json[strategy][str(turns)] = {"top_five": top_five}

        for index, (name, square_type) in enumerate(BOARD):
            row = {
                "strategy": strategy,
                "turns": turns,
                "index": index,
                "square": name,
                "type": square_type,
                "mean_percentage": mean_percentages[index],
                "stdev_percentage": stdev_percentages[index],
                "min_percentage": min(r.percentages[index] for r in group),
                "max_percentage": max(r.percentages[index] for r in group),
            }
            summary_rows.append(row)

    summary_csv = output_dir / "aggregate_summary.csv"
    with summary_csv.open("w", newline="", encoding="utf-8") as file:
        fieldnames = list(summary_rows[0].keys())
        writer = csv.DictWriter(file, fieldnames=fieldnames)
        writer.writeheader()
        for row in summary_rows:
            writer.writerow({k: f"{v:.6f}" if isinstance(v, float) else v for k, v in row.items()})

    # Compare the two strategies at the largest n.
    largest_n = max(result.turns for result in results)
    a_group = grouped[("A", largest_n)]
    b_group = grouped[("B", largest_n)]
    a_means = [statistics.mean(r.percentages[i] for r in a_group) for i in range(len(BOARD))]
    b_means = [statistics.mean(r.percentages[i] for r in b_group) for i in range(len(BOARD))]
    differences = [b - a for a, b in zip(a_means, b_means)]
    biggest = sorted(range(len(BOARD)), key=lambda i: abs(differences[i]), reverse=True)[:10]
    summary_json["largest_n_comparison"] = {
        "turns": largest_n,
        "largest_absolute_differences": [
            {
                "index": i,
                "square": BOARD[i][0],
                "strategy_a_mean_percentage": round(a_means[i], 6),
                "strategy_b_mean_percentage": round(b_means[i], 6),
                "difference_b_minus_a": round(differences[i], 6),
            }
            for i in biggest
        ],
        "maximum_absolute_difference": round(max(abs(value) for value in differences), 6),
    }

    with (output_dir / "run_summary.json").open("w", encoding="utf-8") as file:
        json.dump(summary_json, file, indent=2)

    with (output_dir / "run_summary.txt").open("w", encoding="utf-8") as file:
        for strategy in ("A", "B"):
            file.write(f"STRATEGY {strategy}\n")
            for turns in sorted(int(key) for key in summary_json[strategy]):
                file.write(f"  n = {turns:,}\n")
                for item in summary_json[strategy][str(turns)]["top_five"]:
                    file.write(
                        f"    {item['index']:>2} {item['square']:<30} "
                        f"{item['mean_percentage']:.6f}%\n"
                    )
            file.write("\n")
        comparison = summary_json["largest_n_comparison"]
        file.write(f"LARGEST STRATEGY DIFFERENCES AT n = {largest_n:,}\n")
        for item in comparison["largest_absolute_differences"]:
            file.write(
                f"  {item['index']:>2} {item['square']:<30} "
                f"A={item['strategy_a_mean_percentage']:.6f}% "
                f"B={item['strategy_b_mean_percentage']:.6f}% "
                f"B-A={item['difference_b_minus_a']:+.6f} percentage points\n"
            )

    return summary_json


def create_chart(output_dir: Path) -> None:
    """Create extra-credit comparison chart from aggregate_summary.csv."""
    try:
        plt = importlib.import_module("matplotlib.pyplot")
    except ImportError:
        print("matplotlib is not installed; skipping chart creation.")
        return

    rows: list[dict[str, str]] = []
    with (output_dir / "aggregate_summary.csv").open(encoding="utf-8") as file:
        rows.extend(csv.DictReader(file))
    largest_n = max(int(row["turns"]) for row in rows)
    a = [float(row["mean_percentage"]) for row in rows if row["strategy"] == "A" and int(row["turns"]) == largest_n]
    b = [float(row["mean_percentage"]) for row in rows if row["strategy"] == "B" and int(row["turns"]) == largest_n]
    x = list(range(40))
    width = 0.42
    plt.figure(figsize=(15, 7))
    plt.bar([value - width / 2 for value in x], a, width=width, label="Strategy A")
    plt.bar([value + width / 2 for value in x], b, width=width, label="Strategy B")
    plt.xticks(x, [str(i) for i in x])
    plt.xlabel("Board square index")
    plt.ylabel("Average landing percentage")
    plt.title(f"Monopoly Landing Percentages: 10 Runs at n={largest_n:,}")
    plt.legend()
    plt.tight_layout()
    plt.savefig(output_dir / "strategy_comparison_1m.png", dpi=180)
    plt.close()


def run_batch(output_dir: Path, turn_values: list[int], runs: int, base_seed: int) -> list[SimulationResult]:
    results: list[SimulationResult] = []
    output_dir.mkdir(parents=True, exist_ok=True)
    total_jobs = 2 * len(turn_values) * runs
    job = 0

    for strategy_index, strategy in enumerate(("A", "B")):
        for turns in turn_values:
            for run_number in range(1, runs + 1):
                job += 1
                seed = base_seed + strategy_index * 10_000_000 + turns * 10 + run_number
                simulation = MonopolySimulation(strategy, seed)
                counts = simulation.run(turns)
                result = SimulationResult(strategy, run_number, turns, seed, counts)
                results.append(result)
                filename = f"strategy_{strategy}_n_{turns}_run_{run_number:02d}.csv"
                write_dataset(output_dir / filename, result)
                print(f"[{job:02d}/{total_jobs}] wrote {filename}")

    summarize_results(results, output_dir)
    create_chart(output_dir)
    return results


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Run Monopoly movement simulations.")
    parser.add_argument("--output", type=Path, default=Path(__file__).resolve().parents[1] / "data")
    parser.add_argument("--runs", type=int, default=10, help="Independent runs per strategy and n value.")
    parser.add_argument("--seed", type=int, default=2430, help="Base random seed for reproducibility.")
    parser.add_argument(
        "--quick",
        action="store_true",
        help="Use 100, 1,000, 10,000, and 100,000 turns for faster testing.",
    )
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    turns = [100, 1_000, 10_000, 100_000] if args.quick else [1_000, 10_000, 100_000, 1_000_000]
    run_batch(args.output, turns, args.runs, args.seed)
    print(f"\nFinished. Results are in: {args.output.resolve()}")


if __name__ == "__main__":
    main()
