"""Basic validation tests for the Monopoly simulator."""

import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "src"))

from monopoly_simulator import BOARD, MonopolySimulation


def test_board_has_40_squares():
    assert len(BOARD) == 40


def test_count_total_matches_turns():
    simulation = MonopolySimulation("A", seed=123)
    counts = simulation.run(10_000)
    assert len(counts) == 40
    assert sum(counts) == 10_000


def test_both_strategies_run():
    for strategy in ("A", "B"):
        simulation = MonopolySimulation(strategy, seed=456)
        counts = simulation.run(2_000)
        assert sum(counts) == 2_000
        assert all(count >= 0 for count in counts)


def test_go_to_jail_is_not_a_final_landing():
    simulation = MonopolySimulation("A", seed=789)
    counts = simulation.run(100_000)
    assert counts[30] == 0
    assert counts[10] > 0
