import React, { useState } from "react";
import Navbar from "../components/navbar";
import ActionSelector from "../components/actionSelector";
import MovieManagement from "../components/movieManagement";
import CategoryManagement from "../components/categoryManagement";
import SectionSelector from "../components/sectionSelector";
import "./managmentPage.css"; // Import custom styles
import '../components/button.css'; // Import custom styles

const ManagementPage = () => {
  const [selectedSection, setSelectedSection] = useState(null);
  const [selectedAction, setSelectedAction] = useState(null);

  const handleSectionSelect = (section) => {
    setSelectedSection(section);
    setSelectedAction(null);
  };

  return (
    <div>
      {/* Navbar Component */}
      <Navbar />

      {/* Main Content */}
      <div id="mainContainer" className="bg-dark text-white vh-100">
        {/* Transparent Box */}
        <div className="transparent-box"></div>
        <div className="container pt-5">
        <h1
          className="page-title"
        >
          Management Page
        </h1>
          {/* Section Selection */}
          <SectionSelector
            selectedSection={selectedSection}
            handleSectionSelect={handleSectionSelect}
          />

          {/* Action Selection */}
          {selectedSection && (
            <ActionSelector
              selectedAction={selectedAction}
              setSelectedAction={setSelectedAction}
            />
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
