import React from 'react';
import { Link } from 'react-router-dom';

function Wildcard() {
  const clearLocalStorage = () => {
    localStorage.clear();
  };

  return (
    <div className="d-flex flex-column align-items-center justify-content-center vh-100">
      <div className="h2 fw-bold mb-4">Invalid Page</div>
      <Link to="/login" className="btn btn-primary" onClick={clearLocalStorage}>
        Back to Login
      </Link>
    </div>
  );
}

export default Wildcard;
