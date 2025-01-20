import React from "react";

const CategoryManagement = ({ action }) => {
  const renderForm = () => {
    switch (action) {
      case "Create":
        return (
          <form style={{ width: "50%", margin: "auto" }}>
            <h3>Create a New Category</h3>
            <input type="text" placeholder="Category Name" className="form-control cardField" />
            <input type="text" placeholder="Promoted?" className="form-control cardField" />
            <input type="text" placeholder="Movies" className="form-control cardField" />
            <button type="submit" className="btn btn-primary">Create Category</button>
          </form>
        );
      case "Delete":
        return (
          <form style={{ width: "50%", margin: "auto" }}>
            <h3>Delete a Category</h3>
            <input type="text" placeholder="Category ID" className="form-control cardField" />
            <button type="submit" className="btn btn-danger">Delete Category</button>
          </form>
        );
      case "Update":
        return (
          <form style={{ width: "50%", margin: "auto" }}>
            <h3>Update a Category</h3>
            <input type="text" placeholder="Category ID" className="form-control cardField" />
            <input type="text" placeholder="New Category Name" className="form-control cardField" />
            <input type="text" placeholder="Promoted?" className="form-control cardField" />
            <input type="text" placeholder="Movies" className="form-control cardField" />
            <button type="submit" className="btn btn-success">Update Category</button>
          </form>
        );
      default:
        return null;
    }
  };

  return <div>{renderForm()}</div>;
};

export default CategoryManagement;
