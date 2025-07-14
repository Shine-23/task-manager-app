import { useEffect, useState } from 'react';
import { Modal, Button, Form, Spinner, Alert } from 'react-bootstrap';
import api from '../api';
import '../assets/TaskFormModal.css'; 

function TaskFormModal({ show, onHide, initialTask = null, projectId, onSaved }) {
  const isEdit = Boolean(initialTask);

  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [status, setStatus] = useState('PENDING');
  const [dueDate, setDueDate] = useState('');
  const [assignedUserId, setAssignedUserId] = useState('');
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loadingUsers, setLoadingUsers] = useState(false);
  const [error, setError] = useState(null);

  // Load form values when editing
  useEffect(() => {
    if (isEdit && initialTask) {
      setTitle(initialTask.title || '');
      setDescription(initialTask.description || '');
      setStatus(initialTask.status || 'PENDING');
      setDueDate(initialTask.dueDate ? initialTask.dueDate.substring(0, 10) : '');
      setAssignedUserId(initialTask.assignedUser?.id || '');
    } else {
      setTitle('');
      setDescription('');
      setStatus('PENDING');
      setDueDate('');
      setAssignedUserId('');
    }
  }, [initialTask, isEdit, show]);

  // Fetch users assigned to the project
  useEffect(() => {
    const fetchUsers = async () => {
      setLoadingUsers(true);
      try {
        const res = await api.get(`/projects/${projectId}/assigned-users`);
        const fetchedUsers = Array.isArray(res.data.content) ? res.data.content : [];
        console.log('Users:', fetchedUsers);
        setUsers(fetchedUsers);

      } catch (err) {
        console.error('Failed to fetch users:', err);
        setUsers([]);
      } finally {
        setLoadingUsers(false);
      }
    };

    if (show && projectId) {
      fetchUsers();
    }
  }, [projectId, show]);

  const handleSubmit = async () => {
    if (!title.trim() || !dueDate) {
      setError('Title and due date are required.');
      return;
    }

    setLoading(true);
    setError(null);

    const taskPayload = {
      title,
      description,
      status,
      dueDate,
      projectId,
      assignedUserId: assignedUserId || null,
    };



    try {
      const res = isEdit
        ? await api.put(`/tasks/${initialTask.id}`, taskPayload)
        : await api.post('/tasks', taskPayload);

      onSaved(res.data);
      onHide();
    } catch (err) {
      console.error('Failed to save task:', err);
      setError('Error saving task. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header closeButton>
        <Modal.Title>{isEdit ? 'Edit Task' : 'Create Task'}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {error && <Alert variant="danger">{error}</Alert>}

        <Form>
          <Form.Group className="mb-3">
            <Form.Label>Title</Form.Label>
            <Form.Control
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Task title"
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Description</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Task description"
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Status</Form.Label>
            <Form.Select value={status} onChange={(e) => setStatus(e.target.value)}>
              <option value="PENDING">Pending</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="COMPLETED">Completed</option>
            </Form.Select>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Due Date</Form.Label>
            <Form.Control
              type="date"
              value={dueDate}
              onChange={(e) => setDueDate(e.target.value)}
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Assign to</Form.Label>
            <Form.Select
                value={assignedUserId}
                onChange={(e) => setAssignedUserId(e.target.value)}
            >
                <option value="">Unassigned</option>
                {Array.isArray(users) &&
                users.map((user) => (
                    user.id && (
                    <option key={user.id} value={user.id}>
                        {user.username}
                    </option>
                    )
                ))}
            </Form.Select>
            </Form.Group>

        </Form>
      </Modal.Body>
        <Modal.Footer>
          <button className="btn btn-cancel" onClick={onHide} disabled={loading}>
            Cancel
          </button>
          <button className="btn btn-save" onClick={handleSubmit} disabled={loading}>
            {loading ? (isEdit ? 'Updating...' : 'Creating...') : (isEdit ? 'Update Task' : 'Create Task')}
          </button>
        </Modal.Footer>
    </Modal>
  );
}

export default TaskFormModal;
