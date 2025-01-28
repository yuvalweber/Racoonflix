import React, { createContext, useState, useEffect } from 'react';
import { useContext } from 'react';

// Create the ThemeContext
export const ThemeContext = createContext();

// ThemeProvider component
export const ThemeProvider = ({ children }) => {
  // State to manage the theme mode (dark or light)
  const [isDarkMode, setIsDarkMode] = useState(() => {
    // Initialize from localStorage or default to dark mode
    const savedMode = localStorage.getItem('isDarkMode');
    return savedMode ? JSON.parse(savedMode) : true;
  });

  // Function to toggle the theme mode
  const toggleTheme = () => {
    setIsDarkMode((prevMode) => !prevMode);
  };
  
  // Function to reset the theme to dark mode
  const resetTheme = () => {
    setIsDarkMode(true); // Reset to dark mode
  };

  // Effect to save the theme mode to localStorage and update the body class
  useEffect(() => {
    localStorage.setItem('isDarkMode', JSON.stringify(isDarkMode));
    // Update the body class based on the current mode
    if (isDarkMode) {
      document.body.classList.add('dark-mode');
      document.body.classList.remove('light-mode');
    } else {
      document.body.classList.add('light-mode');
      document.body.classList.remove('dark-mode');
    }
  }, [isDarkMode]);

  return (
    // Provide the theme context to children components
    <ThemeContext.Provider value={{ isDarkMode, toggleTheme, resetTheme }}>
      {children}
    </ThemeContext.Provider>
  );
};

// Custom hook to use the ThemeContext
export const useTheme = () => useContext(ThemeContext);
