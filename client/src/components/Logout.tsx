import React from 'react'
import { useAuth } from '../contexts/AuthContext';

function Logout() {
    const context = useAuth();
    context.logout();
  return (
    <div>Logout</div>
  )
}

export default Logout