import { useContext, useEffect, useState } from 'react'
import { AuthContext } from '../AuthContext'
import api from '../api'
import '../assests/Projects.css'
import ProjectModal from '../components/ProjectModal'
import ProjectTasksModal from '../components/ProjectTasksModal'


function Projects() {
  const { user } = useContext(AuthContext)
  const [projects, setProjects] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showModal, setShowModal] = useState(false)
  const [modalMode, setModalMode] = useState('create')
  const [editData, setEditData] = useState(null)
  const [showTaskModal, setShowTaskModal] = useState(false)
  const [selectedProjectId, setSelectedProjectId] = useState(null)

  const isProjectManager = user?.roles?.includes('ROLE_PROJECT_MANAGER')

  const fetchProjects = async () => {
    setLoading(true)
    setError(null)

    try {
      const url = isProjectManager
        ? `/projects/owner/${user.id}`
        : `/projects/assigned-to/${user.id}`
      const res = await api.get(url)
      setProjects(res.data.content || res.data)
    } catch (err) {
      console.error(err)
      setError('Failed to fetch projects.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchProjects()
  }, [user, isProjectManager])

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this project?')) return
    try {
      await api.delete(`/projects/${id}`)
      setProjects((prev) => prev.filter((proj) => proj.id !== id))
    } catch (err) {
      console.error('Error deleting project:', err)
      alert('Failed to delete project.')
    }
  }

  const handleEdit = (project) => {
  setModalMode('edit')
  setEditData(project)
  setShowModal(true)
}


  const handleModalClose = () => {
  setEditData(null)
  setModalMode('create')  // reset to default mode
  setShowModal(false)
}


  const handleModalSuccess = () => {
    handleModalClose();
    fetchProjects(); 
  };

  const handleProjectCreated = (newProject) => {
    setProjects((prev) => [...prev, newProject])
  }

  if (loading) return <div className="projects-container">Loading projects...</div>
  if (error) return <div className="projects-container error">{error}</div>

  return (
    <div className="projects-container">
      <h1 className="projects-title">Your Projects</h1>

      {isProjectManager && (
        <>
          <button className="create-btn"
              onClick={() => {
                setModalMode('create')     
                setEditData(null)       
                setShowModal(true)        
              }}
            >
            + Create Project
          </button>
          <ProjectModal
          show={showModal}
          onHide={() => setShowModal(false)}
          mode={modalMode}
          initialData={editData}
          onProjectSaved={(updatedProject) => {
            setProjects(prev => {
              if (modalMode === 'edit') {
                return prev.map(p => p.id === updatedProject.id ? updatedProject : p)
              } else {
                return [...prev, updatedProject]
              }
            })
          }}
        />
        </>
      )}

      {projects.length === 0 ? (
        <p>No projects found.</p>
      ) : (
        <div className="project-card-buttons">
          {projects.map((project) => (
            <div className="project-card" key={project.id}>
              <h3>{project.name}</h3>
              <p>{project.description}</p>

             <button
                className="view-btn"
                onClick={() => {
                  setSelectedProjectId(project.id);
                  setShowTaskModal(true);
                }}
              >
                View Tasks
              </button>

              {isProjectManager && (
                <>
                  <button
                    className="edit-btn"
                    onClick={() => {setModalMode('edit')
                      setEditData(project)
                      setShowModal(true)
                    }}
                  >
                    Edit
                  </button>
                  <button
                    className="delete-btn"
                    onClick={() => handleDelete(project.id)}
                  >
                    Delete
                  </button>
                </>
              )}
            </div>
          ))}
        </div>
      )}
      <ProjectTasksModal
            show={showTaskModal}
            onHide={() => setShowTaskModal(false)}
            projectId={selectedProjectId}
            user={user}
            isProjectManager={isProjectManager}
          />    
    </div>
  )
}



export default Projects
