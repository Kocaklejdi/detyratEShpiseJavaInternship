-- Point 1
-- Find the Customer who has the most orders. 
--	If more than 1 customer has the most orders then all customers should be displayed

WITH customer_orders AS (
    SELECT 
        c.customerName, 
        COUNT(o.orderNumber) AS order_count
    FROM 
        customers c
    JOIN 
        orders o 
    ON 
        c.customerNumber = o.customerNumber
    GROUP BY 
        c.customerName
)
SELECT 
    customerName, 
    order_count
FROM 
    customer_orders
WHERE 
    order_count = (SELECT MAX(order_count) FROM customer_orders);
    
-------------------------------------------------------------------------------------------------

-- Point 2
-- View all “Germany” customers and their orderdetails. 
-- If a customer has not made any orders then he should not be included in the result.

SELECT 
	c.customername, od.* 
FROM 
	customers c inner JOIN orders o on(c.customerNumber = o.customerNumber) inner JOIN orderdetails od ON (o.orderNumber = od.orderNumber) 
WHERE 
	c.country = "Germany" 
ORDER BY 
   c.customerNumber, o.orderNumber;

---------------------------------------------------------------------------------

-- Point 3
-- List all employees and the their revenue amount (based on payments table)

SELECT 
	e.employeeNumber,e.firstName, e.lastName, SUM(p.amount)
FROM 
   employees e LEFT JOIN customers c ON (e.employeeNumber = c.salesRepEmployeeNumber)
   LEFT join payments p ON (c.customerNumber = p.customerNumber) 
GROUP BY
	e.employeeNumber
ORDER BY
	SUM(p.amount) desc

--------------------------------------------------------------------------------

-- Point 4
-- List all products which have been ordered the last month. 
-- (since the database is a bit old we assume we are now at 2005-01-01 )

SELECT
 pr.productName
FROM
	orders o INNER JOIN orderdetails od ON (o.orderNumber = od.orderNumber)	INNER JOIN products pr ON (pr.productCode = od.productCode)
WHERE 
	o.orderDate BETWEEN ("2004-11-01") AND ("2005-01-01")
GROUP BY
	pr.productName

-----------------------------------------------------------------------------

-- Point 5
--	Create a new table named employeedetails which should contain data like:
-- 	bankAccount
-- 	address
-- 	phoneNumber
-- 	personalEmail


CREATE TABLE employeedetails (
	employeeNumber INT(10) NOT NULL,
	bankAccount VARCHAR(20) NOT NULL,
	address TEXT NOT NULL,
	phoneNumber VARCHAR(15) NOT NULL,
	personalEmail VARCHAR(255) NOT NULL,
	PRIMARY KEY (employeeNumber)
);

-----------------------------------------------------------------------------------	