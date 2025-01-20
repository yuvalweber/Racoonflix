import React from "react";

const SectionSelector = ({ selectedSection, handleSectionSelect }) => {
  return (
    <div className="d-flex justify-content-center mb-3">
      <button
        className={`stylish-button me-2 ${selectedSection === "movies" ? "active" : ""}`}
        onClick={() => handleSectionSelect("movies")}
      >
        Movies
      </button>
      <button
        className={`stylish-button ${selectedSection === "categories" ? "active" : ""}`}
        onClick={() => handleSectionSelect("categories")}
      >
        Categories
      </button>
    </div>
  );
};

export default SectionSelector;
