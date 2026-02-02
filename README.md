# Cinema Seating System

A menu-driven Java console app that uses a 2D array for cinema seats. **CinemaManager** holds all logic; **CinemaApp** handles the menu and `main`.

## Seat values (spec)

| Value | Meaning    |
|-------|------------|
| 0     | available  |
| 1     | booked     |
| 2     | broken     |
| 3     | VIP        |

- VIP seats exist only in the **middle 4 rows**.
- Display uses **numeric values 0–3**, column indices, and a legend (no symbols like `_`, `R`, `V`).

## Requirements

- Java (JDK 8+)

## Build & run

**Compile:**

```bash
javac CinemaManager.java CinemaApp.java
```

**Run:**

```bash
java CinemaApp
```

You’ll be prompted for **number of rows** and **number of columns**; the grid is then filled randomly (most available, some booked, a few broken, a few VIP in the middle 4 rows).

## Menu options

| Option | Description |
|--------|-------------|
| 1 | Print seating — Numeric grid (0–3), column indices, legend |
| 2 | Show counts and occupancy — Available, booked, broken, VIP counts and occupancy % |
| 3 | Check if a row is usable — At least one available, not all broken, not only booked/VIP |
| 4 | Check if a row can seat a group — Contiguous block (available or VIP), not next to broken |
| 5 | Suggest best row — Row with **most available seats**; tie = lowest row index |
| 6 | Book a group — Enter group size only; books first valid block, then shows updated seating |
| 7 | Exit |

## Classes

- **CinemaManager** — Constructor `(rows, cols)`, random init, count methods, `isRowUsable`, `canSeatGroupInRow`, `suggestBestRow`, `bookGroup(size)`, `printSeating`.
- **CinemaApp** — `main`, menu loop, user input, calls into `CinemaManager`.
