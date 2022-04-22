# Courier Management System

## Problem Statement

Customer gets a quote by entering source pincode, destination pincode, shipment dimensions (length, width, height and
weight). He likes the quote and goes to the nearest agency to book his shipment. Agent verifies the route and price
details, receives booking amount from the customer and books the shipment. He shares the receipt with the customer which
has consignment number in it which can be used for all tracking purposes. Shipments from across all the agencies are
moved to the local branches at regular interval. Shipments can also arrive at an intermediate branch which falls in the
pre-determined route between a given source and destination pincodes. Employee at a branch receives the shipment and
based upon the route, he either forwards it to another branch via one of the transportation media viz. Air, Railway,
Road or if it is the home branch of the destination address, then he forwards it into the bucket of delivery agents.
Agents at every branch timely checks their buckets for any fresh or undelivered shipments. Agent picks the shipment from
his bucket and attempts its delivery at the destination address. Finally, a shipment either gets delivered or left
undelivered. Agent updates the final status of the shipment along with optional remarks.

### Assumptions

- Distance between any two pincodes is generated randomly.
- Route between any two pincodes is generated randomly.
- To calculate the booking amount, a rate card is chosen.