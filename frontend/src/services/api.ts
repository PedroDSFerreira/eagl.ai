import axios from 'axios'

// Use runtime-injected API URL if available, else fallback to Vite env, else default
const API_BASE_URL = (window.__VITE_API_URL__ as string | undefined) || import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1'

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

export interface Contact {
  id: number
  name: string
  phone: string
  email?: string
  address?: string
  imageData?: string
  thumbnail?: string
  description?: ContactDescription
  birthday?: string
  nickname?: string
  notes?: string
}

export interface ContactDescription {
  age: 'CHILD' | 'ADOLESCENT' | 'YOUNG_ADULT' | 'ADULT' | 'ELDER'
  sex: 'MALE' | 'FEMALE'
  skinTone: 'DARK' | 'MEDIUM' | 'LIGHT'
  eyeColor: 'BROWN' | 'GREEN' | 'BLUE'
  hairType: 'LONG' | 'MEDIUM' | 'SHORT' | 'NONE'
  hairColor: 'BLACK' | 'BROWN' | 'GRAY' | 'WHITE' | 'BLONDE' | 'GINGER' | 'PAINTED' | 'NONE'
  facialHair: boolean
}

export const contactsApi = {
  // Get all contacts
  getContacts(): Promise<{ data: Contact[] }> {
    return api.get('/contacts')
  },

  // Get contact by ID
  getContact(id: string | number): Promise<{ data: Contact }> {
    return api.get(`/contacts/${id}`)
  },

  // Create new contact
  createContact(formData: FormData): Promise<{ data: Contact }> {
    return api.post('/contacts', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // Update contact
  updateContact(id: string | number, formData: FormData): Promise<{ data: Contact }> {
    return api.put(`/contacts/${id}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // Delete contact
  deleteContact(id: string | number): Promise<void> {
    return api.delete(`/contacts/${id}`)
  }
}

export default api