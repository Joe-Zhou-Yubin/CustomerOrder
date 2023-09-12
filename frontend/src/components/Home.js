import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

function Home() {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
  
    const [filterCriteria, setFilterCriteria] = useState(''); // Selected filter criteria
    const [searchValue, setSearchValue] = useState(''); // Search value
    const [typeFilter, setTypeFilter] = useState(''); // Type filter
    const [startDateFilter, setStartDateFilter] = useState(''); // Start date filter
    const [endDateFilter, setEndDateFilter] = useState(''); // End date filter
  
    useEffect(() => {
        console.log('Fetching orders...');
      
        // Fetch orders from the backend with pagination
        axios
          .get(`http://localhost:8080/api/co/all?page=${currentPage}&size=10`)
          .then((response) => {
            console.log('Orders response:', response.data);
            const { content, totalPages } = response.data;
      
            // Apply client-side filtering based on filterCriteria and searchValue
            const filteredOrders = content.filter((order) => {
              const lowerCaseSearchValue = searchValue.toLowerCase(); // Convert search value to lowercase
              if (filterCriteria === 'orderNumber') {
                return order.orderNumber.toLowerCase().includes(lowerCaseSearchValue);
              } else if (filterCriteria === 'vendor') {
                return order.vendor.toLowerCase().includes(lowerCaseSearchValue);
              } else if (filterCriteria === 'dateCreated') {
                // Assuming dateCreated is in ISO date string format
                return order.dateCreated.toLowerCase().includes(lowerCaseSearchValue);
              } else if (filterCriteria === 'startDate') {
                // Assuming startDate is in ISO date string format
                return order.startDate.toLowerCase().includes(lowerCaseSearchValue);
              } else if (filterCriteria === 'endDate') {
                // Assuming endDate is in ISO date string format
                return order.endDate.toLowerCase().includes(lowerCaseSearchValue);
              } else if (filterCriteria === 'status') {
                return (
                  searchValue === 'complete' && order.status ||
                  searchValue === 'incomplete' && !order.status
                );
              }
              return true; // No filter or unsupported filter criteria
            });
      
            setOrders(filteredOrders);
            setTotalPages(totalPages);
          })
          .catch((error) => {
            console.error('Error fetching orders:', error);
          })
          .finally(() => {
            console.log('Fetch complete.');
            setLoading(false);
          });
      }, [currentPage, filterCriteria, searchValue, typeFilter]);
      
    const handlePageChange = (newPage) => {
      setCurrentPage(newPage);
    };
  
    if (loading) {
      console.log('Loading...');
      return <div>Loading...</div>;
    }
  
    console.log('Rendering orders:', orders);
  
    // Handle row click event
    const handleRowClick = (orderNumber) => {
      // Redirect to the order details page
      window.location.href = `/co/${orderNumber}`;
    };

  return (
    <div className="container">
      <div className="d-flex justify-content-between align-items-center">
        <h2 className="text-center mt-3">CO Table</h2>
        <Link to="/createco" className="btn btn-primary">
          Create CO
        </Link>
      </div>
      <div className="card">
        <div className="card-header">
          <div className="row">
            <div className="col-md-6">
              <h4>Filter Orders</h4>
            </div>
          </div>
        </div>
        <div className="card-body">
          <div className="mb-3">
            <label htmlFor="filterCriteria" className="form-label">
              Select Filter Criteria
            </label>
            <select
              className="form-control"
              id="filterCriteria"
              name="filterCriteria"
              value={filterCriteria}
              onChange={(e) => setFilterCriteria(e.target.value)}
            >
              <option value="">Select...</option>
              <option value="orderNumber">Order Number</option>
              <option value="vendor">Vendor</option>
              <option value="dateCreated">Date Created</option>
              <option value="startDate">Start Date</option>
              <option value="endDate">End Date</option>
              <option value="status">Status</option> {/* Add Status as a filter option */}
            </select>
          </div>
          {filterCriteria !== 'status' && (
            <div className="mb-3">
              <label htmlFor="searchValue" className="form-label">
                Search Value
              </label>
              <input
                type="text"
                className="form-control"
                id="searchValue"
                name="searchValue"
                value={searchValue}
                onChange={(e) => setSearchValue(e.target.value)}
              />
            </div>
          )}
          {filterCriteria === 'status' && (
            <div className="mb-3">
              <label htmlFor="statusFilter" className="form-label">
                Select Status
              </label>
              <select
                className="form-control"
                id="statusFilter"
                name="statusFilter"
                value={searchValue}
                onChange={(e) => setSearchValue(e.target.value)}
              >
                <option value="">Select...</option>
                <option value="complete">Complete</option>
                <option value="incomplete">Incomplete</option>
              </select>
            </div>
          )}
        </div>
        <div className="card-body">
          <div className="table-responsive">
            <table className="table mx-auto">
              <thead>
                <tr>
                  <th>Order Number</th>
                  <th>Vendor</th>
                  <th>Type</th>
                  <th>Total Amount</th>
                  <th>Start Date</th>
                  <th>End Date</th>
                  <th>Date Created</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {orders.map((order) => (
                  <tr
                    key={order.id}
                    onClick={() => handleRowClick(order.orderNumber)}
                    style={{ cursor: 'pointer' }}
                  >
                    <td>{order.orderNumber}</td>
                    <td>{order.vendor}</td>
                    <td>{order.type}</td>
                    <td>{order.totalAmount}</td>
                    <td>{order.startDate}</td>
                    <td>{order.endDate}</td>
                    <td>{order.dateCreated}</td>
                    <td style={{ color: order.status ? 'green' : 'red' }}>
                      {order.status ? 'Complete' : 'Incomplete'}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div className="pagination d-flex justify-content-between mt-3">
        <button
          className="btn btn-primary"
          onClick={() => handlePageChange(currentPage - 1)}
          disabled={currentPage === 0}
        >
          Previous
        </button>
        <span>Page {currentPage + 1} of {totalPages}</span>
        <button
          className="btn btn-primary"
          onClick={() => handlePageChange(currentPage + 1)}
          disabled={currentPage === totalPages - 1}
        >
          Next
        </button>
      </div>
    </div>
  );
}

export default Home;
