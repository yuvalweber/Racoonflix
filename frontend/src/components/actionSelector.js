import React from "react";

const ActionSelector = ({ selectedAction, setSelectedAction }) => {
  return (
    <div className="d-flex justify-content-center mb-4">
      <button
        className={`stylish-button me-2 ${selectedAction === "Create" ? "active" : ""}`}
        onClick={() => setSelectedAction("Create")}
      >
        Create
      </button>
      <button
        className={`stylish-button me-2 ${selectedAction === "Delete" ? "active" : ""}`}
        onClick={() => setSelectedAction("Delete")}
      >
        Delete
      </button>
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
