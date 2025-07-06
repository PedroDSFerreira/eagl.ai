<template>
  <div class="container mx-auto px-4 py-8">
    <div class="max-w-2xl mx-auto">
      <div class="mb-8">
        <h1 class="text-3xl font-bold tracking-tight">Edit Contact</h1>
        <p class="text-muted-foreground mt-2">Update contact information</p>
      </div>
      
      <div v-if="loading" class="flex justify-center items-center py-12">
        <Loader2 class="h-8 w-8 animate-spin" />
      </div>
      
      <div v-else-if="error" class="text-center py-12">
        <AlertTriangle class="h-12 w-12 text-destructive mx-auto mb-4" />
        <h3 class="text-lg font-semibold mb-2">Error loading contact</h3>
        <p class="text-muted-foreground mb-4">{{ error }}</p>
        <Button as-child>
          <router-link to="/">Back to Contacts</router-link>
        </Button>
      </div>
      
      <Card v-else-if="contact" class="p-6">
        <ContactForm
          :initial-data="contact"
          :is-edit="true"
          :loading="updating"
          @form-submit="handleSubmit"
        />
      </Card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { AlertTriangle, Loader2 } from 'lucide-vue-next'
import { useToast } from 'vue-toastification'
import { contactsApi, type Contact } from '@/services/api'
import ContactForm from '@/components/ContactForm.vue'
import Card from '@/components/ui/card.vue'
import Button from '@/components/ui/button.vue'

interface Props {
  id: string
}

const props = defineProps<Props>()

const contact = ref<Contact | null>(null)
const loading = ref(true)
const updating = ref(false)
const error = ref<string | null>(null)

const router = useRouter()
const toast = useToast()

const fetchContact = async () => {
  try {
    loading.value = true
    error.value = null
    const response = await contactsApi.getContact(props.id)
    contact.value = response.data
  } catch (err: any) {
    console.error('Error fetching contact:', err)
    error.value = err.response?.data?.message || 'Failed to load contact'
    toast.error('Failed to load contact')
  } finally {
    loading.value = false
  }
}

const handleSubmit = async (formData: FormData) => {
  try {
    updating.value = true
    await contactsApi.updateContact(props.id, formData)
    toast.success('Contact updated successfully!')
    router.push(`/contacts/${props.id}`)
  } catch (error: any) {
    console.error('Error updating contact:', error)
    toast.error(error.response?.data?.message || 'Failed to update contact')
  } finally {
    updating.value = false
  }
}

onMounted(() => {
  fetchContact()
})
</script>