package com.COmanager.CO.controllers;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.COmanager.CO.models.CustomerOrder;
import com.COmanager.CO.models.Milestone;
import com.COmanager.CO.repository.CustomerOrderRepository;

@RestController
@RequestMapping("/api/co")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomerOrderController {

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createCustomerOrder(@RequestBody CustomerOrder customerOrder) {
        try {
            // Generate a unique 6-character alphanumeric order number using UUID
            String orderNumber = generateRandomOrderNumber();

            // Create a new CustomerOrder object and populate its fields
            CustomerOrder newCustomerOrder = new CustomerOrder();
            newCustomerOrder.setOrderNumber(orderNumber);
            newCustomerOrder.setVendor(customerOrder.getVendor());

            // Set the date created field directly with the current date
            newCustomerOrder.setDateCreated(new Date());

            newCustomerOrder.setCurrency(customerOrder.getCurrency());
            newCustomerOrder.setTotalAmount(customerOrder.getTotalAmount());
            newCustomerOrder.setStartDate(customerOrder.getStartDate());
            newCustomerOrder.setEndDate(customerOrder.getEndDate());
            newCustomerOrder.setType(customerOrder.getType());
            newCustomerOrder.setStatus(false);

            // Save the new customer order
            customerOrderRepository.save(newCustomerOrder);

            return ResponseEntity.ok("Customer order created successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating customer order: " + e.getMessage());
        }
    }

    // Generate a unique 6-character alphanumeric order number using UUID
    private String generateRandomOrderNumber() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 6);
        return uuidStr;
    }
    
    //delete based on order id
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteCustomerOrder(@PathVariable Long orderId) {
        try {
            // Check if the customer order exists
            Optional<CustomerOrder> customerOrderOptional = customerOrderRepository.findById(orderId);
            if (customerOrderOptional.isPresent()) {
                CustomerOrder customerOrder = customerOrderOptional.get();
                
                // Delete the customer order from the repository
                customerOrderRepository.delete(customerOrder);
                
                return ResponseEntity.ok("Customer order deleted successfully!");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting customer order: " + e.getMessage());
        }
    }
    //delete based on order number
    @DeleteMapping("/deleteon/{orderNumber}")
    public ResponseEntity<?> deleteCustomerOrderByOrderNumber(@PathVariable String orderNumber) {
        try {
            // Find the customer order by order number
            Optional<CustomerOrder> customerOrderOptional = customerOrderRepository.findByOrderNumber(orderNumber);
            if (customerOrderOptional.isPresent()) {
                CustomerOrder customerOrder = customerOrderOptional.get();

                // Delete the customer order from the repository
                customerOrderRepository.delete(customerOrder);

                return ResponseEntity.ok("Customer order deleted successfully!");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting customer order: " + e.getMessage());
        }
    }
    
    @PutMapping("/update/{orderNumber}")
    public ResponseEntity<?> updateCustomerOrderByOrderNumber(@PathVariable String orderNumber, @RequestBody CustomerOrder updatedCustomerOrder) {
        try {
            // Find the customer order by order number
            Optional<CustomerOrder> customerOrderOptional = customerOrderRepository.findByOrderNumber(orderNumber);
            if (customerOrderOptional.isPresent()) {
                CustomerOrder customerOrder = customerOrderOptional.get();

                // Update the fields with the new values
                customerOrder.setVendor(updatedCustomerOrder.getVendor());
                customerOrder.setCurrency(updatedCustomerOrder.getCurrency());
                customerOrder.setTotalAmount(updatedCustomerOrder.getTotalAmount());
                customerOrder.setStartDate(updatedCustomerOrder.getStartDate());
                customerOrder.setEndDate(updatedCustomerOrder.getEndDate());
                customerOrder.setType(updatedCustomerOrder.getType());

                // Save the updated customer order
                customerOrderRepository.save(customerOrder);

                return ResponseEntity.ok("Customer order updated successfully!");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating customer order: " + e.getMessage());
        }
    }
    
    //get all co
    @GetMapping("/all")
    public ResponseEntity<?> getAllCustomerOrders(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        try {
            // Create a Pageable object for pagination
            Pageable pageable = PageRequest.of(page, size);

            // Find all customer orders with pagination
            Page<CustomerOrder> customerOrders = customerOrderRepository.findAll(pageable);

            return ResponseEntity.ok(customerOrders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving customer orders: " + e.getMessage());
        }
    }

    
    //get specific co
    @GetMapping("/get/{orderNumber}")
    public ResponseEntity<?> getCustomerOrderByOrderNumber(@PathVariable String orderNumber) {
        try {
            // Find the customer order by order number
            Optional<CustomerOrder> customerOrderOptional = customerOrderRepository.findByOrderNumber(orderNumber);
            if (customerOrderOptional.isPresent()) {
                CustomerOrder customerOrder = customerOrderOptional.get();
                return ResponseEntity.ok(customerOrder);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving customer order: " + e.getMessage());
        }
    }
    
    @PutMapping("/updateStatus/{orderNumber}")
    public ResponseEntity<?> updateCustomerOrderStatusByOrderNumber(@PathVariable String orderNumber) {
        try {
            // Find the customer order by order number
            Optional<CustomerOrder> customerOrderOptional = customerOrderRepository.findByOrderNumber(orderNumber);
            if (customerOrderOptional.isPresent()) {
                CustomerOrder customerOrder = customerOrderOptional.get();

                // Update the status to true
                customerOrder.setStatus(true);

                // Save the updated customer order
                customerOrderRepository.save(customerOrder);

                return ResponseEntity.ok("Customer order status updated to true!");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating customer order status: " + e.getMessage());
        }
    }
    
    @PutMapping("/unupdateStatus/{orderNumber}")
    public ResponseEntity<?> unupdateCustomerOrderStatusByOrderNumber(@PathVariable String orderNumber) {
        try {
            // Find the customer order by order number
            Optional<CustomerOrder> customerOrderOptional = customerOrderRepository.findByOrderNumber(orderNumber);
            if (customerOrderOptional.isPresent()) {
                CustomerOrder customerOrder = customerOrderOptional.get();

                // Update the status to true
                customerOrder.setStatus(false);

                // Save the updated customer order
                customerOrderRepository.save(customerOrder);

                return ResponseEntity.ok("Customer order status updated to false!");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating customer order status: " + e.getMessage());
        }
    }

    
    @GetMapping("/getAllEnterpriseOrders")
    public ResponseEntity<?> getAllEnterpriseOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            // Create a PageRequest to specify the page and size
            PageRequest pageRequest = PageRequest.of(page, size);

            // Find all customer orders of type "enterprise" with pagination
            Page<CustomerOrder> enterpriseOrdersPage = customerOrderRepository.findByType("enterprise", pageRequest);

            return ResponseEntity.ok(enterpriseOrdersPage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving enterprise orders: " + e.getMessage());
        }
    }

    @GetMapping("/getAllTalentOrders")
    public ResponseEntity<?> getAllTalentOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            // Create a PageRequest to specify the page and size
            PageRequest pageRequest = PageRequest.of(page, size);

            // Find all customer orders of type "talent" with pagination
            Page<CustomerOrder> talentOrdersPage = customerOrderRepository.findByType("talent", pageRequest);

            return ResponseEntity.ok(talentOrdersPage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving talent orders: " + e.getMessage());
        }
    }

    


}
