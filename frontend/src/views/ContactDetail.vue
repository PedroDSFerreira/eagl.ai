<template>
  <div class="container mx-auto px-4 py-8">
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
    
    <div v-else-if="contact">
      <div class="mb-6 flex justify-between items-start">
        <div>
          <h1 class="text-3xl font-bold tracking-tight">{{ contact.name }}</h1>
          <p class="text-muted-foreground mt-1">Contact Details</p>
        </div>
        <div class="flex space-x-3">
          <DropdownMenu>
            <DropdownMenuTrigger as-child>
              <Button variant="outline" size="icon" aria-label="Actions">
                <MoreVertical class="h-5 w-5" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuItem as-child>
                <router-link :to="`/edit/${contact.id}`" class="flex items-center space-x-2">
                  <Pencil class="h-4 w-4" />
                  <span>Edit</span>
                </router-link>
              </DropdownMenuItem>
              <DropdownMenuItem @click="showDeleteDialog = true" data-destructive>
                <Trash2 class="h-4 w-4 mr-2" />
                <span>Delete</span>
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>
      
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div class="lg:col-span-1">
          <Card class="p-6">
            <div class="text-center space-y-4">
              <Avatar class="h-48 w-48 mx-auto">
                <AvatarImage
                  v-if="contact.imageData"
                  :src="`data:image/jpeg;base64,${contact.imageData}`"
                  :alt="contact.name"
                />
                <AvatarFallback class="text-4xl">
                  <User class="h-24 w-24" />
                </AvatarFallback>
              </Avatar>
              <h2 class="text-xl font-semibold">{{ contact.name }}</h2>
            </div>
          </Card>
        </div>
        
        <div class="lg:col-span-2 space-y-6">
          <Card class="p-6">
            <h3 class="text-lg font-semibold mb-4">Contact Information</h3>
            <div class="space-y-2">
              <CopyField icon="Phone" :value="contact.phone" />
              <CopyField v-if="contact.email" icon="Mail" :value="contact.email" />
              <CopyField v-if="contact.address" icon="MapPin" :value="contact.address" />
            </div>
          </Card>
          
          <Card v-if="contact.description" class="p-6">
            <h3 class="text-lg font-semibold mb-4">AI Description</h3>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <Label class="text-muted-foreground">Age</Label>
                <p class="font-medium">{{ formatAge(contact.description.age) }}</p>
              </div>
              <div>
                <Label class="text-muted-foreground">Sex</Label>
                <p class="font-medium">{{ formatSex(contact.description.sex) }}</p>
              </div>
              <div>
                <Label class="text-muted-foreground">Skin Tone</Label>
                <p class="font-medium">{{ formatSkinTone(contact.description.skinTone) }}</p>
              </div>
              <div>
                <Label class="text-muted-foreground">Eye Color</Label>
                <p class="font-medium">{{ formatEyeColor(contact.description.eyeColor) }}</p>
              </div>
              <div>
                <Label class="text-muted-foreground">Hair Type</Label>
                <p class="font-medium">{{ formatHairType(contact.description.hairType) }}</p>
              </div>
              <div>
                <Label class="text-muted-foreground">Hair Color</Label>
                <p class="font-medium">{{ formatHairColor(contact.description.hairColor) }}</p>
              </div>
              <div>
                <Label class="text-muted-foreground">Facial Hair</Label>
                <p class="font-medium">{{ contact.description.facialHair ? 'Yes' : 'No' }}</p>
              </div>
            </div>
          </Card>
        </div>
      </div>
    </div>
    
    <!-- Delete Confirmation Dialog -->
    <Dialog v-model:open="showDeleteDialog">
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Delete Contact</DialogTitle>
          <DialogDescription>
            Are you sure you want to delete {{ contact?.name }}? This action cannot be undone.
          </DialogDescription>
        </DialogHeader>
        <DialogFooter>
          <Button variant="outline" @click="showDeleteDialog = false">Cancel</Button>
          <Button variant="destructive" @click="deleteContact" :disabled="deleting">
            <Loader2 v-if="deleting" class="h-4 w-4 mr-2 animate-spin" />
            Delete
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { User, Phone, Mail, MapPin, Pencil, Trash2, AlertTriangle, Loader2, MoreVertical } from 'lucide-vue-next'
import { useToast } from 'vue-toastification'
import { contactsApi, type Contact } from '@/services/api'
import Card from '@/components/ui/card.vue'
import Button from '@/components/ui/button.vue'
import Avatar from '@/components/ui/avatar.vue'
import AvatarImage from '@/components/ui/avatar-image.vue'
import AvatarFallback from '@/components/ui/avatar-fallback.vue'
import Label from '@/components/ui/label.vue'
import Dialog from '@/components/ui/dialog.vue'
import DialogContent from '@/components/ui/dialog-content.vue'
import DialogHeader from '@/components/ui/dialog-header.vue'
import DialogTitle from '@/components/ui/dialog-title.vue'
import DialogDescription from '@/components/ui/dialog-description.vue'
import DialogFooter from '@/components/ui/dialog-footer.vue'
import { DropdownMenu, DropdownMenuTrigger, DropdownMenuContent, DropdownMenuItem } from '@/components/ui/dropdown-menu'
import CopyField from '@/components/ui/copy-field.vue'

