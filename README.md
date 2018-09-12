# pharmacyinventory

Data Model

Inventory

Product
=======
ProductCode (MED-ID-Date)
Name (Item A)
Category (Prescription/Non-Prescription)
Manufacturer
ReleaseDate

Stock
=====
ProductCode
QuantityInStock
ReorderLevel (Min)
CountedBy
CountedByDate
DiscontinueDate

Reorder
=======
ProductCode
ReorderBy
ReorderByDate
SupplierID


Order (Send Sms)
=====
OrderID
ProductCode
CustomerID
Quantity
SellingPrice
ReceiptNo


Customer
========
CustomerID
Name
PhoneNo
emailNo""
