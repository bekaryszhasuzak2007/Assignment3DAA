
import csv
import os
import matplotlib.pyplot as plt

input_file = "results/results.csv"
output_dir = "results/plots"

os.makedirs(output_dir, exist_ok=True)

with open(input_file, newline="", encoding="utf-8") as file:
    data = list(csv.DictReader(file))

algorithms = sorted(set(row["algorithm"] for row in data))

for x_key, x_label, filename in [
    ("vertices", "Number of vertices", "execution_time_vs_vertices.png"),
    ("edges", "Number of edges", "execution_time_vs_edges.png")
]:
    plt.figure()

    for algorithm in algorithms:
        rows = [
            row for row in data
            if row["algorithm"] == algorithm
        ]

        rows.sort(key=lambda row: int(row[x_key]))

        x_values = [int(row[x_key]) for row in rows]
        y_values = [
            int(row["averageTimeNs"]) / 1_000_000
            for row in rows
        ]

        plt.plot(x_values, y_values, marker="o", label=algorithm)

    plt.xlabel(x_label)
    plt.ylabel("Average execution time (ms)")
    plt.title("Algorithm execution time comparison")
    plt.legend()
    plt.grid(True)
    plt.tight_layout()
    plt.savefig(os.path.join(output_dir, filename), dpi=150)
    plt.close()

print("Plots saved to results/plots/")
