<template>
  <form @submit.prevent="handleSubmit" class="space-y-6">
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="space-y-2">
        <div class="flex items-center gap-2 min-h-[1.5rem]">
          <Label for="name">Name <span class="text-red-500">*</span></Label>
          <template v-if="!showNickname && !form.nickname">
            <Badge as="button" @click="showNickname = true" class="cursor-pointer flex items-center gap-1 ml-2">
              <svg xmlns="http://www.w3.org/2000/svg" class="inline h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>Add nickname
            </Badge>
          </template>
        </div>
        <Input
          id="name"
          v-model="form.name"
          type="text"
          required
          placeholder="Enter full name"
        />
      </div>
      <div class="space-y-2" v-if="showNickname">
        <div class="flex items-center gap-1 min-h-[1.5rem]">
          <Label for="nickname">Nickname</Label>
        </div>
        <Input
          id="nickname"
          v-model="form.nickname"
          type="text"
          placeholder="Enter nickname"
        />
      </div>
      <div class="space-y-2">
        <div class="flex items-center gap-1 min-h-[1.5rem]">
          <Label for="phone">Phone <span class="text-red-500">*</span></Label>
        </div>
        <Input
          id="phone"
          v-model="form.phone"
          type="tel"
          required
          placeholder="Enter phone number"
          pattern="[0-9]+"
          inputmode="numeric"
        />
      </div>
      <div class="space-y-2">
        <div class="flex items-center gap-1 min-h-[1.5rem]">
          <Label for="email">Email</Label>
        </div>
        <Input
          id="email"
          v-model="form.email"
          type="email"
          placeholder="Enter email address"
        />
      </div>
      <div class="space-y-2">
        <div class="flex items-center gap-1 min-h-[1.5rem]">
          <Label for="birthday">Birthday</Label>
        </div>
          <VueDatePicker
            v-model="form.birthday"
            :dark="isDark"
            :teleport="false"
            :enable-time-picker="false"
            :auto-apply="true"
            :format="'yyyy-MM-dd'"
            input-class="flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-sm shadow-sm transition-colors file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50"
            placeholder="YYYY-MM-DD"
          />
      </div>
      <div class="space-y-2">
        <div class="flex items-center gap-1 min-h-[1.5rem]">
          <Label for="address">Address</Label>
        </div>
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
            <img :src="imagePreview" alt="Preview" class="h-32 w-32 object-cover rounded-lg mx-auto cursor-pointer" @click="triggerFileInput" />
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
          <p class="text-xs text-muted-foreground mt-2">Click the image to change photo</p>
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
    <div class="space-y-2">
      <Label for="notes">Notes</Label>
      <textarea
        id="notes"
        v-model="form.notes"
        rows="3"
        class="w-full rounded-lg border border-border bg-background px-3 py-2 text-sm shadow-sm focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary"
        placeholder="Additional notes about this contact"
      />
    </div>
    <div class="flex justify-end space-x-3">
      <Button as="button" type="submit" :disabled="loading || !isFormValid">
        <Loader2 v-if="loading" class="h-4 w-4 mr-2 animate-spin" />
        {{ isEdit ? 'Update Contact' : 'Create Contact' }}
      </Button>
    </div>
  </form>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, computed } from 'vue'
import { ImageIcon, X, Loader2 } from 'lucide-vue-next'
import Button from '@/components/ui/button.vue'
import Input from '@/components/ui/input.vue'
import Label from '@/components/ui/label.vue'
import Badge from '@/components/ui/badge.vue'

import VueDatePicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'

const isDark = ref(document.documentElement.classList.contains('dark'))

onMounted(() => {
  const updateTheme = () => {
    isDark.value = document.documentElement.classList.contains('dark')
  }
  updateTheme()
  const observer = new MutationObserver(() => {
    updateTheme()
  })
  observer.observe(document.documentElement, { attributes: true, attributeFilter: ['class'] })
})

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

// Use ref instead of reactive for better debugging
const showNickname = ref(false)
const form = ref({
  name: '',
  nickname: '',
  phone: '',
  email: '',
  birthday: '',
  address: '',
  notes: '',
  image: null as File | null
})

const imagePreview = ref<string | null>(null)
const fileInput = ref<HTMLInputElement>()

// Computed property to check if form is valid
const isFormValid = computed(() => {
  return form.value.name.trim() !== '' && form.value.phone.trim() !== ''
})

watch(() => props.initialData, (newData) => {
  if (newData) {
    form.value.name = newData.name || ''
    form.value.nickname = newData.nickname || ''
    form.value.phone = newData.phone || ''
    form.value.email = newData.email || ''
    form.value.birthday = newData.birthday || ''
    form.value.address = newData.address || ''
    form.value.notes = newData.notes || ''
    form.value.image = null
    if (newData.imageData) {
      imagePreview.value = `data:image/jpeg;base64,${newData.imageData}`
    }
    // Show nickname input if nickname is present
    if (newData.nickname && newData.nickname.trim() !== '') {
      showNickname.value = true
    }
  }
}, { immediate: true })
// Also watch form.nickname for manual input (e.g., user types nickname directly)
watch(() => form.value.nickname, (nickname) => {
  if (nickname && nickname.trim() !== '') {
    showNickname.value = true
  }
})

const triggerFileInput = () => {
  fileInput.value?.click()
}

const handleImageChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    form.value.image = file
    const reader = new FileReader()
    reader.onload = (e) => {
      imagePreview.value = e.target?.result as string
    }
    reader.readAsDataURL(file)
  }
}

const removeImage = () => {
  form.value.image = null
  imagePreview.value = null
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

const handleSubmit = () => {
  // Validate required fields before submission
  if (!form.value.name.trim() || !form.value.phone.trim()) {
    console.error('Name and phone are required')
    return
  }
  const formData = new FormData()
  formData.append('name', form.value.name.trim())
  if (form.value.nickname && form.value.nickname.trim() !== '') {
    formData.append('nickname', form.value.nickname.trim())
  }
  formData.append('phone', form.value.phone.trim())
  if (form.value.email && form.value.email.trim() !== '') {
    formData.append('email', form.value.email.trim())
  }
  if (form.value.birthday && form.value.birthday !== '') {
    formData.append('birthday', form.value.birthday)
  }
  if (form.value.address && form.value.address.trim() !== '') {
    formData.append('address', form.value.address.trim())
  }
  if (form.value.notes && form.value.notes.trim() !== '') {
    formData.append('notes', form.value.notes.trim())
  }
  if (form.value.image) {
    formData.append('image', form.value.image)
  }
  emit('form-submit', formData)
}
</script>