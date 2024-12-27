import React, { createContext, useState, useContext, ReactNode, useEffect } from 'react';
import Cookies from 'js-cookie'

interface ThemeContextType {
    theme: number;
    setTheme: (theme_type: number) => void;
}

interface ThemeProviderProps {
  children: ReactNode;
}

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export const useTheme = () => {
  const context = useContext(ThemeContext);
  if (!context) {
    throw new Error('useTheme must be used within an ThemeProvider');
  }
  return context;
};

export const ThemeProvider: React.FC<ThemeProviderProps> = ({ children }) => {
  const [state, setThemeState] = useState(0);

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

  useEffect(() => {
    initTheme();
  }, []);
  

  function setTheme(theme_type: number)
  {
    setThemeState(theme_type);
    Cookies.set('theme', String(theme_type), { sameSite: 'strict' });
  }

  return (
    <>
        {
            state > 0 ?
            <link rel="stylesheet" href="css/Dark-Theme.css" />
            :
            <link rel="stylesheet" href="css/Light-Theme.css" />
        }

        <ThemeContext.Provider value={{theme: state, setTheme}}>
            {children}
        </ThemeContext.Provider>
    </>
  );
};
