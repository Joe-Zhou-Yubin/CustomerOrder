import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function CreateCO() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    vendor: '',
    currency: 'USD',
    totalAmount: '',
    startDate: '',
    endDate: '',
    type: 'enterprise',
  });

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validate that the start date is earlier than the end date
    if (new Date(formData.startDate) >= new Date(formData.endDate)) {
      alert('Start date must be earlier than the end date.');
      return; // Exit the function if the validation fails
    }

    try {
      const response = await axios.post('http://localhost:8080/api/co/create', formData);

      if (response.status === 200) {
        // Handle success (e.g., show a success message, redirect, etc.)
        console.log('Customer order created successfully!');
        navigate('/home'); // Redirect to the home page after successful creation
      } else {
        // Handle other response statuses or errors
        console.error('Error creating customer order:', response.statusText);
      }
    } catch (error) {
      // Handle network errors or exceptions
      console.error('Network error:', error);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  return (
    <div className="container mt-5">
      <button className="btn btn-primary mb-3" onClick={() => navigate('/home')}>
        Back to Home
      </button>
      <h2>Create Customer Order</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="vendor" className="form-label">
            Vendor
          </label>
          <input
            type="text"
            className="form-control"
            id="vendor"
            name="vendor"
            value={formData.vendor}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="currency" className="form-label">
            Currency
          </label>
          <select
            className="form-control"
            id="currency"
            name="currency"
            value={formData.currency}
            onChange={handleChange}
          >
            <option value="USD">USD</option>
            <option value="SGD">SGD</option>
            <option value="EUR">EUR</option>
          </select>
        </div>
        <div className="mb-3">
          <label htmlFor="totalAmount" className="form-label">
            Total Amount
          </label>
          <input
            type="number"
            className="form-control"
            id="totalAmount"
            name="totalAmount"
            value={formData.totalAmount}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="startDate" className="form-label">
            Start Date
          </label>
          <input
            type="date"
            className="form-control"
            id="startDate"
            name="startDate"
            value={formData.startDate}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="endDate" className="form-label">
            End Date
          </label>
          <input
            type="date"
            className="form-control"
            id="endDate"
            name="endDate"
            value={formData.endDate}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="type" className="form-label">
            Type
          </label>
          <select
            className="form-control"
            id="type"
            name="type"
            value={formData.type}
            onChange={handleChange}
          >
            <option value="enterprise">Enterprise</option>
            <option value="talent">Talent</option>
          </select>
        </div>
        <button type="submit" className="btn btn-primary">
          Create Customer Order
        </button>
      </form>
    </div>
  );
}

export default CreateCO;
