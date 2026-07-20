# Java Class Files

This folder contains a collection of small, self-contained Apex examples (handlers, controllers/services, async jobs, batch/schedulable work, and callout stubs) along with their corresponding test classes.

> Note: These are sample/learning classes; some classes rely on standard Salesforce objects (e.g., `Account`, `Contact`, `Lead`, `Opportunity`) and may also reference custom fields (e.g., `Number_of_Contacts__c`).

---

## Account-related classes

### `AccountHandler.apxc`
- Provides a simple utility method to insert an `Account`.
- Method: `insertNewAccount(String accountName)`
  - Creates an `Account` with `Name` and a hard-coded `AccountNumber`.
  - Performs DML inside a `try/catch` and returns the inserted `Account` (or `null` on DML exception).

### `AccountManager.apxc`
- Exposes a REST endpoint for retrieving an Account and its Contacts.
- REST mapping: `@RestResource(urlMapping='/Accounts/*/contacts')`
- Method: `@HttpGet global static Account getAccount()`
  - Extracts `accId` from `RestContext.request.requestURI`.
  - Queries:
    - `Account(Id, Name, (SELECT Id, Name FROM Contacts))`

### `AccountManagerTest.apxc`
- Tests `AccountManager.getAccount()` by setting up a `RestRequest` in `RestContext`.

### `AccountProcessor.apxc`
- Async/Future example that counts Contacts per Account.
- Method: `@future public static void countContacts(List<Id> accountIds)`
  - Queries Accounts by Id.
  - For each Account, performs a subquery count:
    - `SELECT count() FROM Contact WHERE AccountId = :account.Id`
  - Updates `Number_of_Contacts__c` and persists changes.

### `AccountProcessorTest.apxc`
- Creates an Account and 2 Contacts, then calls `AccountProcessor.countContacts()` inside a test.

### `AccountUtils` / `AccountUtil.apxc`
- (File name: `AccountUtil.apxc` but class is `AccountUtils`)
- Method: `accountsByState(String state)`
  - Returns Accounts filtered by `BillingState`.

### `AccountTriggerHandler.apxc`
- Trigger-handler style logic.
- Method: `CreateAccounts(List<Account> accList)`
  - If `ShippingState != BillingState`, sets `ShippingState = BillingState`.

### `AccountTriggerTest.apxc`
- Creates multiple Accounts with `BillingState = 'CA'` and asserts that `ShippingState` is set to `CA`.

---

## Contact/Account creation examples

### `AddPrimaryContact.apxc`
- Implements `Queueable` to create new Contacts for Accounts based on `BillingState`.
- Constructor accepts:
  - a source `Contact` to clone
  - a `state` filter
- `execute(...)`:
  - Queries up to 200 Accounts where `BillingState = :state`.
  - Clones the provided Contact and sets `AccountId` for each matched Account.
  - Inserts cloned Contacts.

### `AddPrimaryContactTest.apxc`
- Builds 50 Accounts in `CA` and 50 in `NY`.
- Enqueues `AddPrimaryContact` for state `CA`.

### `ContactSearch.apxc`
- Method: `searchForContacts(String lastName, String maillingpostalCode)`
  - SOQL query filtering by `LastName` and `MailingPostalCode`.

---

## Async / scheduled / batch processing

### `DailyLeadProcessor.apxc`
- Implements `Schedulable`.
- `execute(SchedulableContext SC)`:
  - Finds up to 200 `Lead` records where `LeadSource = null`.
  - Updates `LeadSource` to `'Dreamforce'`.

### `DailyLeadProcessorTest.apxc`
- Creates 200 Leads with `Status = 'Open - Not Contacted'` and schedules the job using a CRON expression.

### `LeadProcessor.apxc`
- Implements `Database.Batchable<sObject>`.
- `start(...)`: returns `Database.getQueryLocator([Select LeadSource From Lead])`
- `execute(...)`: sets `lead.LeadSource = 'Dreamforce'` for each lead and updates.
- `finish(...)`: empty.

