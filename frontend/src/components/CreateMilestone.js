import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

function CreateMilestone() {
  const { orderNumber } = useParams();
  const navigate = useNavigate();

  const [description, setDescription] = useState('');
  const [date, setDate] = useState('');
  const [amount, setAmount] = useState('');

  const handleBackToCO = () => {
    navigate(`/co/${orderNumber}`);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const milestoneData = {
      description,
      date,
      amount: parseFloat(amount),
    };

    axios
      .post(`http://localhost:8080/api/mile/create/${orderNumber}`, milestoneData)
      .then((response) => {
        console.log('Milestone created successfully:', response.data);
        navigate(`/co/${orderNumber}`);
      })
      .catch((error) => {
        console.error('Error creating milestone:', error);
      });

    setDescription('');
    setDate('');
    setAmount('');
  };

  return (
    <div className="container mt-4">
      <div className="row">
        <div className="col-md-6 mx-auto">
          <button className="btn btn-secondary mb-3" onClick={handleBackToCO}>
            Back to Customer Order
          </button>
          <h2>Create Milestone</h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="description" className="form-label">
                Description
              </label>
              <input
                type="text"
                className="form-control"
                id="description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                required
              />
            </div>
            <div className="mb-3">
              <label htmlFor="date" className="form-label">
                Date
              </label>
              <input
                type="date"
                className="form-control"
                id="date"
                value={date}
                onChange={(e) => setDate(e.target.value)}
                required
              />
            </div>
            <div className="mb-3">
              <label htmlFor="amount" className="form-label">
                Amount
              </label>
              <input
                type="number"
                className="form-control"
                id="amount"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                step="0.01"
                required
              />
            </div>
            <button type="submit" className="btn btn-primary">
              Create Milestone
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default CreateMilestone;
