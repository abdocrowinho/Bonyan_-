package com.dodeal.features.shared

1. Header Texts

HeaderText — main page title, used once at the top of every screen
SubTittleText — secondary line directly below the title, describes the screen purpose


2. Metric Card

Use for any small tile that shows a title + a value
Use for stat counters, category filters, and dashboard tiles
Always use ignorePadding = true inside FlowRow or Row layouts
Use isSelected to highlight the active category
Tapping a MetricCard sends a category filter intent to the ViewModel


3. Cards & Containers

glassEffect() — use on any interactive card or detail block (developer card, info block, call logs block). Supports hover highlight and scale on hover
themeCardSurface() — use on solid non-interactive surfaces (form containers, step cards, dialog inner content areas)
Rule: if the card is clickable or hoverable → glassEffect. If it is a static container holding a form → themeCardSurface


4. Buttons

CustomAppButton — the only button in the app. Use for every action: primary (green filled), secondary (dark outlined), with or without an icon
AppToolbarButton — small action button that lives inside the bulk-actions bar above a table (e.g. Bulk Assign)
AppDeleteButton — destructive action button inside the bulk-actions bar (e.g. Bulk Delete)
Never build a custom button using a manually styled Row or Box


5. Text Input

CustomAppTextField — the only text input. Use everywhere a user types
Always use LeadFormFieldHeight (45.dp) as the height for fields inside forms — never hardcode a height
Supports error state, placeholder, leading icon, and read-only mode


6. Form Field Layout

LeadRowField — layout wrapper that puts the label on the left and the field on the right. Every field in an add or edit form must be wrapped in this
LeadDropdownField — dropdown field for forms, handles the label and the dropdown together
ContactPhoneRow — country code dropdown + number input side by side. Use for every phone number field


7. Dropdowns

AppDropdown — the core dropdown. Always use DropdownVariant.PILL. Use everywhere a user picks from a list
AppDropdownCell — dropdown rendered inside a table row cell. Use for editable columns like Manager, Agent, Status, Priority


8. Time & Date Pickers

StyledTimePicker — time picker dialog. Always triggered by a transparent overlay on top of a read-only text field
LeadDatePickerDialog — date picker dialog. Same trigger pattern as StyledTimePicker


9. Table

GenericScrollableTable — the only table wrapper. Handles horizontal scroll and loading state
CustomTableHeaderRow — header row wrapper, contains header cells
CustomHeaderCell — single header cell with a label and a weight
CustomTableBodyRow — body row wrapper, handles selection highlight, hover, and click
CustomBodyCell — single read-only text cell inside a body row
Never build a table manually with a plain LazyColumn and Row cells


10. Row Action Menu

EntityOptionsMenuHost — the popup action menu anchored to a trigger (the ⋮ icon in the action column)
CustomItemOption — a single item inside the action menu, with an icon and a color
Always set showDividerBelow = false on the last item


11. Dialogs

BaseLeadDialog — the only dialog wrapper. Use for every detail view and every edit form. Never use Dialog{} or AlertDialog{} directly as a screen-level dialog
GenericFiltersDialog — the filter dialog for list screens. Takes a list of filter field definitions built by a buildFilterFields function


12. Detail Blocks (inside dialogs)

InfoBlock — grouped key/value information with a title and an icon (e.g. Contact Info, Team, Property)
NotesBlock — notes list with an Add Note button
CallDetailsBlock — call logs list with a See More link
MeetingRecordsBlock — meeting records list with a See More link
Every detail block container must use glassEffect() with RoundedCornerShape(8.sdp) and padding(12.sdp)


13. Toolbar & Steps

CustomToolbar — the search + filter + sort bar shown above every table. Never build a custom search row
GenericStepsRow — the step progress indicator for multi-step add forms


14. Utility Modifiers & Helpers

screenPadding() — standard horizontal padding applied to every screen root composable
onHover() — tracks hover state. Always feed its result into glassEffect or an animated color
TimeStateContainer — displays a Call In time value with automatic color based on time of day

