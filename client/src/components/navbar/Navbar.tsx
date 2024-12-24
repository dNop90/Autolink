import React from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css'

const Navbar: React.FC = () => {
  return (
    <nav>
      <div className='left'>
        <Link to="/" >Home</Link>
        <Link to="/dealer-dashboard">Dealer Dashboard</Link>
        <Link to="/user-profile">Profile</Link>
      </div>
      <div className='right'>
        <Link to="/user-profile">Profile</Link>
      </div>
    </nav>
  );
};

export default Navbar;
