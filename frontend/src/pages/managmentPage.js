import React, { useState } from "react";
import Navbar from "../components/navbar"; // Navbar component to display top navigation
import ActionSelector from "../components/actionSelector"; // ActionSelector component
import MovieManagement from "../components/movieManagement"; // Import MovieManagement component
import CategoryManagement from "../components/categoryManagement"; // Import CategoryManagement component
import SectionSelector from "../components/sectionSelector"; // Import SectionSelector component
import "../components/button.css"; // Button CSS

const ManagementPage = () => {
  const [selectedSection, setSelectedSection] = useState(null);
  const [selectedAction, setSelectedAction] = useState(null);

  const handleSectionSelect = (section) => {
    setSelectedSection(section);
    setSelectedAction(null); // Reset action when switching sections
  };

  return (
    <div>
        {/* Navbar component for navigation */}
        <Navbar />

        {/* Main Container */}
        <div id="mainContainer" className="bg-dark text-white vh-100">
        {/* Transparent Box */}
        <div className="transparent-box"></div>

        {/* Management Page Content */}
        <div className="container pt-5">
            <h1
            className="text-center mb-4"
            style={{ color: "green", textShadow: "1px 1px 2px red", paddingTop: 20 }}
            >
            Management Page
            </h1>

            {/* Section Selection using SectionSelector Component */}
            <SectionSelector selectedSection={selectedSection} handleSectionSelect={handleSectionSelect} />

            {/* Action Selection */}
            {selectedSection && (
            <ActionSelector selectedAction={selectedAction} setSelectedAction={setSelectedAction} />
            )}

            {/* Render MovieManagement for Movies Section */}
            {selectedSection === "movies" && selectedAction && (
            <MovieManagement action={selectedAction} />
            )}

            {/* Render CategoryManagement for Categories Section */}
            {selectedSection === "categories" && selectedAction && (
            <CategoryManagement action={selectedAction} />
            )}
        </div>
        </div>
    </div>
  );
};

export default ManagementPage;
