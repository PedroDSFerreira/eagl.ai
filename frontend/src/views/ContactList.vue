<template>
  <div class="container mx-auto px-4 py-8">
    <div class="mb-8">
      <h1 class="text-3xl font-bold tracking-tight">Contacts</h1>
      <p class="text-muted-foreground mt-2">Manage your AI-powered contact directory</p>
    </div>
    
    <div v-if="loading" class="flex justify-center items-center py-12">
      <Loader2 class="h-8 w-8 animate-spin" />
    </div>
    
    <div v-else-if="error" class="text-center py-12">
      <AlertTriangle class="h-12 w-12 text-destructive mx-auto mb-4" />
      <h3 class="text-lg font-semibold mb-2">Error loading contacts</h3>
      <p class="text-muted-foreground mb-4">{{ error }}</p>
      <Button @click="fetchContacts">Try Again</Button>
    </div>
    
    <div v-else-if="contacts.length === 0" class="text-center py-12">
      <Users class="h-12 w-12 text-muted-foreground mx-auto mb-4" />
      <h3 class="text-lg font-semibold mb-2">No contacts</h3>
      <p class="text-muted-foreground mb-4">Get started by creating your first contact.</p>
      <Button as-child>
        <router-link to="/add" class="flex items-center space-x-2">
          <Plus class="h-4 w-4" />
          <span>Add Contact</span>
        </router-link>
      </Button>
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

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Users, Plus, AlertTriangle, Loader2 } from 'lucide-vue-next'
import { useToast } from 'vue-toastification'
import { contactsApi, type Contact } from '@/services/api'
import ContactCard from '@/components/ContactCard.vue'
import Button from '@/components/ui/button.vue'

const contacts = ref<Contact[]>([])
const loading = ref(true)
const error = ref<string | null>(null)
const toast = useToast()

const fetchContacts = async () => {
  try {
    loading.value = true
    error.value = null
    const response = await contactsApi.getContacts()
    contacts.value = response.data
  } catch (err: any) {
    console.error('Error fetching contacts:', err)
    error.value = err.response?.data?.message || 'Failed to load contacts'
    toast.error('Failed to load contacts')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchContacts()
})
</script>