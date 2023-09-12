import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams, Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

function CO() {
  const [coDetails, setCODetails] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);
  const [updatedVendor, setUpdatedVendor] = useState("");
  const [updatedType, setUpdatedType] = useState("");
  const [updatedTotalAmount, setUpdatedTotalAmount] = useState("");
  const [updatedStartDate, setUpdatedStartDate] = useState("");
  const [updatedEndDate, setUpdatedEndDate] = useState("");
  const [updatedCurrency, setUpdatedCurrency] = useState("");
  const [status, setStatus] = useState(false);
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
  const [milestones, setMilestones] = useState([]);
  const [editingMilestoneId, setEditingMilestoneId] = useState("");
  const [updatedMilestoneDescription, setUpdatedMilestoneDescription] =
    useState("");
  const [updatedMilestoneAmount, setUpdatedMilestoneAmount] = useState("");
  const [updatedMilestoneDeadline, setUpdatedMilestoneDeadline] = useState("");
  const [totalPaidMilestones, setTotalPaidMilestones] = useState(0); // State for total paid milestones
  const [totalUnpaidMilestones, setTotalUnpaidMilestones] = useState(0); // State for total unpaid milestones

  const { orderNumber } = useParams();
  const navigate = useNavigate();

  const unresolvedMilestoneAmount =
    parseFloat(coDetails.totalAmount) -
    totalPaidMilestones -
    totalUnpaidMilestones;

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/co/get/${orderNumber}`)
      .then((response) => {
        setCODetails(response.data);
        setStatus(response.data.status);
        setIsLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching CO details:", error);
      });

    axios
      .get(`http://localhost:8080/api/mile/getall/${orderNumber}`)
      .then((response) => {
        setMilestones(response.data);
      })
      .catch((error) => {
        console.error("Error fetching milestones:", error);
      });

    axios
      .get(`http://localhost:8080/api/mile/totalpaidAmount/${orderNumber}`)
      .then((response) => {
        setTotalPaidMilestones(response.data);
      })
      .catch((error) => {
        console.error("Error fetching milestones:", error);
      });

    axios
      .get(`http://localhost:8080/api/mile/totalunpaidAmount/${orderNumber}`)
      .then((response) => {
        setTotalUnpaidMilestones(response.data);
      })
      .catch((error) => {
        console.error("Error fetching milestones:", error);
      });
  }, [orderNumber]);

  const handleUpdateClick = () => {
    axios
      .put(`http://localhost:8080/api/co/update/${orderNumber}`, {
        vendor: updatedVendor,
        type: updatedType,
        totalAmount: updatedTotalAmount,
        startDate: updatedStartDate,
        endDate: updatedEndDate,
        currency: updatedCurrency,
      })
      .then((response) => {
        console.log(response.data);
        setCODetails({
          ...coDetails,
          vendor: updatedVendor,
          type: updatedType,
          totalAmount: updatedTotalAmount,
          startDate: updatedStartDate,
          endDate: updatedEndDate,
          currency: updatedCurrency,
        });
        setIsEditing(false);
      })
      .catch((error) => {
        console.error("Error updating CO:", error);
      });
  };

  const handleCancelClick = () => {
    setUpdatedVendor(coDetails.vendor);
    setUpdatedType(coDetails.type);
    setUpdatedTotalAmount(coDetails.totalAmount);
    setUpdatedStartDate(coDetails.startDate);
    setUpdatedEndDate(coDetails.endDate);
    setUpdatedCurrency(coDetails.currency);
    setIsEditing(false);
  };

  const handleStatusUpdate = () => {
    if (status) {
      axios
        .put(`http://localhost:8080/api/co/unupdateStatus/${orderNumber}`)
        .then((response) => {
          console.log(response.data);
          setStatus(false);
        })
        .catch((error) => {
          console.error("Error updating status:", error);
        });
    } else {
      axios
        .put(`http://localhost:8080/api/co/updateStatus/${orderNumber}`)
        .then((response) => {
          console.log(response.data);
          setStatus(true);
        })
        .catch((error) => {
          console.error("Error updating status:", error);
        });
    }
  };

  const handleDeleteClick = () => {
    setShowDeleteConfirmation(true);
  };

  const handleConfirmDelete = () => {
    axios
      .delete(`http://localhost:8080/api/co/deleteon/${orderNumber}`)
      .then((response) => {
        console.log(response.data);
        navigate("/home");
      })
      .catch((error) => {
        console.error("Error deleting CO:", error);
      });
  };

  const handleCancelDelete = () => {
    setShowDeleteConfirmation(false);
  };

  const handleEditMilestoneClick = (milestoneId) => {
    const milestoneToEdit = milestones.find(
      (milestone) => milestone.milestoneId === milestoneId
    );
    if (milestoneToEdit) {
      setUpdatedMilestoneDescription(milestoneToEdit.description);
      setUpdatedMilestoneAmount(milestoneToEdit.amount);
      setUpdatedMilestoneDeadline(
        new Date(milestoneToEdit.date).toLocaleDateString()
      );
      setEditingMilestoneId(milestoneId);
    }
  };

  const handleUpdateMilestoneClick = (milestoneId) => {
    const updatedMilestone = {
      description: updatedMilestoneDescription,
      amount: updatedMilestoneAmount,
      date: updatedMilestoneDeadline, // Include the updated deadline here
    };

    axios
      .put(
        `http://localhost:8080/api/mile/update/${milestoneId}`,
        updatedMilestone
      )
      .then((response) => {
        console.log(response.data);
        const updatedMilestones = milestones.map((milestone) => {
          if (milestone.milestoneId === milestoneId) {
            return {
              ...milestone,
              description: updatedMilestoneDescription,
              amount: updatedMilestoneAmount,
              date: updatedMilestoneDeadline, // Update the deadline in the updated milestone
            };
          }
          return milestone;
        });
        setMilestones(updatedMilestones);
        setEditingMilestoneId("");
      })
      .catch((error) => {
        console.error("Error updating milestone:", error);
      });
  };

  const handleDeleteMilestoneClick = (milestoneId) => {
    axios
      .delete(`http://localhost:8080/api/mile/delete/${milestoneId}`)
      .then((response) => {
        console.log(response.data);
        const updatedMilestones = milestones.filter(
          (milestone) => milestone.milestoneId !== milestoneId
        );
        setMilestones(updatedMilestones);
      })
      .catch((error) => {
        console.error("Error deleting milestone:", error);
      });
  };

  const handleUpdatePaidStatus = (milestoneId) => {
    axios
      .put(`http://localhost:8080/api/mile/updatepaid/${milestoneId}`)
      .then((response) => {
        console.log(response.data);
        const updatedMilestones = milestones.map((milestone) => {
          if (milestone.milestoneId === milestoneId) {
            return {
              ...milestone,
              paid: true, // Set paid to true
            };
          }
          return milestone;
        });
        setMilestones(updatedMilestones);
      })
      .catch((error) => {
        console.error("Error updating paid status:", error);
      });
  };

  const handleUpdateUnpaidStatus = (milestoneId) => {
    axios
      .put(`http://localhost:8080/api/mile/updateunpaid/${milestoneId}`)
      .then((response) => {
        console.log(response.data);
        const updatedMilestones = milestones.map((milestone) => {
          if (milestone.milestoneId === milestoneId) {
            return {
              ...milestone,
              paid: false, // Set paid to false
            };
          }
          return milestone;
        });
        setMilestones(updatedMilestones);
      })
      .catch((error) => {
        console.error("Error updating unpaid status:", error);
      });
  };

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="container mt-4">
      <div className="row">
        <div className="col-md-12">
          <Link to="/home" className="btn btn-secondary mb-3">
            Back to Home
          </Link>
        </div>
        <div className="col-md-6 ">
          <div className="card mb-4 w-100 mx-auto">
            <div className="card-header">CO Details</div>
            <div className="card-body">
              <p>
                <strong>Order Number:</strong> {coDetails.orderNumber}
              </p>
              <p>
                <strong>Vendor:</strong>{" "}
                {isEditing ? (
                  <input
                    type="text"
                    value={updatedVendor}
                    onChange={(e) => setUpdatedVendor(e.target.value)}
                  />
                ) : (
                  coDetails.vendor
                )}
              </p>
              <p>
                <strong>Type:</strong>{" "}
                {isEditing ? (
                  <select
                    value={updatedType}
                    onChange={(e) => setUpdatedType(e.target.value)}
                  >
                    <option value="enterprise">Enterprise</option>
                    <option value="talent">Talent</option>
                  </select>
                ) : (
                  coDetails.type
                )}
              </p>
              <p>
                <strong>Total Amount:</strong>{" "}
                {isEditing ? (
                  <input
                    type="number"
                    value={updatedTotalAmount}
                    onChange={(e) => setUpdatedTotalAmount(e.target.value)}
                  />
                ) : (
                  coDetails.totalAmount
                )}
              </p>
              <p>
                <strong>Currency:</strong>{" "}
                {isEditing ? (
                  <select
                    value={updatedCurrency}
                    onChange={(e) => setUpdatedCurrency(e.target.value)}
                  >
                    <option value="USD">USD</option>
                    <option value="SGD">SGD</option>
                    <option value="EUR">EUR</option>
                  </select>
                ) : (
                  coDetails.currency
                )}
              </p>
              <p>
                <strong>Start Date:</strong>{" "}
                {isEditing ? (
                  <input
                    type="date"
                    value={updatedStartDate}
                    onChange={(e) => setUpdatedStartDate(e.target.value)}
                  />
                ) : (
                  coDetails.startDate
                )}
              </p>
              <p>
                <strong>End Date:</strong>{" "}
                {isEditing ? (
                  <input
                    type="date"
                    value={updatedEndDate}
                    onChange={(e) => setUpdatedEndDate(e.target.value)}
                  />
                ) : (
                  coDetails.endDate
                )}
              </p>
              <p>
                <strong>Date Created:</strong> {coDetails.dateCreated}
              </p>
              <p>
                <strong>Status:</strong> {status ? "Complete" : "Incomplete"}
              </p>

              {isEditing ? (
                <div>
                  <button
                    className="btn btn-sm btn-success float-right"
                    onClick={handleUpdateClick}
                  >
                    Save
                  </button>
                  <button
                    className="btn btn-sm btn-danger float-right mr-2"
                    onClick={handleCancelClick}
                  >
                    Cancel
                  </button>
                </div>
              ) : (
                <button
                  className="btn btn-sm btn-primary float-right"
                  onClick={() => setIsEditing(true)}
                >
                  Edit
                </button>
              )}

              <button
                className="btn btn-sm btn-info float-left"
                onClick={handleStatusUpdate}
              >
                {status ? "Mark Incomplete" : "Mark Complete"}
              </button>

              <button
                className="btn btn-sm btn-danger float-left ml-2"
                onClick={handleDeleteClick}
              >
                Delete
              </button>

              {showDeleteConfirmation && (
                <div className="mt-3">
                  <p>Are you sure you want to delete this CO?</p>
                  <button
                    className="btn btn-danger btn-sm"
                    onClick={handleConfirmDelete}
                  >
                    Confirm Delete
                  </button>
                  <button
                    className="btn btn-secondary btn-sm ml-2"
                    onClick={handleCancelDelete}
                  >
                    Cancel
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
        <div className="col-md-6">
          <div className="card mb-4 w-100 mx-auto">
            <div className="card-header">Customer Order Tally</div>
            <div className="card-body">
              <p>
                <strong>Total Paid Milestones:</strong> {totalPaidMilestones}
              </p>
              <p>
                <strong>Total Unpaid Milestones:</strong>{" "}
                {totalUnpaidMilestones}
              </p>
              <p>
                <strong>Unresolved Milestone Amount:</strong>{" "}
                {unresolvedMilestoneAmount}
              </p>
            </div>
          </div>
        </div>
        <div className="col-md-12 w-100 mx-auto">
          <div className="card mb-4 w-100 mx-auto">
            <div className="card-header">Milestones</div>
            <div className="card-body">
              <Link
                to={`/createmile/${orderNumber}`} // Link to the new route with the orderNumber parameter
                className="btn btn-success mb-3"
              >
                Create Milestone
              </Link>
              <table className="table">
                <thead>
                  <tr>
                    <th>Milestone ID</th>
                    <th>Description</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Deadline</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {milestones.map((milestone) => (
                    <tr key={milestone.id}>
                      <td>{milestone.milestoneId}</td>
                      <td>
                        {editingMilestoneId === milestone.milestoneId ? (
                          <input
                            type="text"
                            value={updatedMilestoneDescription}
                            onChange={(e) =>
                              setUpdatedMilestoneDescription(e.target.value)
                            }
                          />
                        ) : (
                          milestone.description
                        )}
                      </td>
                      <td>
                        {editingMilestoneId === milestone.milestoneId ? (
                          <input
                            type="number"
                            value={updatedMilestoneAmount}
                            onChange={(e) =>
                              setUpdatedMilestoneAmount(e.target.value)
                            }
                          />
                        ) : (
                          milestone.amount
                        )}
                      </td>
                      <td>{milestone.paid ? "Paid" : "Unpaid"}</td>
                      <td>
                        {editingMilestoneId === milestone.milestoneId ? (
                          <input
                            type="date"
                            value={updatedMilestoneDeadline}
                            onChange={(e) =>
                              setUpdatedMilestoneDeadline(e.target.value)
                            }
                          />
                        ) : (
                          new Date(milestone.date).toLocaleDateString()
                        )}
                      </td>
                      <td>
                        {editingMilestoneId === milestone.milestoneId ? (
                          <div>
                            <button
                              className="btn btn-sm btn-success"
                              onClick={() =>
                                handleUpdateMilestoneClick(
                                  milestone.milestoneId
                                )
                              }
                            >
                              Save
                            </button>
                            <button
                              className="btn btn-sm btn-danger ml-2"
                              onClick={() => setEditingMilestoneId("")}
                            >
                              Cancel
                            </button>
                          </div>
                        ) : (
                          <div>
                            <button
                              className="btn btn-sm btn-primary"
                              onClick={() =>
                                handleEditMilestoneClick(milestone.milestoneId)
                              }
                            >
                              Edit
                            </button>
                            <button
                              className="btn btn-sm btn-danger ml-2"
                              onClick={() =>
                                handleDeleteMilestoneClick(
                                  milestone.milestoneId
                                )
                              }
                            >
                              Delete
                            </button>
                            <button
                              className={`btn btn-sm ${
                                milestone.paid ? "btn-warning" : "btn-success"
                              }`}
                              onClick={() => {
                                if (milestone.paid) {
                                  handleUpdateUnpaidStatus(
                                    milestone.milestoneId
                                  );
                                } else {
                                  handleUpdatePaidStatus(milestone.milestoneId);
                                }
                                window.location.reload(); // Reload the page
                              }}
                            >
                              {milestone.paid ? "Mark Unpaid" : "Mark Paid"}
                            </button>
                          </div>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default CO;
