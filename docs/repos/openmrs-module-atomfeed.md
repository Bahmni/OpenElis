# Repository: openmrs-module-atomfeed

*Back to [Integration Plan](../../bahmni-openelis-global2-integration-plan.md)*

**Repo:** `openmrs/openmrs-module-atomfeed`
**Local path:** `/Users/vishalkarmalkar/IdeaProjects/bahmni/openelismigration/openmrs-module-atomfeed`
**Deep dive date:** 2026-02-19
**Changes required:** None — not used in new flow

---

## Role in Integration

**Not used in the new FHIR-based flow.** This module is the AtomFeed framework that underpins the *existing* Bahmni–OpenELIS integration. It is documented here to explain why the new mediator does not use it and to confirm backward compatibility.

---

## What This Module Does

`openmrs-module-atomfeed` provides:
- An AtomFeed event publishing framework for OpenMRS modules to publish events to an Atom-format feed
- An AtomFeed client framework for consumers (like the old OpenELIS) to subscribe to those feeds
- Cursor/marker management so consumers can resume from where they left off

In the current Bahmni–OpenELIS integration:
- `bahmni-core`'s `openmrs-elis-atomfeed-client-omod` **consumes** the old OpenELIS's AtomFeed (results/accessions)
- The old OpenELIS **consumes** OpenMRS encounter feeds to detect new lab orders

---

## Verified Finding: AtomFeed IS Published Outbound for Encounter/Order Events

**Correction from diagram analysis (2026-02-19):** The lab order flow diagram confirms OpenMRS publishes encounter/order events outbound via this module, and old OpenELIS polls them. Both directions are active:

| Feed | Direction | Publisher | Consumer |
|---|---|---|---|
| Encounter/order feed | OpenMRS → external | `openmrs-module-atomfeed` | old OpenELIS (currently), new mediator (potentially) |
| Accession/result feed | old OpenELIS → OpenMRS | old OpenELIS | `bahmni-core`'s elis client |

The mediator subscribes to the encounter feed from this module using the `org.ict4h.atomfeed.client` library (same as pacs-integration). The feed already exists — no changes to this module needed. Feed URL: `http://openmrs:8080/openmrs/ws/atomfeed/encounter/recent`.

---

## Backward Compatibility

The existing AtomFeed integration continues to work independently. Deployments that still run the old OpenELIS are unaffected. The new mediator operates alongside the old system without interference — it uses different APIs (REST + FHIR) and does not touch AtomFeed infrastructure.

---

## Changes Required

**None.** This module is not modified and not part of the new integration flow.
