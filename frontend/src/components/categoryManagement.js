import React, { useState } from "react";
import axios from "axios";

const CategoryManagement = ({ action }) => {
  const [categoryData, setCategoryData] = useState({
    id: "",
    name: "",
    promoted: false,
    movies: [],
  });

  const initialCategoryData = {
    id: "",
    name: "",
    promoted: false,
    movies: [],
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    
    setCategoryData({
      ...categoryData,
      [name]: name === "promoted" ? value : value,
    });
  };

  const handleCheckboxChange = (e) => {
    const { name, checked } = e.target;
    setCategoryData({ ...categoryData, [name]: checked });
    };
  

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (action === "Create") {
      try {
        if (!categoryData.name) {
          alert("Category name is required.");
          return;
        }

        delete categoryData.id;

        const response = await axios.post("/api/categories", categoryData);
        if (response.status === 201) {
          alert("Category created successfully!");
          setCategoryData({ ...initialCategoryData });
        } else {
          alert("Failed to create category.");
        }
      } catch (error) {
        console.error("Error creating category:", error.response || error.message);
        alert(error.response?.data?.errors || "An error occurred while creating the category.");
      }
    } else if (action === "Delete") {
      try {
        if (!categoryData.id) {
          alert("Category ID is required for deletion.");
          return;
        }

        const response = await axios.delete(`/api/categories/${categoryData.id}`);
        if (response.status === 204) {
          alert("Category deleted successfully!");
          setCategoryData({ ...initialCategoryData });
        } else {
          alert("Failed to delete category.");
        }
      } catch (error) {
        console.error("Error deleting category:", error.response || error.message);
        alert(error.response?.data?.errors || "An error occurred while deleting the category.");
      }
    } else if (action === "Update") {
      try {
        if (!categoryData.id || !categoryData.name) {
          alert("Both Category ID and new name are required for updating.");
          return;
        }

        const response = await axios.put(`/api/categories/${categoryData.id}`, {
          name: categoryData.name,
          promoted: categoryData.promoted,
          movies: categoryData.movies,
        });

        if (response.status === 200) {
          alert("Category updated successfully!");
          setCategoryData({ ...initialCategoryData });
        } else {
          alert("Failed to update category.");
        }
      } catch (error) {
        console.error("Error updating category:", error.response || error.message);
        alert(error.response?.data?.errors || "An error occurred while updating the category.");
      }
    }
  };

  const renderForm = () => {
    switch (action) {
      case "Create":
        return (
          <form onSubmit={handleSubmit} style={{ width: "50%", margin: "auto" }}>
            <h3>Create a New Category</h3>
            <input
              type="text"
              placeholder="Category Name"
              className="form-control cardField"
              name="name"
              value={categoryData.name}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Movies"
              className="form-control cardField"
              name="movies"
              value={categoryData.movies}
              onChange={(e) =>
                setCategoryData({ ...categoryData, movies: e.target.value.split(",") })
              }
            />
            <div class="form-check form-switch">
                <input
                id="flexSwitchCheckDefault"
                type="checkbox"
                //   className="form-control cardField"
                name="promoted"
                className="form-check-input"
                checked={categoryData.promoted}
                onChange={handleCheckboxChange}
                />
                <label class="form-check-label" for="flexSwitchCheckDefault">promoted</label>
            </div>
            <button type="submit" className="btn btn-primary">
              Create Category
            </button>
          </form>
        );
      case "Delete":
        return (
          <form onSubmit={handleSubmit} style={{ width: "50%", margin: "auto" }}>
            <h3>Delete a Category</h3>
            <input
              type="text"
              placeholder="Category ID"
              className="form-control cardField"
              name="id"
              value={categoryData.id}
              onChange={handleInputChange}
            />
            <button type="submit" className="btn btn-danger">
              Delete Category
            </button>
          </form>
        );
      case "Update":
        return (
          <form onSubmit={handleSubmit} style={{ width: "50%", margin: "auto" }}>
            <h3>Update a Category</h3>
            <input
              type="text"
              placeholder="Category ID"
              className="form-control cardField"
              name="id"
              value={categoryData.id}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="New Category Name"
              className="form-control cardField"
              name="name"
              value={categoryData.name}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Promoted (true/false)"
              className="form-control cardField"
              name="promoted"
              value={categoryData.promoted}
              onChange={handleInputChange}
            />
            <input
              type="text"
              placeholder="Movies"
              className="form-control cardField"
              name="movies"
              value={categoryData.movies}
              onChange={(e) =>
                setCategoryData({ ...categoryData, movies: e.target.value.split(",") })
              }
            />
            <button type="submit" className="btn btn-success">
              Update Category
            </button>
          </form>
        );
      default:
        return null;
    }
  };

  return <div>{renderForm()}</div>;
};

export default CategoryManagement;
