import React from 'react';
import { render } from 'react-dom';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';

import Login from './components/Login';
import Home from './components/Home';
import CO from './components/CO';
import CreateCO from './components/CreateCO';
import CreateUser from './components/CreateUser';
import Enterprise from './components/Enterprise';
import Talent from './components/Talent';
import User from './components/User';
import Navbar from './components/Navbar'; 
import Wildcard from './components/Wildcard';
import CreateMilestone from './components/CreateMilestone';

import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  // Define an array of paths where you want the Navbar to be rendered
  const navbarPaths = ['/home', '/co/', '/createco', '/enterprise', '/talent', '/user', '/createuser', '/createmile/'];
  const user = JSON.parse(localStorage.getItem('user'));
  // Check if the current path is in the array of navbarPaths
  const shouldRenderNavbar = navbarPaths.some((path) => window.location.pathname.startsWith(path));

  return (
    <Router>
      {shouldRenderNavbar && <Navbar />} {/* Conditional rendering of Navbar */}
      <Routes>
        <Route path="/*" element={<Wildcard />} />
        <Route path="/login" element={<Login />} />
        <Route path="/home" element={<Home />} />
        <Route path="/co/:orderNumber" element={<CO />} />
        <Route path="/createco" element={<CreateCO />} />
        <Route path="/createmile/:orderNumber" element={<CreateMilestone />} />
        <Route path="/enterprise" element={<Enterprise />} />
        <Route path="/talent" element={<Talent />} />
        {user && user.roles && user.roles.includes('ROLE_ADMIN') && (
          <>
            <Route path="/user" element={<User />} />
            <Route path="/createuser" element={<CreateUser />} />
          </>
        )}
      </Routes>
    </Router>
  );
}

render(<App />, document.getElementById('root'));
