package com.COmanager.CO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.COmanager.CO.repository.MilestoneRepository;
import com.COmanager.CO.controllers.MilestoneController;
import com.COmanager.CO.models.Milestone;

@WebMvcTest(MilestoneController.class)
@WebAppConfiguration
public class MilestoneControllerTest {

    @MockBean
    private MilestoneRepository milestoneRepository;

    @InjectMocks
    private MilestoneController milestoneController;

    private MockMvc mockMvc;
    private MockHttpSession session;
    private MockHttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(milestoneController).build();
        session = new MockHttpSession();
        request = new MockHttpServletRequest();
        request.setSession(session);
    }

    @Test
    public void testCreateMilestone() throws Exception {
        // Create a sample milestone for testing
        Milestone milestone = new Milestone();
        milestone.setDescription("Sample milestone");
        milestone.setDate(new Date());
        milestone.setAmount(500.0);

        when(milestoneRepository.save(any())).thenReturn(milestone);

        // Perform the POST request to create a milestone
        mockMvc.perform(post("/api/mile/create/{orderNumber}", "ABC123")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Sample milestone\",\"date\":\"2023-09-20\",\"amount\":500.0}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Milestone created successfully!"));

        // Verify that the milestone was saved
        verify(milestoneRepository, times(1)).save(any());
    }

    @Test
    public void testGetAllMilestones() throws Exception {
        // Create a list of sample milestones
        List<Milestone> milestones = new ArrayList<>();
        Milestone milestone1 = new Milestone();
        milestone1.setDescription("Milestone 1");
        milestones.add(milestone1);

        when(milestoneRepository.findAll()).thenReturn(milestones);

        // Perform the GET request to retrieve all milestones
        mockMvc.perform(get("/api/mile/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].description").value("Milestone 1"));

        // Verify that the repository method was called
        verify(milestoneRepository, times(1)).findAll();
    }
    
    @Test
    public void testGetAllMilestonesByOrderNumber() throws Exception {
        String orderNumber = "123";
        List<Milestone> milestones = new ArrayList<>();
        milestones.add(new Milestone());
        milestones.add(new Milestone());

        when(milestoneRepository.findByOrderNumber(orderNumber)).thenReturn(milestones);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/mile/getall/" + orderNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));

        verify(milestoneRepository, times(1)).findByOrderNumber(orderNumber);
    }

    @Test
    public void testUpdatePaidStatus() throws Exception {
        String milestoneId = "456";
        Milestone milestone = new Milestone();
        milestone.setPaid(false);

        when(milestoneRepository.findByMilestoneId(milestoneId)).thenReturn(Optional.of(milestone));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/mile/updatepaid/" + milestoneId))
                .andExpect(status().isOk())
                .andExpect(content().string("Paid status updated successfully!"));

        assertTrue(milestone.isPaid());
        verify(milestoneRepository, times(1)).findByMilestoneId(milestoneId);
        verify(milestoneRepository, times(1)).save(milestone);
    }

    @Test
    public void testUpdateUnpaidStatus() throws Exception {
        String milestoneId = "789";
        Milestone milestone = new Milestone();
        milestone.setPaid(true);

        when(milestoneRepository.findByMilestoneId(milestoneId)).thenReturn(Optional.of(milestone));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/mile/updateunpaid/" + milestoneId))
                .andExpect(status().isOk())
                .andExpect(content().string("Unpaid status updated successfully!"));

        assertFalse(milestone.isPaid());
        verify(milestoneRepository, times(1)).findByMilestoneId(milestoneId);
        verify(milestoneRepository, times(1)).save(milestone);
    }

    @Test
    public void testCalculateTotalAmountByOrderNumber() throws Exception {
        String orderNumber = "123";
        List<Milestone> paidMilestones = new ArrayList<>();
        Milestone milestone1 = new Milestone();
        milestone1.setAmount(100.0);
        Milestone milestone2 = new Milestone();
        milestone2.setAmount(200.0);
        paidMilestones.add(milestone1);
        paidMilestones.add(milestone2);

        when(milestoneRepository.findByOrderNumberAndPaid(orderNumber, true)).thenReturn(paidMilestones);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/mile/totalpaidAmount/" + orderNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("300.0"));

        verify(milestoneRepository, times(1)).findByOrderNumberAndPaid(orderNumber, true);
    }

    @Test
    public void testCalculateTotalUnpaidAmountByOrderNumber() throws Exception {
        String orderNumber = "123";
        List<Milestone> unpaidMilestones = new ArrayList<>();
        Milestone milestone1 = new Milestone();
        milestone1.setAmount(100.0);
        Milestone milestone2 = new Milestone();
        milestone2.setAmount(200.0);
        unpaidMilestones.add(milestone1);
        unpaidMilestones.add(milestone2);

        when(milestoneRepository.findByOrderNumberAndPaid(orderNumber, false)).thenReturn(unpaidMilestones);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/mile/totalunpaidAmount/" + orderNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("300.0"));

        verify(milestoneRepository, times(1)).findByOrderNumberAndPaid(orderNumber, false);
    }

    @Test
    public void testUpdateMilestone() throws Exception {
        String milestoneId = "456";
        Milestone milestone = new Milestone();
        milestone.setDescription("Original Description");

        Milestone updatedMilestone = new Milestone();
        updatedMilestone.setDescription("Updated Description");

        when(milestoneRepository.findByMilestoneId(milestoneId)).thenReturn(Optional.of(milestone));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/mile/update/" + milestoneId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedMilestone)))
                .andExpect(status().isOk())
                .andExpect(content().string("Milestone updated successfully!"));

        assertEquals(updatedMilestone.getDescription(), milestone.getDescription());
        verify(milestoneRepository, times(1)).findByMilestoneId(milestoneId);
        verify(milestoneRepository, times(1)).save(milestone);
    }

    @Test
    public void testDeleteMilestone() throws Exception {
        String milestoneId = "456";
        Milestone milestone = new Milestone();

        when(milestoneRepository.findByMilestoneId(milestoneId)).thenReturn(Optional.of(milestone));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/mile/delete/" + milestoneId))
                .andExpect(status().isOk())
                .andExpect(content().string("Milestone deleted successfully!"));

        verify(milestoneRepository, times(1)).findByMilestoneId(milestoneId);
        verify(milestoneRepository, times(1)).delete(milestone);
    }

    // Utility method to convert an object to JSON string
    private static String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
