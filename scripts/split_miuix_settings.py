from __future__ import annotations

import re
from pathlib import Path


SOURCE = Path("app/src/main/java/h/Hchat/ui/miuix/MiuixSettingsPage.kt")
PART_NAMES = [
    "MiuixSettingsPagePart1.kt",
    "MiuixSettingsPagePart2.kt",
    "MiuixSettingsPagePart3.kt",
    "MiuixSettingsPagePart4.kt",
    "MiuixSettingsPagePart5.kt",
]

# Stable top-level declarations used as split boundaries. Finding declarations
# by text keeps the script usable after settings pages are added or removed.
SPLIT_DECLARATIONS = [
    "fun <T> SettingsRouteTransition(",
    "enum class ZombieCheckSelection",
    "fun FeatureListCard(",
    "object PluginMarketHistoryUi",
    "fun WeChatTabletMiuixPage(",
]


def expose_top_level_symbols(text: str) -> str:
    # Only column-zero declarations are changed. Nested private members remain private.
    return re.sub(r"(?m)^private ", "internal ", text)


def main() -> None:
    raw = SOURCE.read_text(encoding="utf-8")
    # Re-running after a previous split reconstructs the source by removing the
    # repeated import header from every generated part.
    existing_parts = [Path(SOURCE.parent, name) for name in PART_NAMES]
    if all(part.exists() for part in existing_parts):
        main_lines = raw.splitlines(keepends=True)
        first_decl = next(
            index for index, line in enumerate(main_lines)
            if line.startswith(("private const val UI_PREFS_NAME", "internal const val UI_PREFS_NAME"))
        )
        header = "".join(main_lines[:first_decl])
        bodies = ["".join(main_lines[first_decl:])]
        for part in existing_parts:
            part_lines = part.read_text(encoding="utf-8").splitlines(keepends=True)
            bodies.append("".join(part_lines[first_decl:]))
        raw = header + "".join(bodies)
    lines = raw.splitlines(keepends=True)
    first_decl = next(
        index for index, line in enumerate(lines)
        if line.startswith(("private const val UI_PREFS_NAME", "internal const val UI_PREFS_NAME"))
    )
    starts = []
    for declaration in SPLIT_DECLARATIONS:
        start = next(
            index for index, line in enumerate(lines)
            if line.removeprefix("private ").removeprefix("internal ").startswith(declaration)
        )
        while start > first_decl and lines[start - 1].lstrip().startswith("@"):
            start -= 1
        starts.append(start)
    if starts != sorted(starts) or starts[0] <= first_decl or starts[-1] >= len(lines):
        raise SystemExit("invalid split boundaries")

    header = "".join(lines[:first_decl])
    shared = "".join(lines[first_decl:starts[0]])
    SOURCE.write_text(
        expose_top_level_symbols(header + shared),
        encoding="utf-8",
        newline="",
    )

    boundaries = starts + [len(lines)]
    for name, start, end in zip(PART_NAMES, starts, boundaries[1:]):
        part = Path(SOURCE.parent, name)
        part.write_text(
            expose_top_level_symbols(header + "".join(lines[start:end])),
            encoding="utf-8",
            newline="",
        )


if __name__ == "__main__":
    main()
