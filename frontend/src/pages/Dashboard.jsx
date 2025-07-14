import { useContext, useEffect, useState } from 'react';
import { AuthContext } from '../AuthContext';
import api from '../api';
import '../assests/Dashboard.css';
import { Link } from 'react-router-dom';

function Dashboard() {
  const { user } = useContext(AuthContext);
  const [projectCount, setProjectCount] = useState(0);
  const [userCount, setUserCount] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!user?.id) return;

    const fetchCounts = async () => {
      setLoading(true);
      setError(null);
      try {
        const isProjectManager = user?.roles?.includes('ROLE_PROJECT_MANAGER');
        const projectsUrl = isProjectManager
          ? `/projects/owner/${user.id}`
          : `/projects/assigned-to/${user.id}`;

        const [projectsRes, usersRes] = await Promise.all([
          api.get(projectsUrl, { params: { page: 0, size: 1 } }),
          api.get(`/users/role/ROLE_USER`, { params: { page: 0, size: 1 } })
        ]);

        setProjectCount(projectsRes.data.totalElements || 0);
        setUserCount(usersRes.data.totalElements || 0);
      } catch (err) {
        console.error('Error fetching dashboard data:', err);
        setError('Failed to load dashboard data.');
      } finally {
        setLoading(false);
      }
    };

    fetchCounts();
  }, [user]);

  const username = user?.username || user?.sub || 'User';

  if (loading) return <div className="dashboard-container">Loading dashboard...</div>;
  if (error) return <div className="dashboard-container error">{error}</div>;

  return (
    <div className="dashboard-container">
      <h1 className="dashboard-title">
        Welcome, {username.charAt(0).toUpperCase() + username.slice(1)} 👋
      </h1>
      <div className="dashboard-cards">
        <div className="dashboard-card">
          <h2>{projectCount}</h2>
          <p>Projects</p>
          <Link to="/projects" className="dashboard-link">
            View Projects
          </Link>
        </div>
        <div className="dashboard-card">
          <h2>{userCount}</h2>
          <p>Users</p>
          <Link to="/users" className="dashboard-link">
            View Users
          </Link>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
