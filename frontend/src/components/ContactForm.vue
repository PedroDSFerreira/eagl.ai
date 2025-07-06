<template>
  <form @submit.prevent="handleSubmit" class="space-y-6">
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="space-y-2">
        <Label for="name">Name *</Label>
        <Input
          id="name"
          v-model="form.name"
          type="text"
          required
          placeholder="Enter full name"
        />
      </div>
      
      <div class="space-y-2">
        <Label for="phone">Phone *</Label>
        <Input
          id="phone"
          v-model="form.phone"
          type="tel"
          required
          placeholder="Enter phone number"
        />
      </div>
      
      <div class="space-y-2">
        <Label for="email">Email</Label>
        <Input
          id="email"
          v-model="form.email"
          type="email"
          placeholder="Enter email address"
        />
      </div>
      
      <div class="space-y-2">
        <Label for="address">Address</Label>
        <Input
          id="address"
          v-model="form.address"
          type="text"
          placeholder="Enter address"
        />
      </div>
    </div>
    
    <div class="space-y-2">
      <Label>Photo</Label>
      <div class="border-2 border-dashed border-border rounded-lg p-6 text-center hover:border-primary/50 transition-colors">
        <div v-if="imagePreview" class="space-y-4">
          <div class="relative inline-block">
            <img :src="imagePreview" alt="Preview" class="h-32 w-32 object-cover rounded-lg mx-auto" />
            <Button
              type="button"
              variant="destructive"
              size="icon"
              class="absolute -top-2 -right-2 h-6 w-6"
              @click="removeImage"
            >
              <X class="h-3 w-3" />
            </Button>
          </div>
          <Button type="button" variant="outline" @click="triggerFileInput">
            Change Photo
          </Button>
        </div>
        <div v-else class="space-y-4">
          <ImageIcon class="h-12 w-12 text-muted-foreground mx-auto" />
          <div>
            <Button type="button" variant="outline" @click="triggerFileInput">
              Upload Photo
            </Button>
            <p class="text-xs text-muted-foreground mt-2">PNG, JPG, GIF up to 10MB</p>
          </div>
        </div>
        <input
          ref="fileInput"
          type="file"
          accept="image/*"
          class="hidden"
          @change="handleImageChange"
        />
      </div>
    </div>
    
    <div class="flex justify-end space-x-3">
      <Button type="button" variant="outline" @click="$router.go(-1)">
        Cancel
      </Button>
      <Button type="submit" :disabled="loading">
        <Loader2 v-if="loading" class="h-4 w-4 mr-2 animate-spin" />
        {{ isEdit ? 'Update Contact' : 'Create Contact' }}
      </Button>
    </div>
  </form>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import { ImageIcon, X, Loader2 } from 'lucide-vue-next'
import type { Contact } from '@/services/api'
import Button from '@/components/ui/button.vue'
import Input from '@/components/ui/input.vue'
import Label from '@/components/ui/label.vue'

interface Props {
  initialData?: Contact
  isEdit?: boolean
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  isEdit: false,
  loading: false
})

const emit = defineEmits<{
  'form-submit': [formData: FormData]
}>()

const form = reactive({
  name: '',
  phone: '',
  email: '',
  address: '',
  image: null as File | null
})

const imagePreview = ref<string | null>(null)
const fileInput = ref<HTMLInputElement>()

watch(() => props.initialData, (newData) => {
  if (newData) {
    form.name = newData.name || ''
    form.phone = newData.phone || ''
    form.email = newData.email || ''
    form.address = newData.address || ''
    form.image = null
    
    if (newData.imageData) {
      imagePreview.value = `data:image/jpeg;base64,${newData.imageData}`
    }
  }
}, { immediate: true })

const triggerFileInput = () => {
  fileInput.value?.click()
}

const handleImageChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    form.image = file
    const reader = new FileReader()
    reader.onload = (e) => {
      imagePreview.value = e.target?.result as string
    }
    reader.readAsDataURL(file)
  }
}

const removeImage = () => {
  form.image = null
  imagePreview.value = null
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

const handleSubmit = () => {
  const formData = new FormData()
  formData.append('name', form.name)
  formData.append('phone', form.phone)
  formData.append('email', form.email || '')
  formData.append('address', form.address || '')
  if (form.image) {
    formData.append('image', form.image)
  }
  emit('form-submit', formData)
}
</script>