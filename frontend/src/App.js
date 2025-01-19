import './App.css';
import GenericHomePage from './pages/genericHomePage';
import SignUpPage from './pages/registerPage';
import LoginPage from './pages/loginPage';
import MovieInfoPage from './pages/movieInfoPage';
import ConnectedHomePage from './pages/connectedHomePage';
import WatchPage from './pages/watchPage';
import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';


const App = () => {
  return (
	<BrowserRouter>
		<Routes>
			<Route path="/" element={<GenericHomePage />} />
			<Route path="/signup" element={<SignUpPage />} />
			<Route path="/login" element={<LoginPage />} />
			<Route path="/movieInfo/:id" element={<MovieInfoPage />} />
			<Route path="/watch" element={<WatchPage />} />
			<Route path="/connected" element={<ConnectedHomePage/>
} />
		</Routes>
	</BrowserRouter>
  );
};

export default App;