### `LeadProcessorTest.apxc`
- Creates 200 Leads and runs `Database.executeBatch`.

---

## Async callouts (SOAP)

### `AsyncCalculatorServices.apxc`
- Async SOAP callout wrapper using `System.WebServiceCalloutFuture`.
- Provides Future response classes:
  - `doAddResponseFuture`
  - `doSubtractResponseFuture`
  - `doMultiplyResponseFuture`
  - `doDivideResponseFuture`
- `AsyncCalculatorImplPort`:
  - Uses `System.WebServiceCallout.beginInvoke(...)` to invoke calculator operations asynchronously.
- The actual SOAP types referenced are generated/available via the `calculatorServices` stub.

### `calculatorServices.apxc`
- SOAP stub/types container for calculator web service operations.
- Includes request/response classes:
  - `doAdd`, `doSubtract`, `doMultiply`, `doDivide` and their corresponding `*Response` classes.
- Includes a synchronous port class: `CalculatorImplPort` with `doAdd/doSubtract/doMultiply/doDivide` methods using `WebServiceCallout.invoke`.

---

## Callout + mocking examples (HTTP + WebServiceMock)

### `AnimalLocator.apxc`
- Performs an HTTP GET callout to:
  - `https://th-apex-http-callout.herokuapp.com/animals/{x}`
- Parses the JSON response using `JSON.deserializeUntyped`.
- Returns the animal `name`.

### `AnimalLocatorTest.apxc`
- Uses `Test.setMock(HttpCalloutMock.class, new AnimalLocatorMock())`.
- Calls `AnimalLocator.getAnimalNameById(3)` and asserts result equals `'chicken'`.

### `AnimalLocatorMock.apxc`
- Implements `HttpCalloutMock` (mock body not shown here, but used by `AnimalLocatorTest`).

### `ParkService.apxc`
- SOAP service stub for parks lookup.
- Contains `byCountry(String arg0)` method in `ParksImplPort`.

### `ParkLocator.apxc`
- Convenience wrapper:
  - `public static string[] country(string theCountry)`
  - Calls `ParkService.ParksImplPort.byCountry(theCountry)`.

### `ParkServiceMock.apxc`
- Implements `WebServiceMock`.
- Returns a static list of park names in response.

### `ParkLocatorTest.apxc`
- Sets `Test.setMock(WebServiceMock.class, new ParkServiceMock())`.
- Calls `ParkLocator.country('United States')` and asserts it matches the expected list.

---

## Miscellaneous utility / example classes

### `RandomContactFactory.apxc`
- Generates a list of `Contact` records in memory.
- Method: `generateRandomContacts(Integer numContactsToGenerate, String FName)`
  - Builds contacts with `FirstName = FName + ' ' + i` and `LastName = 'Contact ' + i`.
  - Does **not** insert (the insert is commented out).

### `StringListTest.apxc`
- Method: `generateStringList(Integer N)`
  - Returns a `List<String>` with entries like `'Test 0'`, `'Test 1'`, ...

---

## Validation/restriction example

### `TestRestrictByName.apxc`
- Test method that attempts to insert Contacts with one valid last name and one intentionally invalid last name.
- Wraps insert in try/catch (expects an exception/validation failure scenario).

---

## Suggested learning workflow
1. Start by reading the handler/service classes (`AccountHandler`, `AccountManager`, `AccountProcessor`, `AccountTriggerHandler`).
2. Review the async patterns:
   - Future (`AccountProcessor`)
   - Queueable (`AddPrimaryContact`)
   - Schedulable (`DailyLeadProcessor`)
   - Batch (`LeadProcessor`)
3. Review callout patterns:
   - HTTP callout (`AnimalLocator` + `AnimalLocatorMock`)
   - SOAP WebService callout + mocks (`ParkLocator`/`ParkService`/`ParkServiceMock`)
   - Async SOAP callouts (`AsyncCalculatorServices` + `calculatorServices`)
4. Use the `*Test.apxc` classes as examples of how each feature is invoked and validated.

