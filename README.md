# :package: ShirtStore Project 

 ## Abstract
The E-Commerce website for the shirt store uses HTML, CSS, Javascript, Kotlin, Spring Framework, Thymeleaf, Hibernate, and MYSQL. The key functionality focuses on user signup and login, user profile management, product management, shopping cart, order checkout and order history, and automatic email confirmation.

---

## :rocket: Snapshot
Below is an animated overview of the ShirtStore platform's functionality. Click on each section below to expand and view a screencast GIF demonstrating the feature in action.

**Navigation Path**:  
**Step 1: My Account Dashboard** → **Step 2: Browse Shirt** → **Step 3: Shopping Cart** → **Step 4: Admin Portal – User and Shirt Management**

<details>
<summary>:arrow_right: <b>My Account Dashboard</b></summary>
Manage your account, view order history, and update preferences.

![Dashboard](https://github.com/user-attachments/assets/0419fac9-82c6-4e85-af15-0a079b0602ee)
</details>

<details>
<summary>:arrow_right: <b>Browse Shirt</b></summary>
Easily browse and filter a wide variety of shirts.

![BrowseShirt](https://github.com/user-attachments/assets/ee469f88-60fd-4c85-8819-30c26a83152a)
</details>

<details>
<summary>:arrow_right: <b>Shopping Cart</b></summary>
Add items, adjust quantities, and seamlessly proceed to checkout.

![ShoppingCart](https://github.com/user-attachments/assets/bc2142f0-531d-4773-a6b2-48bfcc4a005d)
</details>

<details>
<summary>:arrow_right: <b>Admin Portal – User and Shirt Management</b></summary>
Powerful tools for managing users, inventory, and analytics.

![Admin](https://github.com/user-attachments/assets/26d0dfb4-7832-45fb-aef6-092d9c767efa)
</details>


---
## :star: Feature Highlights
Discover the basic characteristics of ShirtStore, which are organized in the following order:

### 1.
<p align="center">
   <img src="https://github.com/user-attachments/assets/70309fed-cf43-4abd-8bac-aab19b859d6d" alt="My Account Dashboard" width="80%">
</p>


---

### 2. 
<p align="center">
  <img src="https://github.com/user-attachments/assets/52702659-6a0c-4f14-ae8c-49df96665bd3" alt="Browse Shirt" width="80%">
</p>

---

### 3.
<p align="center">
  <img src="https://github.com/user-attachments/assets/869a238a-d114-4fc8-90d9-ef249793b07e" alt="Shopping Cart" width="80%">
</p>

---

### 4.
<p align="center">
  <img src="https://github.com/user-attachments/assets/33973fe3-bbdb-4434-972d-bf5aa5a15dba" alt="Admin Portal" width="80%">
</p>

---
## :chart_with_upwards_trend: Entity-Relationship Diagram (ERD)

<p align="center">
  <img src="https://github.com/user-attachments/assets/614052d6-d0af-47f4-a014-0ecd7a603b48" alt="ERD Diagram" width="80%">
</p>

---

## :hammer: Implementation Tools

### Shirtstore (User-Facing Front-End)
- **Frontend**:
  - **CSS**: Styles are designed to be attractive in appearance.
  - **HTML**: Creates the layout of the website's page.
  - **Kotlin**: Utilize backend functionality and pass back to HTML
  - **JavaScript**: Pass the user interaction from frontend to backend.
  - **Thymeleaf**: Renders changes in data across websites, using Spring Boot.
- **Backend**:
  - **Kotlin**: Design functionality or utilize the functionality exit on the interface. 
  - **Spring Boot**: Manages business logic and consumer relationships.
  - **MySQL**: Handles and saves data.
  - **Spring Security**: Ensures safe verification and entry restriction.

### AdminPortal (Administrative Layout) 
- **Frontend**:
  - **Kotlin** Utilize backend functionality and pass back to HTML 
  - **CSS**: Styles manage the interface in a neat and expert manner.
  - **HTML**: Creates the layout of the administrative portal.
  - **Thymeleaf**: Renders interactive material to improve the operation of administration.
- **Backend**:
  - **Kotlin**: Design functionality or utilize the functionality exit on the interface. 
  - **JavaScript**: Transfer input from users from frontend to backend.
  - **MySQL**: Handles data about users. 
  - **Spring Boot**: Manages operational logic and data processes.
  - **Spring Security**: Ensures safe verification and role-based authorization.

### Shared Resources
- **Database**:
  - **MySQL**: To ensure uniformity of data, Shirtstore and AdminPortal share the same database.
- **Security**:
  - **Spring Security**: Authorization along with access control are shared throughout the two services.

---


## :arrow_right: How to Run the Project

```bash
# Download the project
git clone [https://github.com/jianghuang588/ecommerce_website_java.git](https://github.com/jianghuang588/ecommerce_website_kotlin.git)

# Configure the MySQL database
# Ensure that MySQL is operating locally.
# Initialize a schema in MySQL
# The settings in application.properties must correspond with the database information.


# Open the development environment (Eclipse, IntelliJ IDEA)
# Import the existing maven project into your IDE 
# Navigate to the root directory of ecommerce_website_kotlin
# Press finish

# Construct and manage the project
# Verify the resolution of Maven dependencies.
# Launch the main class as a Kotlin application after locating it.

# Start the application
# Standard user:
#   - Access "http://localhost:8080/" in your web.
#   - Utilize hardcoded user:
#       - Username: h
#       - Password: j
#   - Or create a new user account using the application's interface.

# Admin role:
#   - Access "http://localhost:8081/admin/login" in your web.
#   - Utilize hardcoded user:
#       - Username: admin
#       - Password: admin




