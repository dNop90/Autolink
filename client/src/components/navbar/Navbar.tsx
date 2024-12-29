import React from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css'
import { useAuth } from '../../contexts/AuthContext';
import * as Icon from 'react-bootstrap-icons';
import { useCookie } from '../../contexts/CookieContext';

const Navbar: React.FC = () => {
  const themeContext = useCookie();
  const theme = themeContext.cookieData.theme;

  const authContext = useAuth();
  const user = authContext.user;

  return (
    <nav>
      <div className='left'>
        <Link to="/" >Home</Link>
        <Link to="/explore">Explore</Link>

        {
          user &&
          <Link to="/dashboard">Dashboard</Link>
        }
      </div>

      <div className='right'>
        {
          theme > 0 ?
          <Icon.ToggleOn className='theme-toggle' onClick={() => themeContext.setTheme(0)}/>
          :
          <Icon.ToggleOff className='theme-toggle'onClick={() => themeContext.setTheme(1)}/>
        }

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
