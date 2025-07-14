import { useEffect, useState } from 'react'
import { Modal, Button, Form } from 'react-bootstrap'
import api from '../api'

function ProjectModal({ show, onHide, mode = 'create', initialData = {}, onProjectSaved }) {
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [loading, setLoading] = useState(false)
  const [usersList, setUsersList] = useState([])
  const [selectedUserIds, setSelectedUserIds] = useState([])

  // Fetch users list once
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const res = await api.get('/users/role/ROLE_USER?page=0&size=100')
        setUsersList(res.data.content || res.data)
      } catch (err) {
        console.error('Failed to fetch users:', err)
      }
    }

    fetchUsers()
  }, [])

   // Reset form fields based on modal open + mode/initialData
    useEffect(() => {
      if (show) {
        if (mode === 'edit' && initialData) {
          setName(initialData.name || '')
          setDescription(initialData.description || '')
          setSelectedUserIds(initialData.members?.map((user) => user.id) || [])
        } else {
          setName('')
          setDescription('')
          setSelectedUserIds([])
        }
      }
    }, [show, mode, initialData])

  const handleSubmit = async () => {
    setLoading(true)

    try {
      if (mode === 'create') {
        const res = await api.post('/projects', {
          name,
          description,
          memberIds: selectedUserIds
        })
        onProjectSaved(res.data)
      } else {
        const res = await api.put(`/projects/${initialData.id}`, {
          ...initialData,
          name,
          description,
          membersId: selectedUserIds 
        })
        onProjectSaved(res.data)
      }

      onHide()
    } catch (err) {
      console.error(`Error ${mode === 'edit' ? 'updating' : 'creating'} project:`, err)
    } finally {
      setLoading(false)
    }
  }

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header closeButton>
        <Modal.Title>{mode === 'edit' ? 'Edit Project' : 'Create Project'}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Group className="mb-3">
            <Form.Label>Name</Form.Label>
            <Form.Control
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Enter project name"
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Description</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Enter description"
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Assign Users</Form.Label>
            <Form.Select
              multiple
              value={selectedUserIds}
              onChange={(e) => {
                const selected = Array.from(e.target.selectedOptions).map(opt => Number(opt.value))
                setSelectedUserIds(selected)
              }}
            >
              {usersList.map((user) => (
                <option key={user.id} value={user.id}>
                  {user.username}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>Cancel</Button>
        <Button variant="primary" onClick={handleSubmit} disabled={loading}>
          {loading
            ? mode === 'edit' ? 'Updating...' : 'Creating...'
            : mode === 'edit' ? 'Update' : 'Create'}
        </Button>
      </Modal.Footer>
    </Modal>
  )
}

export default ProjectModal
