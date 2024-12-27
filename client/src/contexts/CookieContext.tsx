import React, { createContext, useState, useContext, ReactNode, useEffect } from 'react';
import Cookies from 'js-cookie'
import { CookieData } from '../data/CookieData';

interface CookieContextType {
    cookieData: CookieData;
    setTheme: (theme_type: number) => void;
    setToken: (token: string) => void;
    getToken: () => string;
}

interface CookieProviderProps {
  children: ReactNode;
}

const CookieContext = createContext<CookieContextType | undefined>(undefined);

export const useCookie = () => {
  const context = useContext(CookieContext);
  if (!context) {
    throw new Error('useCookie must be used within an CookieProvider');
  }
  return context;
};

export const CookieProvider: React.FC<CookieProviderProps> = ({ children }) => {
  const [cookie, setCookieState] = useState<CookieData>(
    {
      theme: 0,
      token: ''
    }
  );

  useEffect(() => {
    initTheme();
    initToken();
  }, []);


  /**
   * Init the theme when the site loaded
   */
  function initTheme()
  {
    let theme_cookie = Cookies.get('theme');

    //Check if cookie exist
    if(theme_cookie)
    {
        setTheme(parseInt(theme_cookie));
    }
    else
    {
        Cookies.set('theme', '0', { sameSite: 'strict' });
    }
  }

  /**
   * Init the session token when the site loaded
   */
  function initToken()
  {
    let session_cookie = Cookies.get('session');
    
    //Check if cookie exist
    if(session_cookie)
    {
        setToken(session_cookie);
    }
    else
    {
        Cookies.set('session', '', { sameSite: 'strict' });
    }
  }
  

  /**
   * Set theme
   * @param theme_type Theme type
   */
  function setTheme(theme_type: number)
  {
    setCookieState({...cookie, theme: theme_type});
    Cookies.set('theme', String(theme_type), { sameSite: 'strict' });
  }

  /**
   * Set the token for user session
   * @param token The user token for session
   */
  function setToken(token: string)
  {
    setCookieState({...cookie, token});
    Cookies.set('session', String(token), { sameSite: 'strict' });
  }

  /**
   * Get user token
   * @returns The token from the cookie
   */
  function getToken()
  {
    return cookie.token;
  }


  return (
    <>
        {
            cookie.theme > 0 ?
            <link rel="stylesheet" href="css/Dark-Theme.css" />
            :
            <link rel="stylesheet" href="css/Light-Theme.css" />
        }

        <CookieContext.Provider value={{cookieData: cookie, setTheme, setToken, getToken}}>
            {children}
        </CookieContext.Provider>
    </>
  );
};
