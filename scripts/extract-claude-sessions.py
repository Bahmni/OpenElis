#!/usr/bin/env python3
"""
Extract Claude Code session data for blog/reporting purposes.

Parses JSONL conversation files from ~/.claude/projects/ and produces
a markdown report with: prompts, models, token usage, costs, and durations.

Pricing source: https://platform.claude.com/docs/en/about-claude/pricing
"""

import json
import sys
import os
import glob
from datetime import datetime, timezone
from collections import defaultdict
from pathlib import Path

# ---------------------------------------------------------------------------
# Official Anthropic pricing (USD per million tokens)
# https://platform.claude.com/docs/en/about-claude/pricing
# ---------------------------------------------------------------------------
PRICING = {
    # model-id substring -> { input, output, cache_write_5m, cache_read }
    "claude-opus-4-6":   {"input": 5.00,  "output": 25.00, "cache_write_5m": 6.25,  "cache_read": 0.50},
    "claude-opus-4-5":   {"input": 5.00,  "output": 25.00, "cache_write_5m": 6.25,  "cache_read": 0.50},
    "claude-opus-4-1":   {"input": 15.00, "output": 75.00, "cache_write_5m": 18.75, "cache_read": 1.50},
    "claude-opus-4-0":   {"input": 15.00, "output": 75.00, "cache_write_5m": 18.75, "cache_read": 1.50},
    "claude-sonnet-4-6": {"input": 3.00,  "output": 15.00, "cache_write_5m": 3.75,  "cache_read": 0.30},
    "claude-sonnet-4-5": {"input": 3.00,  "output": 15.00, "cache_write_5m": 3.75,  "cache_read": 0.30},
    "claude-sonnet-4-0": {"input": 3.00,  "output": 15.00, "cache_write_5m": 3.75,  "cache_read": 0.30},
    "claude-sonnet-3-7": {"input": 3.00,  "output": 15.00, "cache_write_5m": 3.75,  "cache_read": 0.30},
    "claude-haiku-4-5":  {"input": 1.00,  "output": 5.00,  "cache_write_5m": 1.25,  "cache_read": 0.10},
    "claude-haiku-3-5":  {"input": 0.80,  "output": 4.00,  "cache_write_5m": 1.00,  "cache_read": 0.08},
    "claude-haiku-3":    {"input": 0.25,  "output": 1.25,  "cache_write_5m": 0.30,  "cache_read": 0.03},
}


def lookup_pricing(model_id: str) -> dict:
    """Match a model ID string to its pricing entry."""
    if not model_id:
        return PRICING["claude-opus-4-6"]  # default fallback
    for key in PRICING:
        if key in model_id:
            return PRICING[key]
    return PRICING["claude-opus-4-6"]


def cost_usd(usage: dict, model_id: str) -> float:
    """Calculate USD cost from a usage dict and model ID."""
    p = lookup_pricing(model_id)
    input_tokens = usage.get("input_tokens", 0)
    output_tokens = usage.get("output_tokens", 0)
    cache_creation = usage.get("cache_creation_input_tokens", 0)
    cache_read = usage.get("cache_read_input_tokens", 0)

    cost = (
        (input_tokens / 1_000_000) * p["input"]
        + (output_tokens / 1_000_000) * p["output"]
        + (cache_creation / 1_000_000) * p["cache_write_5m"]
        + (cache_read / 1_000_000) * p["cache_read"]
    )
    return cost


def extract_user_text(message: dict) -> str:
    """Pull readable text from a user message."""
    content = message.get("content", "")
    if isinstance(content, list):
        parts = []
        for c in content:
            if isinstance(c, dict) and c.get("type") == "text":
                parts.append(c.get("text", ""))
            elif isinstance(c, str):
                parts.append(c)
        return " ".join(parts)
    return str(content)


def is_real_user_prompt(record: dict) -> bool:
    """Filter to only genuine human prompts (not meta/tool-results/commands)."""
    if record.get("isMeta"):
        return False
    if record.get("toolUseResult"):
        return False
    if record.get("isCompactSummary"):
        return False
    text = extract_user_text(record.get("message", {})).strip()
    if not text or len(text) < 3:
        return False
    # Skip internal commands and task notifications
    if text.startswith("<local-command"):
        return False
    if text.startswith("<task-notification"):
        return False
    if text.startswith("<command-name>"):
        return False
    # Skip interruptions
    if "[Request interrupted by user" in text:
        return False
    return True


