import React from 'react';
import { Link } from 'react-router-dom';
import './homePageBackground.css'

// Icon component
const Icon = () => { return(
    <div className="top-right-icon">
        <Link to="/">
            <img src="/images/newLogo.webp" alt="Icon" className="icon-image-racoon" />
        </Link>
    </div>
);};

export default Icon;