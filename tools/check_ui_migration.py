#!/usr/bin/env python3
"""Report active XML layouts and their Java/Kotlin references.

This intentionally reports instead of deleting: active layouts can back login,
installers, controls, dialogs, and native surfaces.
"""
from pathlib import Path
import re

root = Path(__file__).resolve().parents[1]
res = root / "app_pojavlauncher/src/main/res"
src = root / "app_pojavlauncher/src/main/java"
source = "\n".join(p.read_text(errors="ignore") for p in src.rglob("*.java"))
source += "\n" + "\n".join(p.read_text(errors="ignore") for p in src.rglob("*.kt"))

rows = []
layout_files = list((res / "layout").glob("*.xml")) + list((res / "layout-land").glob("*.xml"))
for layout in sorted(layout_files):
    name = layout.stem
    refs = len(re.findall(rf"R\.layout\.{re.escape(name)}|@layout/{re.escape(name)}", source))
    rows.append((name, refs, layout.relative_to(root)))

print(f"XML layouts: {len(rows)}")
print("refs\tlayout")
for name, refs, path in rows:
    print(f"{refs}\t{path}")
print("\nZero-reference layouts are candidates for review, not automatic deletion.")
print("Delete only after the Compose parity gate in docs/UI-MIGRATION-MATRIX-0.0.0.3.md.")
