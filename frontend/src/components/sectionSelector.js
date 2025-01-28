import React from "react";

// SectionSelector component that takes in selectedSection and handleSectionSelect as props
const SectionSelector = ({ selectedSection, handleSectionSelect }) => {
  return (
    // Container div with flexbox and margin-bottom styling
    <div className="d-flex justify-content-center mb-3">
      {/* Button for selecting "Movies" section */}
      <button
        // Apply "active" class if "movies" is the selected section
        className={`stylish-button me-2 ${selectedSection === "movies" ? "active" : ""}`}
        // Call handleSectionSelect with "movies" when button is clicked
        onClick={() => handleSectionSelect("movies")}
      >
        Movies
      </button>
      {/* Button for selecting "Categories" section */}
      <button
        // Apply "active" class if "categories" is the selected section
        className={`stylish-button ${selectedSection === "categories" ? "active" : ""}`}
        // Call handleSectionSelect with "categories" when button is clicked
        onClick={() => handleSectionSelect("categories")}
      >
        Categories
      </button>
    </div>
  );
};

export default SectionSelector;