def clean_prompt_text(text: str) -> str:
    """Remove XML tags and clean up prompt text for display."""
    import re
    # Remove system-reminder blocks
    text = re.sub(r'<system-reminder>.*?</system-reminder>', '', text, flags=re.DOTALL)
    # Remove other XML-ish tags but keep content
    text = re.sub(r'<[^>]+>', '', text)
    text = text.strip()
    # Truncate very long prompts
    if len(text) > 500:
        text = text[:497] + "..."
    return text


def format_duration(ms: int) -> str:
    """Format milliseconds to human-readable duration."""
    seconds = ms / 1000
    if seconds < 60:
        return f"{seconds:.0f}s"
    minutes = seconds / 60
    if minutes < 60:
        remaining_s = seconds % 60
        return f"{int(minutes)}m {int(remaining_s)}s"
    hours = minutes / 60
    remaining_m = minutes % 60
    return f"{int(hours)}h {int(remaining_m)}m"


def format_tokens(n: int) -> str:
    """Format token count with K/M suffix."""
    if n >= 1_000_000:
        return f"{n/1_000_000:.1f}M"
    if n >= 1_000:
        return f"{n/1_000:.1f}K"
    return str(n)


def parse_session(filepath: str) -> dict:
    """Parse a single JSONL session file and return structured data."""
    records = []
    with open(filepath, "r", encoding="utf-8", errors="replace") as f:
        for line in f:
            line = line.strip()
            if not line:
                continue
            try:
                records.append(json.loads(line))
            except json.JSONDecodeError:
                continue

    if not records:
        return None

    session_id = Path(filepath).stem
    turns = []          # list of (prompt, response_data) pairs
    turn_durations = [] # from system/turn_duration records

    # Collect all records by type
    user_records = []
    assistant_records = []
    system_records = []

    for r in records:
        rtype = r.get("type")
        if rtype == "user" and is_real_user_prompt(r):
            user_records.append(r)
        elif rtype == "assistant":
            assistant_records.append(r)
        elif rtype == "system" and r.get("subtype") == "turn_duration":
            duration_ms = r.get("durationMs", 0)
            if duration_ms:
                turn_durations.append(duration_ms)

    # Pair user prompts with the assistant responses that follow
    # Build a timeline of all user + assistant records sorted by timestamp
    timeline = []
    for r in user_records:
        timeline.append(("user", r))
    for r in assistant_records:
        timeline.append(("assistant", r))

    timeline.sort(key=lambda x: x[1].get("timestamp", ""))

    # Walk through timeline, pairing each user prompt with subsequent assistant responses
    current_prompt = None
    current_prompt_ts = None
    current_responses = []

    for role, record in timeline:
        if role == "user":
            # Save previous turn if exists
            if current_prompt is not None:
                turns.append({
                    "prompt": current_prompt,
                    "timestamp": current_prompt_ts,
                    "responses": current_responses,
                })
            text = clean_prompt_text(extract_user_text(record.get("message", {})))
            current_prompt = text
            current_prompt_ts = record.get("timestamp", "")
            current_responses = []
        elif role == "assistant" and current_prompt is not None:
            msg = record.get("message", {})
            current_responses.append({
                "model": msg.get("model", "unknown"),
                "usage": msg.get("usage", {}),
                "timestamp": record.get("timestamp", ""),
            })

    # Don't forget the last turn
    if current_prompt is not None:
        turns.append({
            "prompt": current_prompt,
            "timestamp": current_prompt_ts,
            "responses": current_responses,
        })

    if not turns:
        return None

    # Compute session-level aggregates
    total_input = 0
    total_output = 0
    total_cache_write = 0
    total_cache_read = 0
    total_cost = 0.0
    models_used = set()
    api_calls = 0

    for turn in turns:
        for resp in turn["responses"]:
            usage = resp.get("usage", {})
            model = resp.get("model", "unknown")
            models_used.add(model)
            total_input += usage.get("input_tokens", 0)
            total_output += usage.get("output_tokens", 0)
            total_cache_write += usage.get("cache_creation_input_tokens", 0)
            total_cache_read += usage.get("cache_read_input_tokens", 0)
            total_cost += cost_usd(usage, model)
            api_calls += 1

    # Session time range
    first_ts = turns[0].get("timestamp", "")
    last_ts = turns[-1].get("timestamp", "")

    total_duration_ms = sum(turn_durations) if turn_durations else 0

    return {
        "session_id": session_id,
        "filepath": filepath,
        "first_timestamp": first_ts,
        "last_timestamp": last_ts,
        "turns": turns,
        "models_used": sorted(models_used),
        "total_input_tokens": total_input,
        "total_output_tokens": total_output,
        "total_cache_write_tokens": total_cache_write,
        "total_cache_read_tokens": total_cache_read,
        "total_cost_usd": total_cost,
        "api_calls": api_calls,
        "total_duration_ms": total_duration_ms,
        "turn_durations": turn_durations,
    }


