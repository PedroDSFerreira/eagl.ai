<template>
  <form @submit.prevent="handleSubmit" class="space-y-6">
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <label for="name" class="block text-sm font-medium text-gray-700 mb-2">
          Name *
        </label>
        <input
          id="name"
          v-model="form.name"
          type="text"
          required
          class="input-field"
          placeholder="Enter full name"
        />
      </div>
      
      <div>
        <label for="phone" class="block text-sm font-medium text-gray-700 mb-2">
          Phone *
        </label>
        <input
          id="phone"
          v-model="form.phone"
          type="tel"
          required
          class="input-field"
          placeholder="Enter phone number"
        />
      </div>
      
      <div>
        <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
          Email
        </label>
        <input
          id="email"
          v-model="form.email"
          type="email"
          class="input-field"
          placeholder="Enter email address"
        />
      </div>
      
      <div>
        <label for="address" class="block text-sm font-medium text-gray-700 mb-2">
          Address
        </label>
        <input
          id="address"
          v-model="form.address"
          type="text"
          class="input-field"
          placeholder="Enter address"
        />
      </div>
    </div>
    
    <div>
      <label for="image" class="block text-sm font-medium text-gray-700 mb-2">
        Photo
      </label>
      <div class="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-lg hover:border-gray-400 transition-colors">
        <div class="space-y-1 text-center">
          <div v-if="imagePreview" class="mb-4 relative">
            <img :src="imagePreview" alt="Preview" class="mx-auto h-32 w-32 object-cover rounded-lg" />
            <button type="button" class="absolute top-0 right-0 m-1 p-1 bg-white bg-opacity-80 rounded-full shadow hover:bg-red-100 transition-colors" @click="removeImage" aria-label="Remove photo">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-red-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          <PhotoIcon v-else class="mx-auto h-12 w-12 text-gray-400" />
          <div class="flex text-sm text-gray-600">
            <label for="image" class="relative cursor-pointer bg-white rounded-md font-medium text-primary-600 hover:text-primary-500 focus-within:outline-none focus-within:ring-2 focus-within:ring-offset-2 focus-within:ring-primary-500">
              <span>{{ imagePreview ? 'Change photo' : 'Upload a photo' }}</span>
              <input
                id="image"
                ref="imageInput"
                type="file"
                accept="image/*"
                class="sr-only"
                @change="handleImageChange"
              />
            </label>
          </div>
          <p class="text-xs text-gray-500">PNG, JPG, GIF up to 10MB</p>
        </div>
      </div>
    </div>
    
    <div class="flex justify-end space-x-3">
      <button
        type="button"
        @click="$router.go(-1)"
        class="btn-secondary"
      >
        Cancel
      </button>
      <button
        type="submit"
        :disabled="loading"
        class="btn-primary"
      >
        <span v-if="loading" class="flex items-center">
          <svg class="animate-spin -ml-1 mr-3 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          {{ isEdit ? 'Updating...' : 'Creating...' }}
        </span>
        <span v-else>{{ isEdit ? 'Update Contact' : 'Create Contact' }}</span>
      </button>
    </div>
  </form>
</template>

<script>
import { PhotoIcon } from '@heroicons/vue/24/outline'

export default {
  name: 'ContactForm',
  components: {
    PhotoIcon
  },
  props: {
    initialData: {
      type: Object,
      default: () => ({})
    },
    isEdit: {
      type: Boolean,
      default: false
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      form: {
        name: '',
        phone: '',
        email: '',
        address: '',
        image: null
      },
      imagePreview: null
    }
  },
  watch: {
    initialData: {
      handler(newData) {
        if (newData) {
          this.form = {
            name: newData.name || '',
            phone: newData.phone || '',
            email: newData.email || '',
            address: newData.address || '',
            image: null
          }
          if (newData.imageData) {
            this.imagePreview = `data:image/jpeg;base64,${newData.imageData}`
          }
        }
      },
      immediate: true
    }
  },
  methods: {
    handleImageChange(event) {
      const file = event.target.files[0]
      if (file) {
        this.form.image = file
        const reader = new FileReader()
        reader.onload = (e) => {
          this.imagePreview = e.target.result
        }
        reader.readAsDataURL(file)
      }
    },
    removeImage() {
      this.form.image = null
      this.imagePreview = null
      if (this.$refs.imageInput) {
        this.$refs.imageInput.value = ''
      }
    },
    handleSubmit() {
      const formData = new FormData()
      formData.append('name', this.form.name)
      formData.append('phone', this.form.phone)
      formData.append('email', this.form.email || '')
      formData.append('address', this.form.address || '')
      if (this.form.image) {
        formData.append('image', this.form.image)
      }
      this.$emit('form-submit', formData)
    }
  }
}
</script>