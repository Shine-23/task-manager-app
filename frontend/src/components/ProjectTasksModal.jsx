import { useEffect, useState } from 'react';
import { Modal, Spinner } from 'react-bootstrap';
import api from '../api';
import TaskFormModal from './TaskFormModal';
import '../assests/ProjectTasksModal.css';

function ProjectTasksModal({ show, onHide, projectId, user, isProjectManager }) {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showTaskForm, setShowTaskForm] = useState(false);
  const [editTask, setEditTask] = useState(null);

  useEffect(() => {
    if (!show || !projectId) return;

    const fetchTasks = async () => {
      setLoading(true);
      setError(null);
      try {
        const url = isProjectManager
          ? `/tasks/by-project/${projectId}`
          : `/tasks/by-project-and-user/${projectId}/${user.id}`;
        const res = await api.get(url);
        setTasks(Array.isArray(res.data) ? res.data : res.data.content || []);
      } catch (err) {
        console.error(err);
        setError('Failed to fetch tasks.');
      } finally {
        setLoading(false);
      }
    };

    fetchTasks();
  }, [show, projectId, user, isProjectManager]);

  const handleDelete = async (taskId) => {
    if (!window.confirm('Are you sure you want to delete this task?')) return;
    try {
      await api.delete(`/tasks/${taskId}`);
      setTasks(prev => prev.filter(t => t.id !== taskId));
    } catch (err) {
      console.error('Failed to delete task:', err);
      alert('Task delete failed.');
    }
  };

  const handleTaskSaved = (savedTask) => {
    setTasks(prev =>
      prev.some(t => t.id === savedTask.id)
        ? prev.map(t => t.id === savedTask.id ? savedTask : t)
        : [...prev, savedTask]
    );
  };

  return (
    <>
      <Modal show={show} onHide={onHide} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Tasks in Project</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {loading ? (
            <div className="text-center"><Spinner animation="border" /></div>
          ) : error ? (
            <p className="text-danger">{error}</p>
          ) : (
            <>
              {isProjectManager && (
                <button
                  className="btn-purple mb-3"
                  onClick={() => {
                    setEditTask(null);
                    setShowTaskForm(true);
                  }}
                >
                  + New Task
                </button>
              )}

              {tasks.length === 0 ? (
                <p>No tasks found in this project.</p>
              ) : (
                <ul className="list-group">
                  {tasks.map(task => (
                    <li
                      key={task.id}
                      className="list-group-item d-flex justify-content-between align-items-center"
                    >
                      <div>
                        <strong>{task.title}</strong><br />
                        <small>Status: {task.status} | Due: {new Date(task.dueDate).toLocaleDateString()}</small>
                      </div>
                      <div className="d-flex align-items-center task-actions">
                        <span className="badge badge-purple">
                          {task.assignedUser?.username
                            ? task.assignedUser.username.charAt(0).toUpperCase() + task.assignedUser.username.slice(1)
                            : 'Unassigned'}
                        </span>
                        {isProjectManager && (
                          <>
                            <button
                              className="btn-edit"
                              onClick={() => {
                                setEditTask(task);
                                setShowTaskForm(true);
                              }}
                            >
                              Edit Task
                            </button>
                            <button
                              className="btn-delete"
                              onClick={() => handleDelete(task.id)}
                            >
                              Delete Task
                            </button>
                          </>
                        )}
                      </div>
                    </li>
                  ))}
                </ul>
              )}
            </>
          )}
        </Modal.Body>
      </Modal>

      {isProjectManager && (
        <TaskFormModal
          show={showTaskForm}
          onHide={() => setShowTaskForm(false)}
          initialTask={editTask}
          projectId={projectId}
          onSaved={handleTaskSaved}
        />
      )}
    </>
  );
}

export default ProjectTasksModal;
