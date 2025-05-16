#!/bin/bash
# Test commands for the Spring Boot REST API
# Save this file as test-api.sh and run with: bash test-api.sh

# Set the base URL
BASE_URL="http://localhost:8082/api"

echo "-----------------------------------------------"
echo "Testing Product API Endpoints"
echo "-----------------------------------------------"

# 1. Get all products
echo -e "\n1. Getting all products:"
curl -X GET $BASE_URL/products | json_pp

# 2. Get a specific product
echo -e "\n2. Getting product with ID 1:"
curl -X GET $BASE_URL/products/1 | json_pp

# 3. Create a new product
echo -e "\n3. Creating a new product:"
curl -X POST $BASE_URL/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Wireless Mouse",
    "description": "Ergonomic wireless mouse",
    "price": 49.99,
    "stockQuantity": 30
  }' | json_pp

# 4. Update a product
echo -e "\n4. Updating product with ID 1:"
curl -X PUT $BASE_URL/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Premium Laptop",
    "description": "High-performance laptop with enhanced features",
    "price": 1499.99,
    "stockQuantity": 5
  }' | json_pp

# 5. Delete a product
echo -e "\n5. Deleting product with ID 3:"
curl -X DELETE $BASE_URL/products/3 -v

echo -e "\n6. Verifying deletion - Getting all products again:"
curl -X GET $BASE_URL/products | json_pp

echo -e "\n-----------------------------------------------"
echo "Testing Customer API Endpoints"
echo "-----------------------------------------------"

# 1. Get all customers
echo -e "\n1. Getting all customers:"
curl -X GET $BASE_URL/customers | json_pp

# 2. Get a specific customer
echo -e "\n2. Getting customer with ID 1:"
curl -X GET $BASE_URL/customers/1 | json_pp

# 3. Create a new customer
echo -e "\n3. Creating a new customer:"
curl -X POST $BASE_URL/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Sarah",
    "lastName": "Miller",
    "dob": 1992,
    "balance": 4500.50
  }' | json_pp

# 4. Update a customer
echo -e "\n4. Updating customer with ID 1:"
curl -X PUT $BASE_URL/customers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "dob": 1985,
    "balance": 6000.00
  }' | json_pp

# 5. Delete a customer
echo -e "\n5. Deleting customer with ID 5:"
curl -X DELETE $BASE_URL/customers/5 -v

echo -e "\n6. Verifying deletion - Getting all customers again:"
curl -X GET $BASE_URL/customers | json_pp

echo -e "\n-----------------------------------------------"
echo "Testing H2 Console Access"
echo "-----------------------------------------------"
echo "H2 Console is available at: http://localhost:8082/h2-console"
echo "JDBC URL: jdbc:h2:mem:productdb"
echo "Username: sa"
echo "Password: (leave empty)"
echo "-----------------------------------------------"

# Individual cURL commands for reference

# PRODUCT COMMANDS
# Get all products
# curl -X GET http://localhost:8082/api/products

# Get product by ID
# curl -X GET http://localhost:8082/api/products/1

# Create product
# curl -X POST http://localhost:8082/api/products -H "Content-Type: application/json" -d '{"name": "Wireless Mouse", "description": "Ergonomic wireless mouse", "price": 49.99, "stockQuantity": 30}'

# Update product
# curl -X PUT http://localhost:8082/api/products/1 -H "Content-Type: application/json" -d '{"name": "Premium Laptop", "description": "High-performance laptop with enhanced features", "price": 1499.99, "stockQuantity": 5}'

# Delete product
# curl -X DELETE http://localhost:8082/api/products/3

# CUSTOMER COMMANDS
# Get all customers
# curl -X GET http://localhost:8082/api/customers

# Get customer by ID
# curl -X GET http://localhost:8082/api/customers/1

# Create customer
# curl -X POST http://localhost:8082/api/customers -H "Content-Type: application/json" -d '{"firstName": "Sarah", "lastName": "Miller", "dob": 1992, "balance": 4500.50}'

# Update customer
# curl -X PUT http://localhost:8082/api/customers/1 -H "Content-Type: application/json" -d '{"firstName": "John", "lastName": "Doe", "dob": 1985, "balance": 6000.00}'

# Delete customer
# curl -X DELETE http://localhost:8082/api/customers/5
