import './App.css';
import GenericHomePage from './pages/genericHomePage';
import SignUpPage from './pages/registerPage';
import LoginPage from './pages/loginPage';
import MovieInfoPage from './pages/movieInfoPage';
import ConnectedHomePage from './pages/connectedHomePage';
import WatchPage from './pages/watchPage';
import ManagementPage from './pages/managmentPage';
import SearchPage from './pages/searchPage';
import AllMoviesPage from './pages/allMoviesPage';
import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './Authentication/AuthContext';
import { ProtectedRoute }  from './Authentication/ProtectedRoute';
import { ThemeProvider } from './components/themeContext';



const App = () => {
  return (
	<ThemeProvider>
	<AuthProvider>
	<BrowserRouter>
		<Routes>
			<Route path="/" element={<GenericHomePage />} />
			<Route path="/signup" element={<SignUpPage />} />
			<Route path="/login" element={<LoginPage />} />
			<Route path="/movieInfo/:id" element={<ProtectedRoute Component={<MovieInfoPage />} />} />
			<Route path="/play" element={<ProtectedRoute Component={<WatchPage />} />} />
			<Route path="/connected" element={<ProtectedRoute Component={<ConnectedHomePage />} />} />
			<Route path="/management" element={<ProtectedRoute Component={<ManagementPage />} />} />
			<Route path="/search" element={<ProtectedRoute Component={<SearchPage />} />} />
			<Route path="/allMovies" element={<ProtectedRoute Component={<AllMoviesPage />} />} />
		</Routes>
	</BrowserRouter>
	</AuthProvider>
	</ThemeProvider>
  );
};

export default App;
