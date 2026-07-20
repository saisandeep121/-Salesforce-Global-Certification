# Day 2 - Salesforce Certification Using Apex Class

This folder contains Apex code used to practice and complete parts of the Salesforce Certification journey—focused on **APEX Triggers** and related basics.

## Progress (Certification Track)

- ✅ **APEX Triggers** — Completed
- ✅ **APEX Testing** — Completed
- ✅ **APEX Basic & Databases** — Completed
- ✅ **APEX .NET basics** — Completed (learning/knowledge notes)

## What’s in this project

### Apex Triggers

1. **`AccountTrigger.apxt`**  
   - **Timing:** `before insert`  
   - **Behavior:** Delegates account creation logic to a handler:
     - Calls `AccountTriggerHandler.CreateAccounts(Trigger.new)`

2. **`AccountAddressTrigger.apxt`**  
   - **Timing:** `before insert`, `before update`  
   - **Behavior:** Copies Billing address postal code to Shipping postal code when conditions match:
     - If `Match_Billing_Address__c == true` and `BillingPostalCode != null`, then sets:
       - `ShippingPostalCode = BillingPostalCode`

3. **`ClosedOpportunityTrigger.apxt`**  
   - **Timing:** `after insert`, `after update`  
   - **Behavior:** For opportunities with `StageName = 'Closed Won'`, inserts a follow-up **Task**:
     - Creates tasks with `Subject = 'Follow Up Test Task'`
     - Links each task to the opportunity via `WhatId = opp.Id`

4. **`RestrictContactByName.apxt`**  
   - **Timing:** `before insert`, `before update`  
   - **Behavior:** Prevents DML for contacts with an invalid last name:
     - If `LastName == 'INVALIDNAME'`, adds an error:
       - `The Last Name "<LastName>" is not allowed for DML`

## Notes
- Some triggers reference supporting components/fields (for example, `AccountTriggerHandler` and `Match_Billing_Address__c`).
- When importing into your Salesforce org, ensure those dependencies (handler classes and custom fields) exist.

## How to use
1. Create/prepare a Salesforce Developer Org or test environment.
2. Deploy these Apex trigger files (`.apxt` contents) to the org.
3. Validate behavior by running DML operations and checking trigger outcomes (tasks created, address copied, validation errors thrown).

