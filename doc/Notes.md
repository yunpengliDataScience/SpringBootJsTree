

```javascript
$('#jstree').jstree(true).get_json('#', { flat: true }).forEach(function (node) {
    const $anchor = $('#' + node.id + '_anchor');
    if (node.data && node.data.status === 'Modified') {
        $anchor.addClass('red-node');
    } else {
        $anchor.removeClass('red-node');
    }
});
```

---

### 🔎 Explanation

1. **`$('#jstree')`**

   * Selects the DOM element with ID `jstree` (your jsTree container).

2. **`.jstree(true)`**

   * Gets the actual jsTree **instance** (not just the jQuery wrapper).
   * This allows you to call jsTree’s internal API methods.

3. **`.get_json('#', { flat: true })`**

   * Retrieves the tree in JSON format starting at the root (`'#'`).
   * `{ flat: true }` means it returns an **array** of nodes instead of nested objects (so easier to loop through).

4. **`.forEach(function (node) { ... })`**

   * Iterates through each node in that flat list.

5. **`const $anchor = $('#' + node.id + '_anchor');`**

   * Every jsTree node has an anchor element in the DOM with an ID pattern:

     ```
     <nodeId>_anchor
     ```
   * Example: if a node’s ID is `"123"`, the anchor’s ID is `"123_anchor"`.
   * `$anchor` is a jQuery object pointing to that anchor element.

6. **`if (node.data && node.data.status === 'Modified')`**

   * Checks if the node has a `data` object and its `status` property is `"Modified"`.

7. **`$anchor.addClass('red-node');`**

   * If status is `"Modified"`, add a CSS class `red-node` to the anchor (usually to color or highlight it).

8. **`else { $anchor.removeClass('red-node'); }`**

   * If not `"Modified"`, make sure the highlight is removed.

---

### ✅ What It Does

This snippet **loops through every node in your jsTree** and:

* Adds a CSS class (`red-node`) to nodes marked as `"Modified"`.
* Removes the class from nodes that aren’t modified.

So visually, your tree shows modified nodes in red (or however you style them in CSS).

---

### 🎨 Example CSS

```css
.red-node {
    color: red;
    font-weight: bold;
}
```

---
