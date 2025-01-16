import { Link } from "react-router-dom";
import './errObj.css';

const ErrObj = ({ error }) => {
    const { status, response } = error || {status: "400" , response: { data: { errors: 'Unknown error' } } };
    return (
    <div className="error-desc-card bg-dark bg-opacity-75 p-2 rounded d-flex align-items-center">
        <div className="error-poster-container">
            <img src="/images/error.gif" alt="Error Poster" />
        </div>

        <div className="ml-4 error-text" style={{ color: 'white' , paddingLeft: '10%'}}>
          <h2 className="error-title">{status}</h2>
          <p className="error-info">{response.data.errors}</p>
          <Link to="/" className="btn btn-secondary">Go Back</Link>
        </div>
      </div>
    );
};

export default ErrObj;