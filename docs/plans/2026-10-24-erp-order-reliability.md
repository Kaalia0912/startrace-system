# ERP 订单可靠性与生产安全 Implementation Plan

> **For Codex:** Execute this plan directly in the current workspace. The user explicitly requested implementation without a separate execution session.

**Goal:** Close production-only tool endpoints, make sales-order imports atomic and report accurate row errors, restore amount auto-calculation, and link the dashboard data-quality alert to a filtered order list.

**Architecture:** Use configuration and Spring Security to disable anonymous production tool access. Use a two-pass import flow plus a transaction so validation failures write nothing and database failures roll back the full batch. Keep Vue 2 and Element UI conventions, adding only one query flag and merging the duplicated watchers.

**Tech Stack:** Spring Boot 4, Spring Security, MyBatis, MySQL, Vue 2, Element UI, JUnit 5, Mockito, Maven, npm.

---

### Task 1: Lock down production tool endpoints

**Files:**
- Modify: `ruoyi-admin/src/main/resources/application.yml`
- Modify: `ruoyi-admin/src/main/resources/application-druid.yml`
- Modify: `ruoyi-framework/src/main/java/com/ruoyi/framework/config/SecurityConfig.java`

**Step 1: Disable SpringDoc and the Druid stat servlet by default**

Set `springdoc.api-docs.enabled=false`, `springdoc.swagger-ui.enabled=false`, and `spring.datasource.druid.statViewServlet.enabled=false`. These can still be overridden explicitly in a development environment.

**Step 2: Remove anonymous route allowances**

Remove Swagger and Druid patterns from the `permitAll` matcher.

**Step 3: Verify**

Run the backend tests and package build. After deployment, verify `/v3/api-docs`, `/swagger-ui/index.html`, and `/druid/` no longer return anonymous success responses.

### Task 2: Make sales-order import atomic and accurate

**Files:**
- Modify: `ruoyi-system/src/main/java/com/ruoyi/system/service/impl/SaleOrderServiceImpl.java`
- Modify: `ruoyi-system/src/test/java/com/ruoyi/system/service/SaleOrderServiceImplTest.java`

**Step 1: Add failing tests**

Cover negative quantity/price/amount, duplicate order numbers inside one workbook, and the absence of inserts when pre-validation fails. Also verify the reported Excel row number accounts for the header row.

**Step 2: Run the focused tests**

Run: `mvn -pl ruoyi-system -am -Dtest=SaleOrderServiceImplTest test`

Expected: the new tests fail against the current implementation.

**Step 3: Implement two-pass validation and transaction rollback**

Validate every row before writing. Reject duplicate nonblank order numbers within the file, ambiguous duplicate database matches, negative numeric values, and existing order numbers when update mode is off. Annotate the whole import method with `@Transactional(rollbackFor = Exception.class)`.

**Step 4: Run the focused tests**

Run the command from Step 2. Expected: PASS.

### Task 3: Restore amount auto-calculation

**Files:**
- Modify: `ruoyi-ui/src/views/order/sale/index.vue`

**Step 1: Merge duplicate Vue watchers**

Keep quantity, unit price, and `$route.query` watchers in one `watch` object so quantity x unit price updates the editable amount until the user manually changes the amount.

**Step 2: Verify the production frontend build**

Run: `npm run build:prod` in `ruoyi-ui`.

Expected: build exits successfully.

### Task 4: Link dashboard data-quality alert to a filtered list

**Files:**
- Modify: `ruoyi-system/src/main/java/com/ruoyi/system/domain/SaleOrder.java`
- Modify: `ruoyi-system/src/main/resources/mapper/order/SaleOrderMapper.xml`
- Modify: `ruoyi-ui/src/views/index.vue`
- Modify: `ruoyi-ui/src/views/order/sale/index.vue`

**Step 1: Add an `incompleteOnly` query flag**

Filter orders where customer name, amount, or delivery date is missing, matching the dashboard summary rule.

**Step 2: Make the workbench alert actionable**

Navigate to `/order/sale?incompleteOnly=true` and show a visible active filter in the order page. Reset explicitly supplied navigation filters when no filter query is present.

**Step 3: Run full verification**

Run backend tests, package build, and frontend production build.

### Task 5: Document business questions

**Files:**
- Create: `docs/erp-business-rules-questionnaire.md`

Group questions by order structure, numbering, master data, amount rules, workflow/status, permissions, imports, and backup/retention. Mark the minimum decisions required before schema changes.