def format_timestamp(ts_str: str) -> str:
    """Format ISO timestamp to readable form."""
    if not ts_str:
        return "N/A"
    try:
        dt = datetime.fromisoformat(ts_str.replace("Z", "+00:00"))
        return dt.strftime("%Y-%m-%d %H:%M:%S UTC")
    except (ValueError, TypeError):
        return ts_str


def generate_report(sessions: list, output_path: str):
    """Generate a markdown report from parsed sessions."""
    # Sort sessions by first timestamp
    sessions.sort(key=lambda s: s["first_timestamp"])

    grand_total_cost = sum(s["total_cost_usd"] for s in sessions)
    grand_total_input = sum(s["total_input_tokens"] for s in sessions)
    grand_total_output = sum(s["total_output_tokens"] for s in sessions)
    grand_total_cache_write = sum(s["total_cache_write_tokens"] for s in sessions)
    grand_total_cache_read = sum(s["total_cache_read_tokens"] for s in sessions)
    grand_total_api_calls = sum(s["api_calls"] for s in sessions)
    grand_total_prompts = sum(len(s["turns"]) for s in sessions)
    grand_total_duration_ms = sum(s["total_duration_ms"] for s in sessions)
    all_models = set()
    for s in sessions:
        all_models.update(s["models_used"])
    # Filter out internal/synthetic model IDs
    all_models = {m for m in all_models if not m.startswith("<") and m != "unknown"}

    lines = []
    lines.append("# Claude Code Session Report")
    lines.append(f"")
    lines.append(f"**Generated:** {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    lines.append(f"**Project:** Bahmni OpenELIS Migration Analysis")
    lines.append("")

    # Executive summary
    lines.append("## Executive Summary")
    lines.append("")
    lines.append(f"| Metric | Value |")
    lines.append(f"|--------|-------|")
    lines.append(f"| Total Sessions | {len(sessions)} |")
    lines.append(f"| Total User Prompts | {grand_total_prompts} |")
    lines.append(f"| Total API Calls (turns) | {grand_total_api_calls} |")
    lines.append(f"| Models Used | {', '.join(sorted(all_models))} |")
    lines.append(f"| Total Input Tokens | {format_tokens(grand_total_input)} |")
    lines.append(f"| Total Output Tokens | {format_tokens(grand_total_output)} |")
    lines.append(f"| Total Cache Write Tokens | {format_tokens(grand_total_cache_write)} |")
    lines.append(f"| Total Cache Read Tokens | {format_tokens(grand_total_cache_read)} |")
    lines.append(f"| Total Wall-Clock Time | {format_duration(grand_total_duration_ms) if grand_total_duration_ms else 'N/A'} |")
    lines.append(f"| **Total Estimated Cost** | **${grand_total_cost:.2f}** |")
    lines.append("")

    # Pricing reference
    lines.append("## Pricing Reference")
    lines.append("")
    lines.append("Costs calculated using [official Anthropic pricing](https://platform.claude.com/docs/en/about-claude/pricing):")
    lines.append("")
    lines.append("| Model | Input | Output | Cache Write (5m) | Cache Read |")
    lines.append("|-------|-------|--------|-----------------|------------|")
    for model in sorted(all_models):
        p = lookup_pricing(model)
        lines.append(f"| {model} | ${p['input']}/MTok | ${p['output']}/MTok | ${p['cache_write_5m']}/MTok | ${p['cache_read']}/MTok |")
    lines.append("")

    # Per-session detail
    lines.append("---")
    lines.append("")
    for idx, session in enumerate(sessions, 1):
        lines.append(f"## Session {idx}: `{session['session_id'][:8]}...`")
        lines.append("")
        lines.append(f"| Detail | Value |")
        lines.append(f"|--------|-------|")
        lines.append(f"| Started | {format_timestamp(session['first_timestamp'])} |")
        lines.append(f"| Ended | {format_timestamp(session['last_timestamp'])} |")
        session_models = [m for m in session['models_used'] if not m.startswith("<") and m != "unknown"]
        lines.append(f"| Model(s) | {', '.join(session_models) if session_models else 'N/A'} |")
        lines.append(f"| User Prompts | {len(session['turns'])} |")
        lines.append(f"| API Calls | {session['api_calls']} |")
        lines.append(f"| Input Tokens | {format_tokens(session['total_input_tokens'])} |")
        lines.append(f"| Output Tokens | {format_tokens(session['total_output_tokens'])} |")
        lines.append(f"| Cache Write | {format_tokens(session['total_cache_write_tokens'])} |")
        lines.append(f"| Cache Read | {format_tokens(session['total_cache_read_tokens'])} |")
        if session['total_duration_ms']:
            lines.append(f"| Wall-Clock Time | {format_duration(session['total_duration_ms'])} |")
        lines.append(f"| **Session Cost** | **${session['total_cost_usd']:.2f}** |")
        lines.append("")

        # List prompts
        lines.append("### Prompts")
        lines.append("")
        for tidx, turn in enumerate(session["turns"], 1):
            ts = format_timestamp(turn["timestamp"])
            prompt = turn["prompt"]
            # Aggregate token usage for this turn
            turn_input = sum(r["usage"].get("input_tokens", 0) for r in turn["responses"])
            turn_output = sum(r["usage"].get("output_tokens", 0) for r in turn["responses"])
            turn_cost = sum(cost_usd(r["usage"], r["model"]) for r in turn["responses"])
            turn_models = {r["model"] for r in turn["responses"]
                          if not r["model"].startswith("<") and r["model"] != "unknown"}

            lines.append(f"**{tidx}.** [{ts}]")
            if turn_models:
                lines.append(f"   Model: `{'`, `'.join(sorted(turn_models))}`"
                             f" | In: {format_tokens(turn_input)}"
                             f" | Out: {format_tokens(turn_output)}"
                             f" | Cost: ${turn_cost:.4f}")
            lines.append(f"")
            lines.append(f"> {prompt}")
            lines.append("")

        lines.append("---")
        lines.append("")

    # Write report
    report = "\n".join(lines)
    with open(output_path, "w", encoding="utf-8") as f:
        f.write(report)

    return report


def main():
    # Default project directory for this repo
    project_dir = os.path.expanduser(
        "~/.claude/projects/-Users-vishalkarmalkar-IdeaProjects-bahmni-OpenElis"
    )

    # Allow override via CLI arg
    if len(sys.argv) > 1:
        project_dir = sys.argv[1]

    if not os.path.isdir(project_dir):
        print(f"Error: Project directory not found: {project_dir}")
        sys.exit(1)

    # Find all JSONL session files
    jsonl_files = glob.glob(os.path.join(project_dir, "*.jsonl"))
    if not jsonl_files:
        print(f"No .jsonl files found in {project_dir}")
        sys.exit(1)

    print(f"Found {len(jsonl_files)} session file(s) in {project_dir}")

    sessions = []
    for filepath in sorted(jsonl_files):
        session = parse_session(filepath)
        if session:
            sessions.append(session)
            print(f"  Parsed: {Path(filepath).stem[:8]}... "
                  f"({len(session['turns'])} prompts, "
                  f"${session['total_cost_usd']:.2f})")
        else:
            print(f"  Skipped: {Path(filepath).stem[:8]}... (no user prompts)")

    if not sessions:
        print("No sessions with user prompts found.")
        sys.exit(1)

    # Output report
    output_path = os.path.join(
        os.path.dirname(os.path.dirname(os.path.abspath(__file__))),
        "docs", "claude-session-report.md"
    )
    os.makedirs(os.path.dirname(output_path), exist_ok=True)

    report = generate_report(sessions, output_path)
    print(f"\nReport written to: {output_path}")

    # Also print summary to stdout
    total_cost = sum(s["total_cost_usd"] for s in sessions)
    total_prompts = sum(len(s["turns"]) for s in sessions)
    print(f"\n{'='*50}")
    print(f"  Sessions: {len(sessions)}")
    print(f"  Prompts:  {total_prompts}")
    print(f"  Cost:     ${total_cost:.2f}")
    print(f"{'='*50}")


if __name__ == "__main__":
    main()
