**Pharmacy Sales – Database Design (Recruitment Task)**
This folder contains a simple database design for pharmacy sales (products, agents, documents, and items).

The structure is normalized (3NF) and described in:
[Schema SQL](src/schema.sql)

Two SQL queries required by the task are located in:
[Queries](src/queries.sql)

1. Total quantity of each product sold in June 2024.
2. Products sold on invoices (FAKTURA) per contractor in the same period.

Optional test data for local checking:
[Test data](src/test-data.sql)

**Folder structure:**

    pharmacy-sales-db/    
        ├── schema.sql
        ├── queries.sql
        ├── test-data.sql
        └── README.md

**ER Diagram**
![img.png](src/img.png)
