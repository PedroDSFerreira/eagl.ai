import axios from 'axios'

const API_BASE_URL = process.env.VUE_APP_API_URL || 'http://localhost:8080/api/v1'

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

export const contactsApi = {
  // Get all contacts
  getContacts() {
    return api.get('/contacts')
  },

  // Get contact by ID
  getContact(id) {
    return api.get(`/contacts/${id}`)
  },

  // Create new contact
  createContact(formData) {
    return api.post('/contacts', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // Update contact
  updateContact(id, formData) {
    return api.put(`/contacts/${id}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // Delete contact
  deleteContact(id) {
    return api.delete(`/contacts/${id}`)
  }
}

export default api