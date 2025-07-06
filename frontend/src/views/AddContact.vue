<template>
  <div class="container mx-auto px-4 py-8">
    <div class="max-w-2xl mx-auto">
      <div class="mb-8">
        <h1 class="text-3xl font-bold tracking-tight">Add New Contact</h1>
        <p class="text-muted-foreground mt-2">Create a new contact with AI-powered image analysis</p>
      </div>
      
      <Card class="p-6">
        <ContactForm :loading="loading" @form-submit="handleSubmit" />
      </Card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { contactsApi } from '@/services/api'
import ContactForm from '@/components/ContactForm.vue'
import Card from '@/components/ui/card.vue'

const loading = ref(false)
const router = useRouter()
const toast = useToast()

const handleSubmit = async (formData: FormData) => {
  try {
    loading.value = true
    await contactsApi.createContact(formData)
    toast.success('Contact created successfully!')
    router.push('/')
  } catch (error: any) {
    toast.error(error.response?.data?.message || 'Failed to create contact')
  } finally {
    loading.value = false
  }
}
</script>