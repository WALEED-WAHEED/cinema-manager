# Cinema Seating System

A menu-driven Java console application that uses a 2D array to represent cinema seats. It supports availability checking, VIP seating, and group bookings. Designed for high school/college level Java.

## Features

- **Seating layout** — 5 rows × 8 seats; rows 0 and 1 are VIP rows
- **Display seats** — View the current layout (`_` = available, `R` = regular booked, `V` = VIP booked)
- **Row analysis** — See available/booked counts and occupancy per row and overall
- **Group suggestions** — Get the best row for a group (prefers center rows with contiguous seats)
- **Group booking** — Book a block of seats in a row with optional VIP

## Requirements

- Java (JDK 8 or later)

## Build & Run

**Compile:**

```bash
javac CinemaSeatingSystem.java
```

**Run:**

```bash
java CinemaSeatingSystem
```

## Menu Options

| Option | Description |
|--------|-------------|
| 1 | Display seats — Show the current seating layout |
| 2 | Analyze rows — Show occupancy stats per row and total |
| 3 | Suggest best row — Enter group size to get suggested row and seat range |
| 4 | Book a group — Enter row, start seat, group size, and VIP (y/n) |
| 5 | Exit |

## Seat Layout

- **Rows 0–1:** VIP rows  
- **Rows 2–4:** Regular rows  
- Seats are numbered by column (0–7) within each row.

## License

Side project — use as you like.
