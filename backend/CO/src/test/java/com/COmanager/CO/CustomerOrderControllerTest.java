package com.COmanager.CO;
import com.COmanager.CO.controllers.CustomerOrderController;
import com.COmanager.CO.models.CustomerOrder;
import com.COmanager.CO.repository.CustomerOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerOrderController.class)
@WebAppConfiguration
public class CustomerOrderControllerTest {

    @MockBean
    private CustomerOrderRepository customerOrderRepository;

    @InjectMocks
    private CustomerOrderController customerOrderController;

    private MockMvc mockMvc;
    private MockHttpSession session;
    private MockHttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerOrderController).build();
        session = new MockHttpSession();
        request = new MockHttpServletRequest();
        request.setSession(session);
    }

    @Test
    public void testCreateCustomerOrder() throws Exception {
        // Create a sample customer order for testing
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setVendor("VendorName");
        customerOrder.setCurrency("USD");
        customerOrder.setTotalAmount(100.0);
        customerOrder.setType("enterprise");

        when(customerOrderRepository.save(any())).thenReturn(customerOrder);

        // Perform the POST request to create a customer order
        mockMvc.perform(post("/api/co/create")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"vendor\":\"VendorName\",\"currency\":\"USD\",\"totalAmount\":100.0,\"type\":\"enterprise\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer order created successfully!"));

        // Verify that the customer order was saved
        verify(customerOrderRepository, times(1)).save(any());
    }

    @Test
    public void testGetCustomerOrderByOrderNumber() throws Exception {
        // Create a sample customer order for testing
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setOrderNumber("ABC123");
        customerOrder.setVendor("VendorName");
        customerOrder.setCurrency("USD");
        customerOrder.setTotalAmount(100.0);
        customerOrder.setType("enterprise");

        when(customerOrderRepository.findByOrderNumber("ABC123")).thenReturn(Optional.of(customerOrder));

        // Perform the GET request to retrieve a customer order by order number
        mockMvc.perform(get("/api/co/get/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value("ABC123"))
                .andExpect(jsonPath("$.vendor").value("VendorName"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.totalAmount").value(100.0))
                .andExpect(jsonPath("$.type").value("enterprise"));

        // Verify that the repository method was called
        verify(customerOrderRepository, times(1)).findByOrderNumber("ABC123");
    }

    @Test
    public void testDeleteCustomerOrder() throws Exception {
        when(customerOrderRepository.findById(1L)).thenReturn(Optional.of(new CustomerOrder()));

        // Perform the DELETE request to delete a customer order by ID
        mockMvc.perform(delete("/api/co/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer order deleted successfully!"));

        // Verify that the repository method was called
        verify(customerOrderRepository, times(1)).delete(any());
    }

    @Test
    public void testDeleteCustomerOrderByOrderNumber() throws Exception {
        when(customerOrderRepository.findByOrderNumber("ABC123")).thenReturn(Optional.of(new CustomerOrder()));

        // Perform the DELETE request to delete a customer order by order number
        mockMvc.perform(delete("/api/co/deleteon/ABC123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer order deleted successfully!"));

        // Verify that the repository method was called
        verify(customerOrderRepository, times(1)).delete(any());
    }

    @Test
    public void testUpdateCustomerOrderByOrderNumber() throws Exception {
        CustomerOrder existingCustomerOrder = new CustomerOrder();
        existingCustomerOrder.setOrderNumber("ABC123");
        when(customerOrderRepository.findByOrderNumber("ABC123")).thenReturn(Optional.of(existingCustomerOrder));

        CustomerOrder updatedCustomerOrder = new CustomerOrder();
        updatedCustomerOrder.setVendor("UpdatedVendorName");
        updatedCustomerOrder.setCurrency("EUR");
        updatedCustomerOrder.setTotalAmount(200.0);
        updatedCustomerOrder.setType("talent");

        // Perform the PUT request to update a customer order by order number
        mockMvc.perform(put("/api/co/update/ABC123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"vendor\":\"UpdatedVendorName\",\"currency\":\"EUR\",\"totalAmount\":200.0,\"type\":\"talent\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer order updated successfully!"));

        // Verify that the repository method was called and the fields were updated
        verify(customerOrderRepository, times(1)).findByOrderNumber("ABC123");
        verify(customerOrderRepository, times(1)).save(any());
        assertEquals(existingCustomerOrder.getVendor(), updatedCustomerOrder.getVendor());
        assertEquals(existingCustomerOrder.getCurrency(), updatedCustomerOrder.getCurrency());
        assertEquals(existingCustomerOrder.getTotalAmount(), updatedCustomerOrder.getTotalAmount());
        assertEquals(existingCustomerOrder.getType(), updatedCustomerOrder.getType());
    }

    @Test
    public void testGetAllCustomerOrders() throws Exception {
        // Create a list of sample customer orders
        List<CustomerOrder> customerOrders = new ArrayList<>();
        CustomerOrder customerOrder1 = new CustomerOrder();
        customerOrder1.setId(4L);
        customerOrder1.setOrderNumber("4b5ca4");
        customerOrder1.setVendor("ABC Corporation");
        customerOrder1.setCurrency("SGD");
        customerOrder1.setTotalAmount(1000.0);
        customerOrder1.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-09-21"));
        customerOrder1.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-09-30"));
        customerOrder1.setType("enterprise");
        customerOrder1.setDateCreated(new SimpleDateFormat("yyyy-MM-dd").parse("2023-09-11")); // Set the dateCreated field

        customerOrders.add(customerOrder1);

        // Create a Page object with the sample customer orders
        Page<CustomerOrder> customerOrderPage = new PageImpl<>(customerOrders);

        when(customerOrderRepository.findAll(any(Pageable.class))).thenReturn(customerOrderPage);

        // Perform the GET request to retrieve all customer orders with pagination
        MvcResult result = mockMvc.perform(get("/api/co/all?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(4))
                .andExpect(jsonPath("$.content[0].orderNumber").value("4b5ca4"))
                .andExpect(jsonPath("$.content[0].vendor").value("ABC Corporation"))
                .andExpect(jsonPath("$.content[0].dateCreated").value("2023-09-11")) // Update the expected dateCreated value
                .andExpect(jsonPath("$.content[0].currency").value("SGD"))
                .andExpect(jsonPath("$.content[0].totalAmount").value(1000.0))
                .andExpect(jsonPath("$.content[0].startDate").value("2023-09-21"))
                .andExpect(jsonPath("$.content[0].endDate").value("2023-09-30"))
                .andExpect(jsonPath("$.content[0].type").value("enterprise"))
                .andExpect(jsonPath("$.content[0].status").value(false))
                .andReturn(); // Capture the result for debugging

        // Verify that the repository method was called
        verify(customerOrderRepository, times(1)).findAll(any(Pageable.class));

        // Debugging: Print the actual response content
        System.out.println("Actual Response Content:");
        System.out.println(result.getResponse().getContentAsString());

        // Debugging: Print the expected response content
        String expectedContent = "{\"content\":[{\"id\":4,\"orderNumber\":\"4b5ca4\",\"vendor\":\"ABC Corporation\",\"dateCreated\":\"2023-09-11\",\"currency\":\"SGD\",\"totalAmount\":1000.0,\"startDate\":\"2023-09-21\",\"endDate\":\"2023-09-30\",\"type\":\"enterprise\",\"status\":false}],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"offset\":0,\"pageSize\":10,\"pageNumber\":0,\"unpaged\":false,\"paged\":true},\"last\":true,\"totalPages\":1,\"totalElements\":1,\"size\":10,\"number\":0,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"numberOfElements\":1,\"first\":true,\"empty\":false}";
        System.out.println("Expected Response Content:");
        System.out.println(expectedContent);
    }



    @Test
    public void testUpdateCustomerOrderStatusByOrderNumber() throws Exception {
        CustomerOrder existingCustomerOrder = new CustomerOrder();
        existingCustomerOrder.setOrderNumber("ABC123");
        when(customerOrderRepository.findByOrderNumber("ABC123")).thenReturn(Optional.of(existingCustomerOrder));

        // Perform the PUT request to update the status of a customer order by order number
        mockMvc.perform(put("/api/co/updateStatus/ABC123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer order status updated to true!"));

        // Verify that the repository method was called and the status was updated
        verify(customerOrderRepository, times(1)).findByOrderNumber("ABC123");
        assertTrue(existingCustomerOrder.isStatus());
    }

    @Test
    public void testUnupdateCustomerOrderStatusByOrderNumber() throws Exception {
        CustomerOrder existingCustomerOrder = new CustomerOrder();
        existingCustomerOrder.setOrderNumber("ABC123");
        existingCustomerOrder.setStatus(true);
        when(customerOrderRepository.findByOrderNumber("ABC123")).thenReturn(Optional.of(existingCustomerOrder));

        // Perform the PUT request to update the status of a customer order to false by order number
        mockMvc.perform(put("/api/co/unupdateStatus/ABC123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer order status updated to false!"));

        // Verify that the repository method was called and the status was updated
        verify(customerOrderRepository, times(1)).findByOrderNumber("ABC123");
        assertFalse(existingCustomerOrder.isStatus());
    }

    @Test
    public void testGetAllEnterpriseOrders() throws Exception {
        List<CustomerOrder> enterpriseOrders = new ArrayList<>();
        enterpriseOrders.add(new CustomerOrder());
        enterpriseOrders.add(new CustomerOrder());

        Page<CustomerOrder> enterpriseOrdersPage = new PageImpl<>(enterpriseOrders);

        when(customerOrderRepository.findByType("enterprise", PageRequest.of(0, 10)))
                .thenReturn(enterpriseOrdersPage);

        // Perform the GET request to retrieve all enterprise customer orders with pagination
        mockMvc.perform(get("/api/co/getAllEnterpriseOrders?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));

        // Verify that the repository method was called
        verify(customerOrderRepository, times(1)).findByType("enterprise", PageRequest.of(0, 10));
    }

    @Test
    public void testGetAllTalentOrders() throws Exception {
        List<CustomerOrder> talentOrders = new ArrayList<>();
        talentOrders.add(new CustomerOrder());
        talentOrders.add(new CustomerOrder());

        Page<CustomerOrder> talentOrdersPage = new PageImpl<>(talentOrders);

        when(customerOrderRepository.findByType("talent", PageRequest.of(0, 10)))
                .thenReturn(talentOrdersPage);

        // Perform the GET request to retrieve all talent customer orders with pagination
        mockMvc.perform(get("/api/co/getAllTalentOrders?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));

        // Verify that the repository method was called
        verify(customerOrderRepository, times(1)).findByType("talent", PageRequest.of(0, 10));
    }
}
