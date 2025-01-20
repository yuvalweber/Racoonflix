import './App.css';
import GenericHomePage from './pages/genericHomePage';
import SignUpPage from './pages/registerPage';
import LoginPage from './pages/loginPage';
import MovieInfoPage from './pages/movieInfoPage';
import ConnectedHomePage from './pages/connectedHomePage';
import WatchPage from './pages/watchPage';
import ManagementPage from './pages/managmentPage';
import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './Authentication/AuthContext';
import { ProtectedRoute }  from './Authentication/ProtectedRoute';



const App = () => {
  return (
	<AuthProvider>
	<BrowserRouter>
		<Routes>
			<Route path="/" element={<GenericHomePage />} />
			<Route path="/signup" element={<SignUpPage />} />
			<Route path="/login" element={<LoginPage />} />
			<Route path="/movieInfo/:id" element={<ProtectedRoute Component={<MovieInfoPage />} />} />
			<Route path="/play" element={<ProtectedRoute Component={<WatchPage />} />} />
			<Route path="/connected" element={<ProtectedRoute Component={<ConnectedHomePage />} />} />
			<Route path="/movieInfo/:id" element={<MovieInfoPage />} />
			<Route path="/watch" element={<WatchPage />} />
			<Route path="/connected" element={<ConnectedHomePage/>} />
			<Route path="/managment" element={<ManagementPage />} />
		</Routes>
	</BrowserRouter>
	</AuthProvider>
  );
};

export default App;
