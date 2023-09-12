import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';

function Navbar() {
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem('user'));
  const location = useLocation(); // Get the current location

  const handleLogout = () => {
    // Clear local storage and navigate to the login page or any desired route
    localStorage.removeItem('user');
    navigate('/login');
    window.location.reload();
  };

  // Function to determine if a given route path is active
  const isRouteActive = (routePath) => {
    return location.pathname.startsWith(routePath);
  };

  // Define inline CSS for the active item (font weight: bold)
  const activeItemStyle = {
    fontWeight: 'bold',
  };

  // Define additional spacing between items
  const itemSpacing = {
    marginRight: '20px', // Adjust the value as needed
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <div className="container">
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav mx-auto">
            <li className="nav-item" style={{ ...itemSpacing, ...(isRouteActive('/home') ? activeItemStyle : {}) }}>
              <Link to="/home" className="nav-link">
                All CO
              </Link>
            </li>
            <li className="nav-item" style={{ ...itemSpacing, ...(isRouteActive('/enterprise') ? activeItemStyle : {}) }}>
              <Link to="/enterprise" className="nav-link">
                Enterprise CO
              </Link>
            </li>
            <li className="nav-item" style={{ ...itemSpacing, ...(isRouteActive('/talent') ? activeItemStyle : {}) }}>
              <Link to="/talent" className="nav-link">
                Talent CO
              </Link>
            </li>
            {user && user.roles && user.roles.includes('ROLE_ADMIN') && (
              <li className="nav-item" style={{ ...itemSpacing, ...(isRouteActive('/user') ? activeItemStyle : {}) }}>
                <Link to="/user" className="nav-link">
                  User Dashboard
                </Link>
              </li>
            )}
            <li className="nav-item">
              <button className="nav-link" onClick={handleLogout}>
                Logout
              </button>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