interface Props {
  id: string
}

const props = defineProps<Props>()

const contact = ref<Contact | null>(null)
const loading = ref(true)
const error = ref<string | null>(null)
const showDeleteDialog = ref(false)
const deleting = ref(false)

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

const deleteContact = async () => {
  try {
    deleting.value = true
    await contactsApi.deleteContact(props.id)
    toast.success('Contact deleted successfully')
    router.push('/')
  } catch (err: any) {
    console.error('Error deleting contact:', err)
    toast.error('Failed to delete contact')
  } finally {
    deleting.value = false
    showDeleteDialog.value = false
  }
}

const formatAge = (age: string) => {
  const ageMap = {
    'CHILD': 'Child',
    'ADOLESCENT': 'Adolescent',
    'YOUNG_ADULT': 'Young Adult',
    'ADULT': 'Adult',
    'ELDER': 'Elder'
  }
  return ageMap[age as keyof typeof ageMap] || age
}

const formatSex = (sex: string) => {
  return sex === 'MALE' ? 'Male' : 'Female'
}

const formatSkinTone = (skinTone: string) => {
  const toneMap = {
    'DARK': 'Dark',
    'MEDIUM': 'Medium',
    'LIGHT': 'Light'
  }
  return toneMap[skinTone as keyof typeof toneMap] || skinTone
}

const formatEyeColor = (eyeColor: string) => {
  const colorMap = {
    'BROWN': 'Brown',
    'GREEN': 'Green',
    'BLUE': 'Blue'
  }
  return colorMap[eyeColor as keyof typeof colorMap] || eyeColor
}

const formatHairType = (hairType: string) => {
  const typeMap = {
    'LONG': 'Long',
    'MEDIUM': 'Medium',
    'SHORT': 'Short',
    'NONE': 'None'
  }
  return typeMap[hairType as keyof typeof typeMap] || hairType
}

const formatHairColor = (hairColor: string) => {
  const colorMap = {
    'BLACK': 'Black',
    'BROWN': 'Brown',
    'BLONDE': 'Blonde',
    'GINGER': 'Red',
    'GRAY': 'Gray',
    'WHITE': 'White',
    'PAINTED': 'Colored',
    'NONE': 'None'
  }
  return colorMap[hairColor as keyof typeof colorMap] || hairColor
}

onMounted(() => {
  fetchContact()
})
</script>

<style>
[data-destructive] {
  color: hsl(var(--destructive)) !important;
  font-weight: 600;
}
[data-destructive][data-highlighted] {
  background-color: hsl(var(--destructive)) !important;
  color: hsl(var(--destructive-foreground)) !important;
}
</style>