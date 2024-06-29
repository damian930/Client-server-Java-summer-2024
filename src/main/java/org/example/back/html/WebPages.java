package org.example.back.html;

import org.example.back.data_base.Category;
import org.example.back.data_base.Product;
import org.example.back.data_base.Product_DAO;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebPages {

    //------------------------------------------------------------------------------------------------------------//
    public static String generateDynamicHTMLForCategories(List<Category> categories) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head><title>Categories</title>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body {");
        htmlBuilder.append("    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;");
        htmlBuilder.append("    background-color: #15202b;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    padding: 20px;");
        htmlBuilder.append("    position: relative;");
        htmlBuilder.append("}");
        htmlBuilder.append("h1 {");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("    text-align: center;");
        htmlBuilder.append("}");
        htmlBuilder.append(".home-button, .stats-button, .add-button, .edit-button {");
        htmlBuilder.append("    position: absolute;");
        htmlBuilder.append("    top: 10px;");
        htmlBuilder.append("}");
        htmlBuilder.append(".home-button {");
        htmlBuilder.append("    left: 10px;");
        htmlBuilder.append("}");
        htmlBuilder.append(".stats-button {");
        htmlBuilder.append("    position: absolute;");
        htmlBuilder.append("    top: 20px;");
        htmlBuilder.append("    right: 231px;");
        htmlBuilder.append("}");
        htmlBuilder.append(".add-button {");
        htmlBuilder.append("    position: absolute;");
        htmlBuilder.append("    top: 20px;");
        htmlBuilder.append("    right: 162px;");
        htmlBuilder.append("}");
        htmlBuilder.append(".edit-button {");
        htmlBuilder.append("    position: absolute;");
        htmlBuilder.append("    top: 20px;");
        htmlBuilder.append("    right: 94px;");
        htmlBuilder.append("}");
        htmlBuilder.append(".delete-button {");
        htmlBuilder.append("    position: absolute;");
        htmlBuilder.append("    top: 20px;");
        htmlBuilder.append("    right: 10px;");
        htmlBuilder.append("}");
        htmlBuilder.append(".button {");
        htmlBuilder.append("    font-size: 14px;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    background-color: #1da1f2;");
        htmlBuilder.append("    border: none;");
        htmlBuilder.append("    padding: 10px 20px;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    cursor: pointer;");
        htmlBuilder.append("}");
        htmlBuilder.append(".button:hover {");
        htmlBuilder.append("    background-color: #ffffff;");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("}");
        htmlBuilder.append(".category-button {");
        htmlBuilder.append("    display: block;");
        htmlBuilder.append("    width: calc(100% - 80px);");
        htmlBuilder.append("    padding: 10px;");
        htmlBuilder.append("    margin: 10px auto;");
        htmlBuilder.append("    font-size: 16px;");
        htmlBuilder.append("    color: #15202b;");
        htmlBuilder.append("    background-color: #ffffff;");
        htmlBuilder.append("    border: 2px solid #ffffff;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    cursor: pointer;");
        htmlBuilder.append("}");
        htmlBuilder.append(".category-button:hover {");
        htmlBuilder.append("    background-color: #1da1f2;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("}");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head><body>");

        // Home Button
        htmlBuilder.append("<a href=\"/categories\"><button class=\"home-button button\">Home</button></a>");

        // Stats Button
        htmlBuilder.append("<a href=\"/stats\"><button class=\"stats-button button\">Stats</button></a>");

        // Add Button
        htmlBuilder.append("<a href=\"/add/category\"><button class=\"add-button button\">Add</button></a>");

        // Edit Button
        htmlBuilder.append("<a href=\"/edit/category\"><button class=\"edit-button button\">Edit</button></a>");

        // Delete Button
        htmlBuilder.append("<a href=\"/delete/category\"><button class=\"delete-button button\">Delete</button></a>");

        // Title
        htmlBuilder.append("<h1>Categories</h1>");

        // Category Buttons
        for (Category category : categories) {
            htmlBuilder.append("<button class=\"category-button\" onclick=\"redirectToProducts('")
                    .append(category.getName()).append("')\">")
                    .append(category.getName()).append("</button>");
        }

        // JavaScript Functions
        htmlBuilder.append("<script>");
        htmlBuilder.append("function redirectToProducts(categoryName) {");
        htmlBuilder.append("    window.location.href = '/products?category=' + encodeURIComponent(categoryName);");
        htmlBuilder.append("}");
        htmlBuilder.append("</script>");

        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    public static String generateAddCategoryHTML() {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head><title>Add Category</title>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body {");
        htmlBuilder.append("    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;");
        htmlBuilder.append("    background-color: #15202b;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    padding: 20px;");
        htmlBuilder.append("}");
        htmlBuilder.append("h1 {");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("    text-align: center;");
        htmlBuilder.append("}");
        htmlBuilder.append("form {");
        htmlBuilder.append("    margin-top: 20px;");
        htmlBuilder.append("    max-width: 600px;");
        htmlBuilder.append("    margin: 0 auto;");
        htmlBuilder.append("    background-color: #1a1a1a;");
        htmlBuilder.append("    padding: 20px;");
        htmlBuilder.append("    border-radius: 8px;");
        htmlBuilder.append("}");
        htmlBuilder.append("label {");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("}");
        htmlBuilder.append("input[type=text] {");
        htmlBuilder.append("    width: 100%;");
        htmlBuilder.append("    padding: 10px;");
        htmlBuilder.append("    margin: 5px 0;");
        htmlBuilder.append("    display: inline-block;");
        htmlBuilder.append("    border: 1px solid #ccc;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    box-sizing: border-box;");
        htmlBuilder.append("}");
        htmlBuilder.append("button[type=submit] {");
        htmlBuilder.append("    margin-top: 10px;");
        htmlBuilder.append("    background-color: #1da1f2;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    padding: 10px 20px;");
        htmlBuilder.append("    border: none;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    cursor: pointer;");
        htmlBuilder.append("}");
        htmlBuilder.append("button[type=submit]:hover {");
        htmlBuilder.append("    background-color: #ffffff;");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("}");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head><body>");

        // Home Button
        htmlBuilder.append("<a href=\"/categories\"><button class=\"home-button\">Home</button></a>");

        // Heading
        htmlBuilder.append("<h1>Add Category</h1>");

        // Add Category Form
        htmlBuilder.append("<form id=\"addCategoryForm\" action=\"/insert/category\" method=\"POST\" onsubmit=\"return validateAddForm()\">");
        htmlBuilder.append("<label for=\"name\">Name:</label><br>");
        htmlBuilder.append("<input type=\"text\" id=\"name\" name=\"name\" required><br>");
        htmlBuilder.append("<button type=\"submit\">Add Category</button>");
        htmlBuilder.append("</form>");

        // JavaScript for Form Validation
        htmlBuilder.append("<script>");
        htmlBuilder.append("function validateAddForm() {");
        htmlBuilder.append("    var name = document.getElementById('name').value.trim();");
        htmlBuilder.append("    if (name === '') {");
        htmlBuilder.append("        alert('Please enter a category name');");
        htmlBuilder.append("        return false;");
        htmlBuilder.append("    }");
        htmlBuilder.append("    return true;");
        htmlBuilder.append("}");
        htmlBuilder.append("</script>");

        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    public static String generateEditCategoryHTML(List<Category> categories) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head><title>Edit Category</title>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body {");
        htmlBuilder.append("    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;");
        htmlBuilder.append("    background-color: #15202b;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    padding: 20px;");
        htmlBuilder.append("}");
        htmlBuilder.append("h1 {");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("    text-align: center;");
        htmlBuilder.append("}");
        htmlBuilder.append("form {");
        htmlBuilder.append("    margin-top: 20px;");
        htmlBuilder.append("    max-width: 600px;");
        htmlBuilder.append("    margin: 0 auto;");
        htmlBuilder.append("    background-color: #1a1a1a;");
        htmlBuilder.append("    padding: 20px;");
        htmlBuilder.append("    border-radius: 8px;");
        htmlBuilder.append("}");
        htmlBuilder.append("label {");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("}");
        htmlBuilder.append("input[type=text], select, textarea {");
        htmlBuilder.append("    width: 100%;");
        htmlBuilder.append("    padding: 10px;");
        htmlBuilder.append("    margin: 5px 0;");
        htmlBuilder.append("    display: inline-block;");
        htmlBuilder.append("    border: 1px solid #ccc;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    box-sizing: border-box;");
        htmlBuilder.append("}");
        htmlBuilder.append("button[type=submit], button[type=button] {");
        htmlBuilder.append("    margin-top: 10px;");
        htmlBuilder.append("    background-color: #1da1f2;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    padding: 10px 20px;");
        htmlBuilder.append("    border: none;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    cursor: pointer;");
        htmlBuilder.append("}");
        htmlBuilder.append("button[type=submit]:hover, button[type=button]:hover {");
        htmlBuilder.append("    background-color: #ffffff;");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("}");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head><body>");

        // Navigation and header
        htmlBuilder.append("<a href=\"/categories\"><button class=\"home-button\">Home</button></a>");
        htmlBuilder.append("<h1>Edit Category</h1>");

        // Edit Form
        htmlBuilder.append("<form id=\"editForm\" action=\"/save/category\" method=\"POST\" onsubmit=\"return validateEditForm()\">");
        htmlBuilder.append("<label for=\"category\">Category:</label><br>");
        htmlBuilder.append("<select id=\"category\" name=\"categoryId\" required>");

        // Populate dropdown with categories
        for (Category category : categories) {
            htmlBuilder.append("<option value=\"").append(category.getId()).append("\">").append(category.getName()).append("</option>");
        }

        htmlBuilder.append("</select><br>");
        htmlBuilder.append("<label for=\"newName\">New Name:</label><br>");
        htmlBuilder.append("<input type=\"text\" id=\"newName\" name=\"newName\" required><br>");

        // Save button
        htmlBuilder.append("<button type=\"submit\">Save</button>");

        htmlBuilder.append("</form>");

        // JavaScript for Form Validation
        htmlBuilder.append("<script>");
        htmlBuilder.append("function validateEditForm() {");
        htmlBuilder.append("    var category = document.getElementById('category');");
        htmlBuilder.append("    var newName = document.getElementById('newName');");
        htmlBuilder.append("    if (category.value === '') {");
        htmlBuilder.append("        alert('Please select a category.');");
        htmlBuilder.append("        return false;");
        htmlBuilder.append("    }");
        htmlBuilder.append("    if (newName.value.trim() === '') {");
        htmlBuilder.append("        alert('Please enter a new name for the category.');");
        htmlBuilder.append("        return false;");
        htmlBuilder.append("    }");
        htmlBuilder.append("    return true;");
        htmlBuilder.append("}");
        htmlBuilder.append("</script>");

        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    public static String generateDeleteCategoryHTML(List<Category> categories) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head><title>Delete Category</title>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body {");
        htmlBuilder.append("    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;");
        htmlBuilder.append("    background-color: #15202b;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    padding: 20px;");
        htmlBuilder.append("}");
        htmlBuilder.append("h1 {");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("    text-align: center;");
        htmlBuilder.append("}");
        htmlBuilder.append("form {");
        htmlBuilder.append("    margin-top: 20px;");
        htmlBuilder.append("    max-width: 600px;");
        htmlBuilder.append("    margin: 0 auto;");
        htmlBuilder.append("    background-color: #1a1a1a;");
        htmlBuilder.append("    padding: 20px;");
        htmlBuilder.append("    border-radius: 8px;");
        htmlBuilder.append("}");
        htmlBuilder.append("label {");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("}");
        htmlBuilder.append("select {");
        htmlBuilder.append("    width: 100%;");
        htmlBuilder.append("    padding: 10px;");
        htmlBuilder.append("    margin: 5px 0;");
        htmlBuilder.append("    display: inline-block;");
        htmlBuilder.append("    border: 1px solid #ccc;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    box-sizing: border-box;");
        htmlBuilder.append("}");
        htmlBuilder.append("button[type=submit], button[type=button] {");
        htmlBuilder.append("    margin-top: 10px;");
        htmlBuilder.append("    background-color: #ff3333;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    padding: 10px 20px;");
        htmlBuilder.append("    border: none;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    cursor: pointer;");
        htmlBuilder.append("}");
        htmlBuilder.append("button[type=submit]:hover, button[type=button]:hover {");
        htmlBuilder.append("    background-color: #ffffff;");
        htmlBuilder.append("    color: #ff3333;");
        htmlBuilder.append("}");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head><body>");

        // Navigation and header
        htmlBuilder.append("<a href=\"/categories\"><button class=\"home-button\">Home</button></a>");
        htmlBuilder.append("<h1>Delete Category</h1>");

        // Delete Form
        htmlBuilder.append("<form id=\"deleteForm\" action=\"/deleting_category\" method=\"POST\" onsubmit=\"return confirmDelete()\">");
        htmlBuilder.append("<label for=\"category\">Select Category to Delete:</label><br>");
        htmlBuilder.append("<select id=\"category\" name=\"categoryId\" required onchange=\"updateCategoryName()\">");

        // Populate dropdown with categories
        for (Category category : categories) {
            htmlBuilder.append("<option value=\"").append(category.getId()).append("\">").append(category.getName()).append("</option>");
        }

        htmlBuilder.append("</select><br>");

        // Hidden input field for category name
        htmlBuilder.append("<input type=\"hidden\" id=\"categoryName\" name=\"categoryName\">");

        htmlBuilder.append("<button type=\"submit\">Confirm Delete</button>");
        htmlBuilder.append("</form>");

        // JavaScript for updating category name based on selected option
        htmlBuilder.append("<script>");
        htmlBuilder.append("function updateCategoryName() {");
        htmlBuilder.append("    var categoryId = document.getElementById('category').value;");
        htmlBuilder.append("    var categoryName = document.getElementById('category').options[document.getElementById('category').selectedIndex].text;");
        htmlBuilder.append("    document.getElementById('categoryName').value = categoryName;");
        htmlBuilder.append("}");
        htmlBuilder.append("function confirmDelete() {");
        htmlBuilder.append("    return confirm('Are you sure you want to delete category: ' + document.getElementById('categoryName').value + '?');");
        htmlBuilder.append("}");
        htmlBuilder.append("</script>");

        return htmlBuilder.toString();
    }


    //------------------------------------------------------------------------------------------------------------//


    public static Map<String, String> parseQueryParams_products(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                params.put(key, value);
            }
        }
        return params;
    }

    public static String generateDynamicHTMLForProducts(String categoryName, List<Product> products) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head><title>").append(categoryName).append(" Products</title>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body {");
        htmlBuilder.append("    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;");
        htmlBuilder.append("    background-color: #15202b;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    padding: 20px;");
        htmlBuilder.append("}");
        htmlBuilder.append("h1 {");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("    text-align: center;");
        htmlBuilder.append("}");
        htmlBuilder.append(".product-details {");
        htmlBuilder.append("    margin-top: 20px;");
        htmlBuilder.append("}");
        htmlBuilder.append(".home-button {");
        htmlBuilder.append("    position: absolute;");
        htmlBuilder.append("    top: 20px;");
        htmlBuilder.append("    left: 20px;");
        htmlBuilder.append("    font-size: 14px;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    background-color: #1da1f2;");
        htmlBuilder.append("    border: none;");
        htmlBuilder.append("    padding: 10px 20px;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    cursor: pointer;");
        htmlBuilder.append("}");
        htmlBuilder.append(".home-button:hover {");
        htmlBuilder.append("    background-color: #ffffff;");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("}");
        htmlBuilder.append(".product-button {");
        htmlBuilder.append("    display: block;");
        htmlBuilder.append("    width: calc(100% - 80px);");
        htmlBuilder.append("    padding: 10px;");
        htmlBuilder.append("    margin: 10px auto;");
        htmlBuilder.append("    font-size: 16px;");
        htmlBuilder.append("    color: #15202b;");
        htmlBuilder.append("    background-color: #ffffff;");
        htmlBuilder.append("    border: 2px solid #ffffff;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    cursor: pointer;");
        htmlBuilder.append("}");
        htmlBuilder.append(".product-button:hover {");
        htmlBuilder.append("    background-color: #1da1f2;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("}");
        htmlBuilder.append(".add-button {");
        htmlBuilder.append("    position: absolute;");
        htmlBuilder.append("    top: 20px;");
        htmlBuilder.append("    right: 20px;");
        htmlBuilder.append("    font-size: 14px;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    background-color: #1da1f2;");
        htmlBuilder.append("    border: none;");
        htmlBuilder.append("    padding: 10px 20px;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    cursor: pointer;");
        htmlBuilder.append("}");
        htmlBuilder.append(".add-button:hover {");
        htmlBuilder.append("    background-color: #ffffff;");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("}");
        htmlBuilder.append(".search-input {");
        htmlBuilder.append("    width: 100%;");
        htmlBuilder.append("    padding: 10px;");
        htmlBuilder.append("    margin-top: 10px;");
        htmlBuilder.append("    box-sizing: border-box;");
        htmlBuilder.append("    font-size: 16px;");
        htmlBuilder.append("    border: 2px solid #1da1f2;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("}");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head><body>");

        // Home Button
        htmlBuilder.append("<a href=\"/categories\"><button class=\"home-button\">Home</button></a>");

        // Heading
        htmlBuilder.append("<h1>").append(categoryName).append(" Products</h1>");

        // Add Product Button
        htmlBuilder.append("<button class=\"add-button\" onclick=\"openAddProduct()\">Add</button>");

        // Search Input
        htmlBuilder.append("<input type=\"text\" id=\"searchInput\" class=\"search-input\" placeholder=\"Search products...\" onkeyup=\"filterProducts()\">");

        // Product Buttons
        htmlBuilder.append("<div id=\"productList\">");
        for (Product product : products) {
            htmlBuilder.append("<button class=\"product-button\" onclick=\"redirectToProduct('").append(product.getId()).append("')\">")
                    .append(product.getName()).append("</button>");
        }
        htmlBuilder.append("</div>");

        // JavaScript Function for Redirecting to Product Page
        htmlBuilder.append("<script>");
        htmlBuilder.append("function redirectToProduct(productId) {");
        htmlBuilder.append("    window.location.href = '/product?id=' + encodeURIComponent(productId);");
        htmlBuilder.append("}");

        // JavaScript Function for Opening Add Product URL
        htmlBuilder.append("function openAddProduct() {");
        htmlBuilder.append("    window.location.href = '/add/product';");
        htmlBuilder.append("}");

        // JavaScript Function for Filtering Products
        htmlBuilder.append("function filterProducts() {");
        htmlBuilder.append("    var input, filter, ul, li, button, i, txtValue;");
        htmlBuilder.append("    input = document.getElementById('searchInput');");
        htmlBuilder.append("    filter = input.value.toUpperCase();");
        htmlBuilder.append("    ul = document.getElementById('productList');");
        htmlBuilder.append("    li = ul.getElementsByTagName('button');");
        htmlBuilder.append("    for (i = 0; i < li.length; i++) {");
        htmlBuilder.append("        button = li[i];");
        htmlBuilder.append("        txtValue = button.textContent || button.innerText;");
        htmlBuilder.append("        if (txtValue.toUpperCase().indexOf(filter) > -1) {");
        htmlBuilder.append("            button.style.display = '';");
        htmlBuilder.append("        } else {");
        htmlBuilder.append("            button.style.display = 'none';");
        htmlBuilder.append("        }");
        htmlBuilder.append("    }");
        htmlBuilder.append("}");

        htmlBuilder.append("</script>");

        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    public static Map<String, String> parseQueryParams_product(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = idx > 0 ? pair.substring(0, idx) : pair;
                String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : "";
                params.put(key, value);
            }
        }
        return params;
    }

    public static String generateDynamicHTMLForProduct(Product product) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head><title>").append(product.getName()).append(" - Edit Product</title>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body {");
        htmlBuilder.append("    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;");
        htmlBuilder.append("    background-color: #15202b;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    padding: 20px;");
        htmlBuilder.append("}");
        htmlBuilder.append("h1 {");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("    text-align: center;");
        htmlBuilder.append("}");
        htmlBuilder.append(".product-details {");
        htmlBuilder.append("    margin-top: 20px;");
        htmlBuilder.append("}");
        htmlBuilder.append(".home-button {");
        htmlBuilder.append("    position: absolute;");
        htmlBuilder.append("    top: 20px;");
        htmlBuilder.append("    left: 20px;");
        htmlBuilder.append("    font-size: 14px;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    background-color: #1da1f2;");
        htmlBuilder.append("    border: none;");
        htmlBuilder.append("    padding: 10px 20px;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    cursor: pointer;");
        htmlBuilder.append("}");
        htmlBuilder.append(".home-button:hover {");
        htmlBuilder.append("    background-color: #ffffff;");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("}");
        htmlBuilder.append(".edit-button, .add-amount-button {");
        htmlBuilder.append("    margin-top: 10px;");
        htmlBuilder.append("    padding: 10px 20px;");
        htmlBuilder.append("    background-color: #ffffff;");
        htmlBuilder.append("    color: #15202b;");
        htmlBuilder.append("    border: 2px solid #ffffff;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    cursor: pointer;");
        htmlBuilder.append("}");
        htmlBuilder.append(".edit-button:hover, .add-amount-button:hover {");
        htmlBuilder.append("    background-color: #1da1f2;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("}");
        htmlBuilder.append(".buy-form, .add-amount-form {");
        htmlBuilder.append("    margin-top: 20px;");
        htmlBuilder.append("    text-align: center;");
        htmlBuilder.append("}");
        htmlBuilder.append(".buy-input, .amount-input {");
        htmlBuilder.append("    width: 100px;");
        htmlBuilder.append("    padding: 8px;");
        htmlBuilder.append("}");
        htmlBuilder.append(".buy-button, .add-amount-submit {");
        htmlBuilder.append("    padding: 12px 24px;");
        htmlBuilder.append("    background-color: #1da1f2;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    border: none;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    cursor: pointer;");
        htmlBuilder.append("    font-size: 16px;");
        htmlBuilder.append("}");
        htmlBuilder.append(".buy-button:hover, .add-amount-submit:hover {");
        htmlBuilder.append("    background-color: #ffffff;");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("}");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head><body>");
        htmlBuilder.append("<a href=\"/categories\"><button class=\"home-button\">Home</button></a>");
        htmlBuilder.append("<h1>").append(product.getName()).append(" - Edit Product</h1>");
        htmlBuilder.append("<div class=\"product-details\">");
        htmlBuilder.append("<p><strong>UPC:</strong> ").append(product.getUPC()).append("</p>");
        htmlBuilder.append("<p><strong>Name:</strong> ").append(product.getName()).append("</p>");
        htmlBuilder.append("<p><strong>Category:</strong> ").append(product.getCategory().getName()).append("</p>");
        htmlBuilder.append("<p><strong>Description:</strong> ").append(product.getCharacteristics()).append("</p>");
        htmlBuilder.append("<p><strong>Price:</strong> $").append(product.getSellingPrice()).append("</p>");
        htmlBuilder.append("<p><strong>Units left:</strong> ").append(product.getQuantity()).append("</p>");
        htmlBuilder.append("</div>");

        // Buy form handled by JavaScript
        htmlBuilder.append("<div class=\"buy-form\">");
        htmlBuilder.append("<label for=\"quantity\">Quantity to Buy:</label>");
        htmlBuilder.append("<input type=\"number\" id=\"quantity\" name=\"quantity\" class=\"buy-input\" min=\"1\" max=\"").append(product.getQuantity()).append("\" required>");
        htmlBuilder.append("<button id=\"buyButton\" class=\"buy-button\">Buy</button>");
        htmlBuilder.append("</div>");

        // Add amount form handled by JavaScript
        htmlBuilder.append("<div class=\"add-amount-form\">");
        htmlBuilder.append("<label for=\"amountToAdd\">Amount to Add:</label>");
        htmlBuilder.append("<input type=\"number\" id=\"amountToAdd\" name=\"amountToAdd\" class=\"amount-input\" min=\"1\" required>");
        htmlBuilder.append("<button id=\"addAmountButton\" class=\"add-amount-button\">Add Amount</button>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<a href=\"/edit/product?id=").append(product.getId()).append("\">");
        htmlBuilder.append("<button class=\"edit-button\">Edit</button></a>");
        htmlBuilder.append("<script>");
        htmlBuilder.append("document.getElementById('buyButton').addEventListener('click', function() {");
        htmlBuilder.append("    var quantity = parseInt(document.getElementById('quantity').value);");
        htmlBuilder.append("    if (quantity < 1 || quantity > ").append(product.getQuantity()).append(") {");
        htmlBuilder.append("        alert('Please enter a valid quantity.');");
        htmlBuilder.append("        return;");
        htmlBuilder.append("    }");
        htmlBuilder.append("    var xhr = new XMLHttpRequest();");
        htmlBuilder.append("    xhr.open('POST', '/product?id=").append(product.getId()).append("', true);");
        htmlBuilder.append("    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');");
        htmlBuilder.append("    xhr.onload = function() {");
        htmlBuilder.append("        if (xhr.status === 200) {");
        htmlBuilder.append("            alert('Purchase successful!');");
        htmlBuilder.append("            window.location.reload();");
        htmlBuilder.append("        } else {");
        htmlBuilder.append("            alert('Failed to process purchase. Please try again later.');");
        htmlBuilder.append("        }");
        htmlBuilder.append("    };");
        htmlBuilder.append("    xhr.onerror = function() {");
        htmlBuilder.append("        alert('Error occurred while processing purchase.');");
        htmlBuilder.append("    };");
        htmlBuilder.append("    xhr.send(JSON.stringify({ productId: ").append(product.getId()).append(", quantity: quantity }));");
        htmlBuilder.append("});");

        htmlBuilder.append("document.getElementById('addAmountButton').addEventListener('click', function() {");
        htmlBuilder.append("    var amount = parseInt(document.getElementById('amountToAdd').value);");
        htmlBuilder.append("    if (isNaN(amount) || amount < 1) {");
        htmlBuilder.append("        alert('Please enter a valid amount to add.');");
        htmlBuilder.append("        return;");
        htmlBuilder.append("    }");
        htmlBuilder.append("    var xhr = new XMLHttpRequest();");
        htmlBuilder.append("    xhr.open('POST', '/product?id=").append(product.getId()).append("', true);");
        htmlBuilder.append("    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');");
        htmlBuilder.append("    xhr.onload = function() {");
        htmlBuilder.append("        if (xhr.status === 200) {");
        htmlBuilder.append("            alert('Amount added successfully!');");
        htmlBuilder.append("            window.location.reload();");
        htmlBuilder.append("        } else {");
        htmlBuilder.append("            alert('Failed to add amount. Please try again later.');");
        htmlBuilder.append("        }");
        htmlBuilder.append("    };");
        htmlBuilder.append("    xhr.onerror = function() {");
        htmlBuilder.append("        alert('Error occurred while adding amount.');");
        htmlBuilder.append("    };");
        htmlBuilder.append("    xhr.send(JSON.stringify({ amount: amount }));");
        htmlBuilder.append("});");
        htmlBuilder.append("</script>");
        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    public static String generateEditProductHTML(Product product, List<Category> categories) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head><title>Edit Product</title>");
        {
            htmlBuilder.append("<style>");
            htmlBuilder.append("body {");
            htmlBuilder.append("    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;");
            htmlBuilder.append("    background-color: #15202b;");
            htmlBuilder.append("    color: #ffffff;");
            htmlBuilder.append("    padding: 20px;");
            htmlBuilder.append("}");
            htmlBuilder.append("h1 {");
            htmlBuilder.append("    color: #1da1f2;");
            htmlBuilder.append("    text-align: center;");
            htmlBuilder.append("}");
            htmlBuilder.append("form {");
            htmlBuilder.append("    margin-top: 20px;");
            htmlBuilder.append("    max-width: 600px;");
            htmlBuilder.append("    margin: 0 auto;");
            htmlBuilder.append("    background-color: #1a1a1a;");
            htmlBuilder.append("    padding: 20px;");
            htmlBuilder.append("    border-radius: 8px;");
            htmlBuilder.append("}");
            htmlBuilder.append("label {");
            htmlBuilder.append("    color: #ffffff;");
            htmlBuilder.append("}");
            htmlBuilder.append("input[type=text], input[type=number], textarea {");
            htmlBuilder.append("    width: 100%;");
            htmlBuilder.append("    padding: 10px;");
            htmlBuilder.append("    margin: 5px 0;");
            htmlBuilder.append("    display: inline-block;");
            htmlBuilder.append("    border: 1px solid #ccc;");
            htmlBuilder.append("    border-radius: 4px;");
            htmlBuilder.append("    box-sizing: border-box;");
            htmlBuilder.append("}");
            htmlBuilder.append("select {");
            htmlBuilder.append("    width: 100%;");
            htmlBuilder.append("    padding: 10px;");
            htmlBuilder.append("    margin: 5px 0;");
            htmlBuilder.append("    display: inline-block;");
            htmlBuilder.append("    border: 1px solid #ccc;");
            htmlBuilder.append("    border-radius: 4px;");
            htmlBuilder.append("    box-sizing: border-box;");
            htmlBuilder.append("}");
            htmlBuilder.append("button[type=submit], button[type=button] {");
            htmlBuilder.append("    margin-top: 10px;");
            htmlBuilder.append("    background-color: #1da1f2;");
            htmlBuilder.append("    color: #ffffff;");
            htmlBuilder.append("    padding: 10px 20px;");
            htmlBuilder.append("    border: none;");
            htmlBuilder.append("    border-radius: 4px;");
            htmlBuilder.append("    cursor: pointer;");
            htmlBuilder.append("}");
            htmlBuilder.append("button[type=submit]:hover, button[type=button]:hover {");
            htmlBuilder.append("    background-color: #ffffff;");
            htmlBuilder.append("    color: #1da1f2;");
            htmlBuilder.append("}");
            htmlBuilder.append("</style>");
        }
        htmlBuilder.append("</head><body>");

        // Navigation and header
        htmlBuilder.append("<a href=\"/categories\"><button class=\"home-button\">Home</button></a>");
        htmlBuilder.append("<h1>Edit Product</h1>");

        // Edit Form
        htmlBuilder.append("<form id=\"editForm\" action=\"/save/product\" method=\"POST\" onsubmit=\"return validateEditForm()\">");
        htmlBuilder.append("<input type=\"hidden\" name=\"productId\" value=\"").append(product.getId()).append("\">");
        htmlBuilder.append("<label for=\"name\">Name:</label><br>");
        htmlBuilder.append("<input type=\"text\" id=\"name\" name=\"name\" value=\"").append(product.getName()).append("\" required><br>");
        htmlBuilder.append("<label for=\"category\">Category:</label><br>");
        htmlBuilder.append("<select id=\"category\" name=\"category\">");

        // Populate category options
        for (Category category : categories) {
            htmlBuilder.append("<option value=\"").append(category.getName()).append("\"");
            if (product.getCategory().getName().equals(category.getName())) {
                htmlBuilder.append(" selected");
            }
            htmlBuilder.append(">").append(category.getName()).append("</option>");
        }
        htmlBuilder.append("</select><br>");
        htmlBuilder.append("<label for=\"description\">Description:</label><br>");
        htmlBuilder.append("<textarea id=\"description\" name=\"description\" required>").append(product.getCharacteristics()).append("</textarea><br>");
        htmlBuilder.append("<span id=\"descriptionError\" style=\"color: red; display: none;\">Description cannot be empty</span><br>");
        htmlBuilder.append("<label for=\"price\">Price:</label><br>");
        htmlBuilder.append("<input type=\"number\" id=\"price\" name=\"price\" step=\"any\" value=\"").append(product.getSellingPrice()).append("\" min=\"0\" required><br>");
        htmlBuilder.append("<span id=\"priceError\" style=\"color: red; display: none;\">Price must be greater than or equal to 0</span><br>");

        // Save and Delete buttons
        htmlBuilder.append("<button type=\"submit\">Save</button>");

        htmlBuilder.append("<button type=\"button\" onclick=\"deleteProduct('").append(product.getId()).append("')\">Delete</button>");

        htmlBuilder.append("</form>");

        // JavaScript for Form Validation and Delete Confirmation
        htmlBuilder.append("<script>");
        htmlBuilder.append("function validateEditForm() {");
        htmlBuilder.append("    return true;");
        htmlBuilder.append("}");

        // JavaScript for deleting a product
        htmlBuilder.append("function deleteProduct(productId) { // comment\n");
        htmlBuilder.append("    fetch('/delete/product', {");
        htmlBuilder.append("        method: 'DELETE',");
        htmlBuilder.append("        headers: {");
        htmlBuilder.append("            'Content-Type': 'application/json'");
        htmlBuilder.append("        },");
        htmlBuilder.append("        body: JSON.stringify({ productId: productId })");
        htmlBuilder.append("    })");
        htmlBuilder.append("    .then(response => {");
        htmlBuilder.append("        if (response.ok) {");
        htmlBuilder.append("            console.log('Product deleted successfully');");
        htmlBuilder.append("            window.location.href = '/categories';");  // Redirect to categories page after deletion
        htmlBuilder.append("        } else if (response.status === 404) {");
        htmlBuilder.append("            console.error('Endpoint not found (404)');");
        htmlBuilder.append("            alert('Error: Endpoint not found');");
        htmlBuilder.append("        } else {");
        htmlBuilder.append("            console.error('Failed to delete product:', response.statusText);");
        htmlBuilder.append("            alert('Error deleting product');");
        htmlBuilder.append("        }");
        htmlBuilder.append("    })");
        htmlBuilder.append("    .catch(error => {");
        htmlBuilder.append("        console.error('Error deleting product:', error);");
        htmlBuilder.append("        alert('Network error: Unable to delete product');");
        htmlBuilder.append("    });");
        htmlBuilder.append("}");
        htmlBuilder.append("</script>");

        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    public static Map<String, String> parseFormData(String formData) {
        Map<String, String> formDataMap = new HashMap<>();

        if (formData != null && !formData.isEmpty()) {
            String[] pairs = formData.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    // URL-decode the value if needed
                    value = java.net.URLDecoder.decode(value, java.nio.charset.StandardCharsets.UTF_8);
                    formDataMap.put(key, value);
                }
            }
        }

        return formDataMap;
    }

    public static String generateAddProductHTML(List<Category> categories) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head><title>Add Product</title>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body {");
        htmlBuilder.append("    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;");
        htmlBuilder.append("    background-color: #15202b;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    padding: 20px;");
        htmlBuilder.append("}");
        htmlBuilder.append("h1 {");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("    text-align: center;");
        htmlBuilder.append("}");
        htmlBuilder.append("form {");
        htmlBuilder.append("    margin-top: 20px;");
        htmlBuilder.append("    max-width: 600px;");
        htmlBuilder.append("    margin: 0 auto;");
        htmlBuilder.append("    background-color: #1a1a1a;");
        htmlBuilder.append("    padding: 20px;");
        htmlBuilder.append("    border-radius: 8px;");
        htmlBuilder.append("}");
        htmlBuilder.append("label {");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("}");
        htmlBuilder.append("input[type=text], input[type=number], textarea {");
        htmlBuilder.append("    width: 100%;");
        htmlBuilder.append("    padding: 10px;");
        htmlBuilder.append("    margin: 5px 0;");
        htmlBuilder.append("    display: inline-block;");
        htmlBuilder.append("    border: 1px solid #ccc;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    box-sizing: border-box;");
        htmlBuilder.append("}");
        htmlBuilder.append("select {");
        htmlBuilder.append("    width: 100%;");
        htmlBuilder.append("    padding: 10px;");
        htmlBuilder.append("    margin: 5px 0;");
        htmlBuilder.append("    display: inline-block;");
        htmlBuilder.append("    border: 1px solid #ccc;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    box-sizing: border-box;");
        htmlBuilder.append("}");
        htmlBuilder.append("button[type=submit] {");
        htmlBuilder.append("    margin-top: 10px;");
        htmlBuilder.append("    background-color: #1da1f2;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    padding: 10px 20px;");
        htmlBuilder.append("    border: none;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    cursor: pointer;");
        htmlBuilder.append("}");
        htmlBuilder.append("button[type=submit]:hover {");
        htmlBuilder.append("    background-color: #ffffff;");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("}");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head><body>");

        // Home Button
        htmlBuilder.append("<a href=\"/categories\"><button class=\"home-button\">Home</button></a>");

        // Heading
        htmlBuilder.append("<h1>Add Product</h1>");

        // Add Product Form
        htmlBuilder.append("<form id=\"addProductForm\" action=\"/insert/product\" method=\"POST\" onsubmit=\"return validateAddForm()\">");
        htmlBuilder.append("<label for=\"name\">Name:</label><br>");
        htmlBuilder.append("<input type=\"text\" id=\"name\" name=\"name\" required><br>");
        htmlBuilder.append("<label for=\"category\">Category:</label><br>");
        htmlBuilder.append("<select id=\"category\" name=\"category\">");

        // Populate category options
        for (Category category : categories) {
            htmlBuilder.append("<option value=\"").append(category.getName()).append("\">").append(category.getName()).append("</option>");
        }
        htmlBuilder.append("</select><br>");
        htmlBuilder.append("<label for=\"description\">Description:</label><br>");
        htmlBuilder.append("<textarea id=\"description\" name=\"description\" required></textarea><br>");
        htmlBuilder.append("<label for=\"price\">Price:</label><br>");
        htmlBuilder.append("<input type=\"number\" id=\"price\" name=\"price\" step=\"any\" min=\"0\" required><br>");
        htmlBuilder.append("<label for=\"upc\">UPC:</label><br>");
        htmlBuilder.append("<input type=\"text\" id=\"upc\" name=\"upc\" required><br>");
        htmlBuilder.append("<label for=\"quantity\">Quantity:</label><br>");
        htmlBuilder.append("<input type=\"number\" id=\"quantity\" name=\"quantity\" min=\"0\" required><br>");
        htmlBuilder.append("<button type=\"submit\">Add Product</button>");
        htmlBuilder.append("</form>");

        // JavaScript for Form Validation
        htmlBuilder.append("<script>");
        htmlBuilder.append("function validateAddForm() {");
        htmlBuilder.append("    var description = document.getElementById('description').value.trim();");
        htmlBuilder.append("    var price = document.getElementById('price').value.trim();");
        htmlBuilder.append("    var upc = document.getElementById('upc').value.trim();");
        htmlBuilder.append("    var quantity = document.getElementById('quantity').value.trim();");
        htmlBuilder.append("    if (description === '' || upc === '' || quantity === '') {");
        htmlBuilder.append("        alert('Please fill out all fields');");
        htmlBuilder.append("        return false;");
        htmlBuilder.append("    }");
        htmlBuilder.append("    if (parseFloat(price) < 0) {");
        htmlBuilder.append("        alert('Price must be greater than or equal to 0');");
        htmlBuilder.append("        return false;");
        htmlBuilder.append("    }");
        htmlBuilder.append("    return true;");
        htmlBuilder.append("}");
        htmlBuilder.append("</script>");

        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    public static String generateDynamicHTMLForStats(List<Product> products, List<Category> categories, double totalInventoryValue) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><head><title>Store Statistics</title>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body {");
        htmlBuilder.append("    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;");
        htmlBuilder.append("    background-color: #15202b;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    padding: 20px;");
        htmlBuilder.append("}");
        htmlBuilder.append("h1 {");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("    text-align: center;");
        htmlBuilder.append("}");
        htmlBuilder.append(".stats-section {");
        htmlBuilder.append("    margin-top: 20px;");
        htmlBuilder.append("}");
        htmlBuilder.append(".stats-title {");
        htmlBuilder.append("    font-size: 24px;");
        htmlBuilder.append("    font-weight: bold;");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("    margin-bottom: 10px;");
        htmlBuilder.append("}");
        htmlBuilder.append(".stats-item {");
        htmlBuilder.append("    margin-bottom: 5px;");
        htmlBuilder.append("}");
        htmlBuilder.append(".home-button {");
        htmlBuilder.append("    position: absolute;");
        htmlBuilder.append("    top: 10px;");
        htmlBuilder.append("    left: 10px;");
        htmlBuilder.append("    font-size: 14px;");
        htmlBuilder.append("    color: #ffffff;");
        htmlBuilder.append("    background-color: #1da1f2;");
        htmlBuilder.append("    border: none;");
        htmlBuilder.append("    padding: 10px 20px;");
        htmlBuilder.append("    border-radius: 4px;");
        htmlBuilder.append("    cursor: pointer;");
        htmlBuilder.append("}");
        htmlBuilder.append(".home-button:hover {");
        htmlBuilder.append("    background-color: #ffffff;");
        htmlBuilder.append("    color: #1da1f2;");
        htmlBuilder.append("}");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head><body>");

        // Home Button
        htmlBuilder.append("<a href=\"/categories\"><button class=\"home-button\">Home</button></a>");

        // Title
        htmlBuilder.append("<h1>Store Statistics</h1>");

        // Overall Inventory Stats
        htmlBuilder.append("<div class=\"stats-section\">");
        htmlBuilder.append("<div class=\"stats-title\">Overall Inventory Statistics</div>");
        htmlBuilder.append("<div class=\"stats-item\">Total Number of Products: ").append(products.size()).append("</div>");
        htmlBuilder.append("<div class=\"stats-item\">Total Inventory Value: $").append(String.format("%.2f", totalInventoryValue)).append("</div>");
        htmlBuilder.append("</div>");

        // Category-wise Stats
        htmlBuilder.append("<div class=\"stats-section\">");
        htmlBuilder.append("<div class=\"stats-title\">Category-wise Inventory Statistics</div>");
        for (Category category : categories) {
            List<Product> categoryProducts = new Product_DAO().findByCategory(category.getName());
            double categoryTotalValue = 0.0;
            for (Product product : categoryProducts) {
                categoryTotalValue += product.getQuantity() * product.getSellingPrice();
            }
            htmlBuilder.append("<div class=\"stats-item\">Category: ").append(category.getName()).append("</div>");
            htmlBuilder.append("<div class=\"stats-item\">Number of Products: ").append(categoryProducts.size()).append("</div>");
            htmlBuilder.append("<div class=\"stats-item\">Total Inventory Value: $").append(String.format("%.2f", categoryTotalValue)).append("</div>");
            htmlBuilder.append("<br>");
        }
        htmlBuilder.append("</div>");

        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }


}

