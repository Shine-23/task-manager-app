import { useContext, useEffect, useState } from 'react';
import { AuthContext } from '../AuthContext';
import api from '../api';
import '../assests/Users.css';
import { useNavigate } from 'react-router-dom';

function Users() {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const isProjectManager = user?.roles?.includes('ROLE_PROJECT_MANAGER');

  const fetchUsers = async (pageNumber = 0) => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.get('/users/role/ROLE_USER', {
        params: { page: pageNumber, size: 5 }
      });
      setUsers(res.data.content || []);
      setTotalPages(res.data.totalPages || 1);
      setPage(res.data.number || 0);
    } catch (err) {
      console.error(err);
      setError('Failed to fetch users');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers(page);
  }, []);

  const handleNext = () => {
    if (page + 1 < totalPages) {
      fetchUsers(page + 1);
    }
  };

  const handlePrevious = () => {
    if (page > 0) {
      fetchUsers(page - 1);
    }
  };

  return (
    <div className="users-container">
      <h1>Users</h1>
      {loading ? (
        <p>Loading...</p>
      ) : error ? (
        <p className="error">{error}</p>
      ) : (
        <>
          <table className="users-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Email</th>
                {isProjectManager && <th>Actions</th>}
              </tr>
            </thead>
            <tbody>
              {users.map((u, index) => (
                <tr key={u.id}>
                  <td>{page * 5 + index + 1}</td>
                 <td>{u.username.charAt(0).toUpperCase() + u.username.slice(1)}</td>
                  <td>{u.email}</td>
                  {isProjectManager && (
                    <td>
                      <button onClick={() => navigate(`/users/${u.id}/projects`)}>
                        View Projects
                      </button>
                    </td>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
          <div className="pagination">
            <button onClick={handlePrevious} disabled={page === 0}>
              Previous
            </button>
            <span>
              Page {page + 1} of {totalPages}
            </span>
            <button onClick={handleNext} disabled={page + 1 >= totalPages}>
              Next
            </button>
          </div>
        </>
      )}
    </div>
  );
}

export default Users;
