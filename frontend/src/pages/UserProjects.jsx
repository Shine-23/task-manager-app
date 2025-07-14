import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../api';
import '../assests/Projects.css'; // reuse styles if needed

function UserProjects() {
  const { userId } = useParams();
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProjects = async () => {
      try {
        const res = await api.get(`/projects/assigned-to/${userId}`);
        setProjects(res.data.content || res.data);
      } catch (err) {
        console.error('Error fetching user projects:', err);
        setError('Failed to load user projects.');
      } finally {
        setLoading(false);
      }
    };

    fetchProjects();
  }, [userId]);

  if (loading) return <div>Loading...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="projects-container">
      <h2>User’s Assigned Projects</h2>
      {projects.length === 0 ? (
        <p>No projects found for this user.</p>
      ) : (
        <div className="projects-grid">
          {projects.map((project) => (
            <div className="project-card" key={project.id}>
              <h3>{project.name}</h3>
              <p>{project.description}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default UserProjects;
