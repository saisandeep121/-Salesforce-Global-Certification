# Java Trigger Files

This folder contains a set of Salesforce Apex **trigger** examples.

## Files in this folder

- `AccountTrigger.apxt`
- `AccountAddressTrigger.apxt`
- `ClosedOpportunityTrigger.apxt`
- `RestrictContactByName.apxt`

## Trigger walkthrough

### 1) `AccountTrigger.apxt`
**Trigger:** `Account` *(before insert)*

**Purpose:** Delegates account creation logic to a handler class.

**What it does:**
- Runs only when:
  - `Trigger.isBefore` and `Trigger.isInsert`
- Calls:
  - `AccountTriggerHandler.CreateAccounts(Trigger.new)`

**Important note:** `AccountTriggerHandler` is **not present** in this folder. This trigger will not compile unless that class exists elsewhere in your Salesforce org/repo.

---

### 2) `AccountAddressTrigger.apxt`
**Trigger:** `Account` *(before insert, before update)*

**Purpose:** Keeps `ShippingPostalCode` aligned to `BillingPostalCode` when the account is configured to match billing address.

**What it does:**
- For each `Account a : Trigger.New`:
  - If `a.Match_Billing_Address__c == true`
  - And `a.BillingPostalCode != null`
  - Then sets:
    - `a.ShippingPostalCode = a.BillingPostalCode`

---

### 3) `ClosedOpportunityTrigger.apxt`
**Trigger:** `Opportunity` *(after insert, after update)*

**Purpose:** Creates a follow-up `Task` for Opportunities whose `StageName` is **Closed Won**.

**What it does:**
- After `insert` and `update`, it queries opportunities among `Trigger.New` where:
  - `StageName = 'Closed Won'`
- For each matching Opportunity, it creates a `Task` with:
  - `Subject = 'Follow Up Test Task'`
  - `WhatId = opp.Id` (links the task to the Opportunity)
- Inserts the task(s) if any were created.

**Deployment note:** Record-creating triggers like this typically require adequate unit tests for successful deployment.

---

### 4) `RestrictContactByName.apxt`
**Trigger:** `Contact` *(before insert, before update)*

**Purpose:** Blocks DML when an invalid last name is used.

**What it does:**
- For each `Contact c : Trigger.New`:
  - If `c.LastName == 'INVALIDNAME'`
  - Then stops the operation via:
    - `c.addError('The Last Name "' + c.LastName + '" is not allowed for DML');`

---

## How to deploy (high-level)
1. Ensure referenced fields exist:
   - `Account.Match_Billing_Address__c`
2. Ensure `AccountTriggerHandler` exists in your org/repo (required for `AccountTrigger.apxt`).
3. Deploy using one of the standard Salesforce methods (SFDX/Salesforce CLI, Metadata API, Change Sets).
4. Run tests (especially for `ClosedOpportunityTrigger`).

