<template>
  <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900">Edit Contact</h1>
      <p class="mt-2 text-gray-600">Update contact information</p>
    </div>
    
    <div v-if="loading" class="flex justify-center items-center py-12">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
    </div>
    
    <div v-else-if="error" class="text-center py-12">
      <ExclamationTriangleIcon class="mx-auto h-12 w-12 text-red-400" />
      <h3 class="mt-2 text-sm font-medium text-gray-900">Error loading contact</h3>
      <p class="mt-1 text-sm text-gray-500">{{ error }}</p>
      <router-link to="/" class="mt-4 btn-primary">
        Back to Contacts
      </router-link>
    </div>
    
    <div v-else-if="contact" class="card">
      <ContactForm
        :initial-data="contact"
        :is-edit="true"
        :loading="updating"
        @form-submit="handleSubmit"
      />
    </div>
  </div>
</template>

<script>
import { contactsApi } from '../services/api'
import ContactForm from '../components/ContactForm.vue'
import { ExclamationTriangleIcon } from '@heroicons/vue/24/outline'
import { useToast } from 'vue-toastification'

export default {
  name: 'EditContact',
  components: {
    ContactForm,
    ExclamationTriangleIcon
  },
  props: {
    id: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      contact: null,
      loading: true,
      updating: false,
      error: null
    }
  },
  setup() {
    const toast = useToast()
    return { toast }
  },
  async created() {
    await this.fetchContact()
  },
  methods: {
    async fetchContact() {
      try {
        this.loading = true
        this.error = null
        const response = await contactsApi.getContact(this.id)
        this.contact = response.data
      } catch (error) {
        console.error('Error fetching contact:', error)
        this.error = error.response?.data?.message || 'Failed to load contact'
        this.toast.error('Failed to load contact')
      } finally {
        this.loading = false
      }
    },
    async handleSubmit(formData) {
      try {
        this.updating = true
        await contactsApi.updateContact(this.id, formData)
        this.toast.success('Contact updated successfully!')
        this.$router.push(`/contacts/${this.id}`)
      } catch (error) {
        console.error('Error updating contact:', error)
        this.toast.error(error.response?.data?.message || 'Failed to update contact')
      } finally {
        this.updating = false
      }
    }
  }
}
</script>