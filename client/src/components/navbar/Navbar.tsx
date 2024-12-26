import React from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css'
import { useAuth } from '../../contexts/AuthContext';

const Navbar: React.FC = () => {
  const authContext = useAuth();
  const user = authContext.user;

  return (
    <nav>
      <div className='left'>
        <Link to="/" >Home</Link>
        <Link to="/explore">Explore</Link>
        <Link to="/dashboard">Dashboard</Link>
      </div>
      <div className='right'>
        {
          user ?
          <>
            <ul>
              <span className='username'>{user.username}</span>
              <li><Link to="/user/profile">Profile</Link></li>
              <li><Link to="/logout">Logout</Link></li>
            </ul>
          </>
          :
          <>
            <Link to="/login">Login</Link>
            <Link to="/register">Register</Link>
          </>
        }
        
      </div>
    </nav>
  );
};

export default Navbar;
