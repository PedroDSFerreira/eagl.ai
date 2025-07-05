<template>
  <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900">Add New Contact</h1>
      <p class="mt-2 text-gray-600">Create a new contact with AI-powered image analysis</p>
    </div>
    
    <div class="card">
      <ContactForm
        :loading="loading"
        @form-submit="handleSubmit"
      />
    </div>
  </div>
</template>

<script>
import { contactsApi } from '../services/api'
import ContactForm from '../components/ContactForm.vue'
import { useToast } from 'vue-toastification'

export default {
  name: 'AddContact',
  components: {
    ContactForm
  },
  data() {
    return {
      loading: false
    }
  },
  setup() {
    const toast = useToast()
    return { toast }
  },
  methods: {
    async handleSubmit(formData) {
      try {
        this.loading = true
        await contactsApi.createContact(formData)
        this.toast.success('Contact created successfully!')
        this.$router.push('/')
      } catch (error) {
        console.error('Error creating contact:', error)
        this.toast.error(error.response?.data?.message || 'Failed to create contact')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>