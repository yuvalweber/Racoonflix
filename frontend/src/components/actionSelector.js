import React from "react";

// ActionSelector component to select an action (Create, Delete, Update)
const ActionSelector = ({ selectedAction, setSelectedAction }) => {
  return (
    <div className="d-flex justify-content-center mb-4">
      {/* Button to select "Create" action */}
      <button
        className={`stylish-button me-2 ${selectedAction === "Create" ? "active" : ""}`}
        onClick={() => setSelectedAction("Create")}
      >
        Create
      </button>
      {/* Button to select "Delete" action */}
      <button
        className={`stylish-button me-2 ${selectedAction === "Delete" ? "active" : ""}`}
        onClick={() => setSelectedAction("Delete")}
      >
        Delete
      </button>
      {/* Button to select "Update" action */}
      <button
        className={`stylish-button ${selectedAction === "Update" ? "active" : ""}`}
        onClick={() => setSelectedAction("Update")}
      >
        Update
      </button>
    </div>
  );
};

export default ActionSelector;