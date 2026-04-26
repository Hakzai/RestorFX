# 📄 enhancements.md

Complementary document to `prototype.md` containing architectural, functional, and product improvements.

---

# 🎯 Objective

Evolve the system from a prototype into a **modular, configurable, and potentially commercial product**, while keeping low coupling and enabling parallel development through independent epics.

---

# 🧩 EPIC 10 — Internationalization (i18n)

## Goal

Support multiple languages while keeping the core in English and the UI in Portuguese.

## Implementation

* Use `ResourceBundle`
* Files:

  * `/resources/i18n/messages_en.properties`
  * `/resources/i18n/messages_pt_BR.properties`

## Pattern

* FXML: `text="%key"`
* Java: centralized translation service

## Suggestion

Create class:

```java
public class I18n {
    private static ResourceBundle bundle;

    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("i18n.messages", locale);
    }

    public static String t(String key) {
        return bundle.getString(key);
    }
}
```

## Independence

✅ No dependencies on other epics

---

# 🎨 EPIC 11 — UI/UX Standardization (CSS + Layout)

## Goal

Evolve from prototype visuals to a professional interface.

## Implementation

* Use JavaFX CSS
* Structure:

  * `/styles/base.css`
  * `/styles/theme-light.css`
  * `/styles/theme-dark.css`

## Guidelines

* Consistent spacing (8/16/24)
* Rounded borders
* Use icons (Ikonli or FontAwesomeFX)

## Independence

✅ No dependencies on other epics

---

# 🧱 EPIC 12 — Modular Architecture (Separated Screens)

## Goal

Replace tab-based navigation with independent modules.

## Structure

```
modules/
  sales/
  inventory/
  finance/
```

## Implementation

* Each module contains:

  * View (FXML)
  * Controller
  * Service

## Loader

```java
public class ModuleLoader {
    public Parent load(String moduleName) throws IOException {
        return FXMLLoader.load(getClass().getResource(
            "/modules/" + moduleName + "/view.fxml"
        ));
    }
}
```

## Benefits

* Per-client customization
* Enable/disable modules
* Foundation for monetization

## Independence

✅ No dependencies on other epics

---

# 🧩 EPIC 13 — Componentization (FXML)

## Goal

Enable UI reuse without FXML inheritance.

## Strategy

* Use `<fx:include>`
* Create reusable components

## Example

```xml
<VBox>
    <fx:include source="components/header.fxml"/>
</VBox>
```

## Alternative

Custom Controls in Java

## Independence

✅ No dependencies on other epics

---

# ⚙️ EPIC 14 — Central Configuration System

## Goal

Centralize system configuration.

## Structure

```java
public class AppContext {
    private Locale locale;
    private Config config;
    private User user;
    private List<String> enabledModules;
}
```

## Independence

✅ No dependencies on other epics

---

# 🔐 EPIC 15 — Authentication and Authorization

## Goal

Implement login and access control.

## Features

* Username/password login
* User session
* Roles:

  * ADMIN
  * USER

## Permissions

* Per module
* Per feature

## Suggested structure

```java
class User {
    String username;
    String password;
    Role role;
}
```

## Independence

✅ No dependencies on other epics

---

# 📊 EPIC 16 — Reporting Module

## Goal

Generate operational and financial reports.

## Types

* Financial
* Customers
* Orders

## Outputs

* On-screen
* Export (PDF, CSV in the future)

## Note

Do not confuse with technical observability.

## Independence

✅ No dependencies on other epics

---

# 🧪 EPIC 17 — Basic Logging

## Goal

Enable system event tracking.

## Implementation

* SLF4J + Logback

## Example

```java
logger.info("User logged in");
```

## Independence

✅ No dependencies on other epics

---

# 🚀 EPIC 18 — Build and Deployment Versioning

## Goal

Automatically version artifacts generated after each merge into main.

## Strategy

Format:

```
v{major}.{minor}.{epic}
```

### Example

* EPIC 10 → version: `v1.0.10`
* EPIC 15 → version: `v1.0.15`

## Rules

* Each merge into `main` generates a new version
* Epic number composes the version
* Optional: include timestamp or commit hash

## Technical suggestion

* Use CI/CD (GitHub Actions or Azure DevOps)
* Automatically generate artifact

## Independence

✅ No dependencies on other epics

---

# ⚠️ OPTIONAL EPICS (HIGHER COMPLEXITY)

---

# 🧠 EPIC 19 — Feature Flags / Basic vs Advanced Mode

## Goal

Enable dynamic feature toggling.

## Implementation

* JSON configuration file

```json
{
  "mode": "basic",
  "features": {
    "advancedPricing": false
  }
}
```

## Usage

```java
if (config.isEnabled("advancedPricing")) {
    field.setVisible(true);
}
```

## Risks

* Increased complexity
* Harder maintenance without clear standards

## Independence

✅ No dependencies on other epics

---

# 🧩 EPIC 20 — Dynamic Forms

## Goal

Generate UI forms from configuration.

## Example

```json
{
  "fields": [
    { "name": "price", "type": "number" },
    { "name": "discount", "type": "number", "advanced": true }
  ]
}
```

## Benefits

* High customization
* Fewer deployments

## Risks

* High complexity
* Harder debugging

## Independence

✅ No dependencies on other epics

---

# 📌 Conclusion

The proposed improvements transform the system into:

* Modular
* Configurable
* Scalable
* Commercial-ready

The separation into independent epics enables safe parallel development via branches, with controlled integration later.

---

Next steps may include:

* Architecture diagram
* Base Java package structure
* Initial CI/CD setup
