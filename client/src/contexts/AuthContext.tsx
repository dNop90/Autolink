import React, { createContext, useState, useContext, ReactNode, useEffect } from 'react';
import { User } from '../data/User';
import { useCookie } from './CookieContext';
import { api } from '../services/api';
import { socket } from '../services/websocket';

interface AuthContextType {
  user: User | null;
  login: (userid: number, username: string, role: number, imageurl: string) => void;
  logout: () => void;
}

interface AuthProviderProps {
  children: ReactNode; // ReactNode type ensures 'children' can be any valid React element(s)
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const cookie = useCookie();
  
  //On page loaded
  //We will verified the user token based on their cookie
  useEffect(() => {
    const runInitToken = async () => {
      await InitToken();
      socket.init();
    }

    runInitToken();
  }, []);

  /**
   * Init the token
   */
  async function InitToken()
  {
    if(cookie.cookieData.token.length > 0)
    {
      try
      {
        let res = await api.user.tokenValidation(cookie.cookieData.token);

        setUser({userid: res.accountId, username: res.username, role: res.role, imageurl: res.imageurl});
      }
      catch(err)
      {
        cookie.removeToken();
      }
    }
  }

  function login(userid: number, username: string, role: number, imageurl: string)
  {
    setUser({userid, username, role, imageurl});
  }

  function logout()
  {
    setUser(null);
    cookie.removeToken();
  }

  return (
    <>
      <AuthContext.Provider value={{ user, login, logout}}>
        {children}
      </AuthContext.Provider>
    </>
    
  );
};
