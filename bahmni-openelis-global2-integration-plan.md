# Bahmni + OpenELIS-Global-2: Integration Plan

**Date:** 2026-02-17 (Updated: 2026-02-18)
**Status:** Draft
**Objective:** Replace Bahmni's OpenELIS fork with OpenELIS-Global-2 (OE-Global-2), integrated via FHIR.

**Key references:**
- [Reference implementation: `DIGI-UW/openelis-openmrs-hie`](https://github.com/DIGI-UW/openelis-openmrs-hie) — working Docker Compose stack
- [OE-Global-2 + OpenMRS FHIR integration discussion](https://talk.openelis-global.org/t/integration-with-openmrs-over-fhir/1702) (Angshuman + Moses Mutesasira)
- [OE-Global-2 test method selection](https://talk.openelis-global.org/t/openelis-global-capability-for-selecting-a-specific-method-for-a-given-order/1691)
- [`openmrs-module-labonfhir`](https://github.com/openmrs/openmrs-module-labonfhir) — the critical OpenMRS module that creates FHIR Tasks

**Detail pages:** [Current Flow](docs/current-flow-detail.md) | [Proposed Flow](docs/proposed-flow-detail.md) | [Architecture](docs/architecture-detail.md) | [Q&A](docs/integration-qa-detail.md)

---

## 1. Context

Bahmni ships a fork of OpenELIS (v3.1, circa 2013) integrated with OpenMRS via AtomFeed — a custom polling mechanism. OpenELIS-Global-2 is the actively maintained successor with native FHIR R4 support. We are adopting it **as-is** — no code porting, no forking.

**The work is integration:** making OE-Global-2 and Bahmni's OpenMRS exchange lab orders, results, and reference data via FHIR.

---

## 2. Current vs Proposed: At a Glance

| Aspect | Current (AtomFeed) | Proposed (FHIR) |
|---|---|---|
| **Order creation** | AtomFeed event → REST fetch | Lab on FHIR module creates FHIR Task + ServiceRequest, pushes to SHR |
| **Order pickup** | OpenELIS polls AtomFeed (5s) | OE-Global-2 polls SHR for FHIR Tasks (20s-2min) |
| **Test matching** | Custom code mapping | LOINC code lookup |
| **Result return** | AtomFeed event → REST fetch | FHIR DiagnosticReport pushed to SHR |
| **Result pickup** | OpenMRS polls AtomFeed (15s) | Lab on FHIR polls SHR for completed Tasks |
| **Routing/auth** | Direct HTTP | OpenHIM proxy (basic auth, routing, audit) |
| **Lab UI** | Struts/JSP (2013 vintage) | React |
| **Integration standard** | Atom RFC 4287 (custom) | HL7 FHIR R4 |
| **Key OpenMRS modules** | `openmrs-module-atomfeed`, `bahmni-core` | **`openmrs-module-labonfhir`** (v1.5.3+), `openmrs-module-fhir2` |
| **Containers** | 2 (app + db) | ~12 new (OE-Global-2 + OpenHIM + SHR) |

**What stays the same:** Bahmni UI order entry, lab work in LIS, Bahmni UI result display, Odoo billing.

Detail: [Current Flow](docs/current-flow-detail.md) | [Proposed Flow](docs/proposed-flow-detail.md) | [Architecture](docs/architecture-detail.md)

---

## 3. How It Works (simplified)

```
DOCTOR (Bahmni)                    NOTICEBOARD (SHR)                LAB (OE-Global-2)
      |                                    |                              |
      |-- Lab on FHIR pushes Task -------->|                              |
      |   (FHIR bundle: Task +            |                              |
      |    ServiceRequest + Patient)       |<-- polls every 20s ---------|
      |                                    |-- returns Task + Patient --->|
      |                                    |                    (matches LOINC code,
      |                                    |                     does lab work)
      |                                    |<-- pushes DiagnosticReport --|
      |<-- Lab on FHIR polls results ------|                              |
      |                                    |                              |
   (shows result                    OpenHIM guards                  OE-Global-2
    in Bahmni UI)                    the door                        React UI
```

The critical module is **`openmrs-module-labonfhir`** — it's the active bridge between OpenMRS and the lab. The FHIR2 module is a passive API layer; Lab on FHIR is the one that **reacts** to new orders and **pushes** them out.

**Lab on FHIR is NOT currently in Bahmni's OpenMRS distribution.** Adding it is the first thing to validate.

Detail: [Proposed Flow — module roles, config, technical detail](docs/proposed-flow-detail.md)

---

## 4. Key Findings

| Finding | Source | Impact |
|---|---|---|
| FHIR2 alone doesn't create Tasks — **Lab on FHIR module is required** | [Community discussion](https://talk.openelis-global.org/t/integration-with-openmrs-over-fhir/1702) | Must add `openmrs-module-labonfhir` to Bahmni |
| Reference architecture uses **OpenHIM + SHR** as intermediaries (not direct connection) | [Reference impl](https://github.com/DIGI-UW/openelis-openmrs-hie) | Adds ~6 containers; may be simplifiable |
| Exchange is **purely FHIR** — no HL7v2 | [Moses Mutesasira](https://talk.openelis-global.org/t/integration-with-openmrs-over-fhir/1702/2) | Clean integration path |
| OE-Global-2 matches tests by **LOINC codes only** | Code analysis | Bahmni test catalog needs LOINC codes |
| Test methods (PCR, culture, etc.) selected at **execution time** by lab tech, not at order time | [Community discussion](https://talk.openelis-global.org/t/openelis-global-capability-for-selecting-a-specific-method-for-a-given-order/1691) | LOINC mapping is test-level, not method-level |
| A **working reference implementation** exists with Docker Compose | [DIGI-UW/openelis-openmrs-hie](https://github.com/DIGI-UW/openelis-openmrs-hie) | Phase 1 starts from this, not from scratch |
| Lab on FHIR detects orders via **JMS events** (instant), returns results via **polling** | Code analysis | Event-driven outbound, poll-based inbound |

Detail: [Full Q&A with technical depth](docs/integration-qa-detail.md)

---

## 5. Open Questions

| # | Question | Blocks | Owner |
|---|---|---|---|
| 1 | Can `openmrs-module-labonfhir` (v1.5.3) be added to Bahmni's OpenMRS distribution? Any module conflicts or version incompatibilities? | **Critical — blocks everything** | Angshuman Sarkar |
| 2 | Can the architecture be simplified for Bahmni? Can we skip OpenHIM + SHR and have OE-Global-2 talk directly to OpenMRS FHIR2? The reference impl adds ~6 containers for this layer. | Phase 1 | Angshuman Sarkar + team |
| 3 | Does the current Bahmni test catalog have LOINC codes? How many tests need mapping? | Phase 2 | SME |
| 4 | Where should the test catalog be mastered — OpenMRS or OE-Global-2? | Phase 2 | Team decision |
| 5 | Is standalone patient sync needed, or is patient-on-demand (via Task context) sufficient? | Phase 1 | SME |

---

## 6. Plan

### Phase 1: Proof of Concept (2-3 weeks)

**Goal:** Validate the FHIR integration end-to-end, then assess Bahmni-specific gaps.

**Step 1a: Run the reference implementation as-is**
- [ ] Spin up [`DIGI-UW/openelis-openmrs-hie`](https://github.com/DIGI-UW/openelis-openmrs-hie) (`docker-compose up -d`)
- [ ] Place a lab order in OpenMRS 3 → confirm it appears in OE-Global-2
- [ ] Enter and validate a result in OE-Global-2 → confirm DiagnosticReport reaches OpenMRS
- [ ] Observe the full Task lifecycle: REQUESTED → ACCEPTED → IN_PROGRESS → COMPLETED

**Step 1b: Assess Bahmni-specific gaps (answers open questions 1-2)**
- [ ] Determine if `openmrs-module-labonfhir` can be added to Bahmni's OpenMRS without conflicts
- [ ] Evaluate if OpenHIM + SHR are required, or if a simplified direct connection works
- [ ] Involve **Angshuman Sarkar** for OpenMRS-side assessment

### Phase 2: Test Catalog + LOINC (2-3 weeks)

*(Contingent on open questions 3 and 4.)*

- [ ] Audit Bahmni test catalog for LOINC code coverage
- [ ] Map tests without LOINC codes to LOINC
- [ ] Create CSV configuration files for OE-Global-2
- [ ] Validate order matching end-to-end

### Phase 3: Master Data + Deployment (2-3 weeks)

- [ ] Configure master data (result ranges, organizations, providers, users)
- [ ] Integrate OE-Global-2 containers into Bahmni Docker Compose stack
- [ ] Configure networking, proxy, SSL, authentication

### Phase 4: End-to-End Testing (2-3 weeks)

- [ ] Full lab workflow testing (order → sample → result → validation → report → display)
- [ ] Edge cases: rejected samples, amended results, cancelled orders
- [ ] User acceptance testing with lab technicians
- [ ] Verify Odoo billing integration is unaffected

### Phase 5: Go-Live (1 week)

- [ ] Deploy to production (fresh install, no data migration)
- [ ] Monitor for issues during initial operation period

**Total: 10-14 weeks** *(pending resolution of open questions)*

### Future: Data Migration Tooling

Existing Bahmni installations will need a data migration path when OE-Global-2 becomes the standard. Can be planned independently.

---

## 7. Community References

| Source | Link | Key Insight |
|---|---|---|
| **FHIR integration discussion** | [talk.openelis-global.org/t/1702](https://talk.openelis-global.org/t/integration-with-openmrs-over-fhir/1702) | Lab on FHIR + FHIR2 needed; purely FHIR; OpenHIM for auth |
| **Test method selection** | [talk.openelis-global.org/t/1691](https://talk.openelis-global.org/t/openelis-global-capability-for-selecting-a-specific-method-for-a-given-order/1691) | Method selection at execution time; parent/child test pattern |
| **Reference implementation** | [github.com/DIGI-UW/openelis-openmrs-hie](https://github.com/DIGI-UW/openelis-openmrs-hie) | Working Docker Compose with OpenMRS 3 + OE-Global-2 + OpenHIM + SHR |
| **Lab on FHIR module** | [github.com/openmrs/openmrs-module-labonfhir](https://github.com/openmrs/openmrs-module-labonfhir) | Creates FHIR Task + ServiceRequest; pushes to LIS |

---

*Detail pages: [Current Flow](docs/current-flow-detail.md) | [Proposed Flow](docs/proposed-flow-detail.md) | [Architecture](docs/architecture-detail.md) | [Q&A](docs/integration-qa-detail.md)*

*Archived analysis documents with detailed code inventory available in [archive/](archive/) for reference.*
