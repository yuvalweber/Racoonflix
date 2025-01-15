import './App.css';
import GenericHomePage from './pages/genericHomePage';
import SignUpPage from './pages/registerPage';
import LoginPage from './pages/loginPage';
import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';


const App = () => {
  return (
	<BrowserRouter>
		<Routes>
			<Route path="/" element={<GenericHomePage />} />
			<Route path="/signup" element={<SignUpPage />} />
			<Route path="/login" element={<LoginPage />} />
		</Routes>
	</BrowserRouter>
  );
};

export default App;
