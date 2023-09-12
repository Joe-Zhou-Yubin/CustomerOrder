package com.COmanager.CO.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.COmanager.CO.models.Milestone;
import com.COmanager.CO.repository.MilestoneRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/mile")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MilestoneController {

    @Autowired
    private MilestoneRepository milestoneRepository;

    @PostMapping("/create/{orderNumber}")
    public ResponseEntity<?> createMilestone(
        @PathVariable String orderNumber,
        @RequestBody Milestone milestone
    ) {
        try {
            // Generate a unique 6-character alphanumeric milestone ID using UUID
            String milestoneId = generateRandomMilestoneId();
            
            Milestone newMilestone = new Milestone();
            newMilestone.setMilestoneId(milestoneId); // Set milestone ID
            newMilestone.setOrderNumber(orderNumber);
            newMilestone.setDescription(milestone.getDescription());
            newMilestone.setDate(milestone.getDate());
            newMilestone.setAmount(milestone.getAmount());
            
            // Ensure paid is initialized to false
            newMilestone.setPaid(false);

            milestoneRepository.save(newMilestone);

            return ResponseEntity.ok("Milestone created successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating milestone: " + e.getMessage());
        }
    }

    
 // Generate a unique 6-character alphanumeric order number using UUID
    private String generateRandomMilestoneId() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 6);
        return uuidStr;
    }
    
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllMilestones() {
        try {
            List<Milestone> milestones = milestoneRepository.findAll();
            return ResponseEntity.ok(milestones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving milestones: " + e.getMessage());
        }
    }
    
    @GetMapping("/getall/{orderNumber}")
    public ResponseEntity<?> getAllMilestonesByOrderNumber(@PathVariable String orderNumber) {
        try {
            List<Milestone> milestones = milestoneRepository.findByOrderNumber(orderNumber);
            return ResponseEntity.ok(milestones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving milestones by order number: " + e.getMessage());
        }
    }
    
    @PutMapping("/updatepaid/{milestoneId}")
    public ResponseEntity<?> updatePaidStatus(@PathVariable String milestoneId) {
        try {
            Optional<Milestone> milestoneOptional = milestoneRepository.findByMilestoneId(milestoneId);
            if (milestoneOptional.isPresent()) {
                Milestone milestone = milestoneOptional.get();
                
                // Set the paid status to true
                milestone.setPaid(true);
                
                milestoneRepository.save(milestone);

                return ResponseEntity.ok("Paid status updated successfully!");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating paid status: " + e.getMessage());
        }
    }
    
    @PutMapping("/updateunpaid/{milestoneId}")
    public ResponseEntity<?> updateUnpaidStatus(@PathVariable String milestoneId) {
        try {
            Optional<Milestone> milestoneOptional = milestoneRepository.findByMilestoneId(milestoneId);
            if (milestoneOptional.isPresent()) {
                Milestone milestone = milestoneOptional.get();
                
                // Set the paid status to true
                milestone.setPaid(false);
                
                milestoneRepository.save(milestone);

                return ResponseEntity.ok("Unpaid status updated successfully!");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating paid status: " + e.getMessage());
        }
    }
    
    @GetMapping("/totalpaidAmount/{orderNumber}")
    public ResponseEntity<?> calculateTotalAmountByOrderNumber(@PathVariable String orderNumber) {
        try {
            List<Milestone> paidMilestones = milestoneRepository.findByOrderNumberAndPaid(orderNumber, true);

            double totalAmount = 0.0;
            for (Milestone milestone : paidMilestones) {
                totalAmount += milestone.getAmount();
            }

            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error calculating total amount: " + e.getMessage());
        }
    }
    
    @GetMapping("/totalunpaidAmount/{orderNumber}")
    public ResponseEntity<?> calculateTotalUnpaidAmountByOrderNumber(@PathVariable String orderNumber) {
        try {
            List<Milestone> paidMilestones = milestoneRepository.findByOrderNumberAndPaid(orderNumber, false);

            double totalAmount = 0.0;
            for (Milestone milestone : paidMilestones) {
                totalAmount += milestone.getAmount();
            }

            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error calculating total amount: " + e.getMessage());
        }
    }
    
    
    @PutMapping("/update/{milestoneId}")
    public ResponseEntity<?> updateMilestone(
        @PathVariable String milestoneId,
        @RequestBody Milestone updatedMilestone
    ) {
        try {
            Optional<Milestone> milestoneOptional = milestoneRepository.findByMilestoneId(milestoneId);
            if (milestoneOptional.isPresent()) {
                Milestone milestone = milestoneOptional.get();

                // Update the fields with the new values
                milestone.setDescription(updatedMilestone.getDescription());
                milestone.setDate(updatedMilestone.getDate());
                milestone.setAmount(updatedMilestone.getAmount());
                milestone.setPaid(updatedMilestone.isPaid());

                milestoneRepository.save(milestone);

                return ResponseEntity.ok("Milestone updated successfully!");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating milestone: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{milestoneId}")
    public ResponseEntity<?> deleteMilestone(@PathVariable String milestoneId) {
        try {
            Optional<Milestone> milestoneOptional = milestoneRepository.findByMilestoneId(milestoneId);
            if (milestoneOptional.isPresent()) {
                Milestone milestone = milestoneOptional.get();

                milestoneRepository.delete(milestone);

                return ResponseEntity.ok("Milestone deleted successfully!");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting milestone: " + e.getMessage());
        }
    }


}
