<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900">Contacts</h1>
      <p class="mt-2 text-gray-600">Manage your AI-powered contact directory</p>
    </div>
    
    <div v-if="loading" class="flex justify-center items-center py-12">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
    </div>
    
    <div v-else-if="error" class="text-center py-12">
      <ExclamationTriangleIcon class="mx-auto h-12 w-12 text-red-400" />
      <h3 class="mt-2 text-sm font-medium text-gray-900">Error loading contacts</h3>
      <p class="mt-1 text-sm text-gray-500">{{ error }}</p>
      <button @click="fetchContacts" class="mt-4 btn-primary">
        Try Again
      </button>
    </div>
    
    <div v-else-if="contacts.length === 0" class="text-center py-12">
      <UserGroupIcon class="mx-auto h-12 w-12 text-gray-400" />
      <h3 class="mt-2 text-sm font-medium text-gray-900">No contacts</h3>
      <p class="mt-1 text-sm text-gray-500">Get started by creating your first contact.</p>
      <router-link to="/add" class="mt-4 btn-primary inline-flex items-center">
        <PlusIcon class="w-4 h-4 mr-2" />
        Add Contact
      </router-link>
    </div>
    
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <ContactCard
        v-for="contact in contacts"
        :key="contact.id"
        :contact="contact"
      />
    </div>
  </div>
</template>

<script>
import { contactsApi } from '../services/api'
import ContactCard from '../components/ContactCard.vue'
import { UserGroupIcon, PlusIcon, ExclamationTriangleIcon } from '@heroicons/vue/24/outline'
import { useToast } from 'vue-toastification'

export default {
  name: 'ContactList',
  components: {
    ContactCard,
    UserGroupIcon,
    PlusIcon,
    ExclamationTriangleIcon
  },
  data() {
    return {
      contacts: [],
      loading: true,
      error: null
    }
  },
  setup() {
    const toast = useToast()
    return { toast }
  },
  async created() {
    await this.fetchContacts()
  },
  methods: {
    async fetchContacts() {
      try {
        this.loading = true
        this.error = null
        const response = await contactsApi.getContacts()
        this.contacts = response.data
      } catch (error) {
        console.error('Error fetching contacts:', error)
        this.error = error.response?.data?.message || 'Failed to load contacts'
        this.toast.error('Failed to load contacts')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>