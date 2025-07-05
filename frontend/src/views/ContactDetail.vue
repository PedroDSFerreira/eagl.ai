<template>
  <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
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
    
    <div v-else-if="contact">
      <div class="mb-6 flex justify-between items-start">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">{{ contact.name }}</h1>
          <p class="mt-1 text-gray-600">Contact Details</p>
        </div>
        <div class="flex space-x-3">
          <router-link :to="`/edit/${contact.id}`" class="btn-secondary">
            <PencilIcon class="w-4 h-4 mr-2" />
            Edit
          </router-link>
          <button @click="confirmDelete" class="btn-danger">
            <TrashIcon class="w-4 h-4 mr-2" />
            Delete
          </button>
        </div>
      </div>
      
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div class="lg:col-span-1">
          <div class="card">
            <div class="text-center">
              <div v-if="contact.imageData" class="mx-auto w-48 h-48 rounded-lg overflow-hidden bg-gray-200 mb-4">
                <img 
                  :src="`data:image/jpeg;base64,${contact.imageData}`" 
                  :alt="contact.name"
                  class="w-full h-full object-cover"
                />
              </div>
              <div v-else class="mx-auto w-48 h-48 rounded-lg bg-gray-200 flex items-center justify-center mb-4">
                <UserIcon class="w-24 h-24 text-gray-400" />
              </div>
              <h2 class="text-xl font-semibold text-gray-900">{{ contact.name }}</h2>
            </div>
          </div>
        </div>
        
        <div class="lg:col-span-2 space-y-6">
          <div class="card">
            <h3 class="text-lg font-semibold text-gray-900 mb-4">Contact Information</h3>
            <div class="space-y-4">
              <div class="flex items-center">
                <PhoneIcon class="w-5 h-5 text-gray-400 mr-3" />
                <span class="text-gray-900">{{ contact.phone }}</span>
              </div>
              <div v-if="contact.email" class="flex items-center">
                <EnvelopeIcon class="w-5 h-5 text-gray-400 mr-3" />
                <span class="text-gray-900">{{ contact.email }}</span>
              </div>
              <div v-if="contact.address" class="flex items-center">
                <MapPinIcon class="w-5 h-5 text-gray-400 mr-3" />
                <span class="text-gray-900">{{ contact.address }}</span>
              </div>
            </div>
          </div>
          
          <div v-if="contact.description" class="card">
            <h3 class="text-lg font-semibold text-gray-900 mb-4">AI Description</h3>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <span class="text-sm font-medium text-gray-500">Age</span>
                <p class="text-gray-900">{{ formatAge(contact.description.age) }}</p>
              </div>
              <div>
                <span class="text-sm font-medium text-gray-500">Sex</span>
                <p class="text-gray-900">{{ formatSex(contact.description.sex) }}</p>
              </div>
              <div>
                <span class="text-sm font-medium text-gray-500">Skin Tone</span>
                <p class="text-gray-900">{{ formatSkinTone(contact.description.skinTone) }}</p>
              </div>
              <div>
                <span class="text-sm font-medium text-gray-500">Eye Color</span>
                <p class="text-gray-900">{{ formatEyeColor(contact.description.eyeColor) }}</p>
              </div>
              <div>
                <span class="text-sm font-medium text-gray-500">Hair Type</span>
                <p class="text-gray-900">{{ formatHairType(contact.description.hairType) }}</p>
              </div>
              <div>
                <span class="text-sm font-medium text-gray-500">Hair Color</span>
                <p class="text-gray-900">{{ formatHairColor(contact.description.hairColor) }}</p>
              </div>
              <div>
                <span class="text-sm font-medium text-gray-500">Facial Hair</span>
                <p class="text-gray-900">{{ contact.description.facialHair ? 'Yes' : 'No' }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Delete Confirmation Modal -->
    <div v-if="showDeleteModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div class="mt-3 text-center">
          <ExclamationTriangleIcon class="mx-auto h-12 w-12 text-red-400" />
          <h3 class="text-lg font-medium text-gray-900 mt-2">Delete Contact</h3>
          <div class="mt-2 px-7 py-3">
            <p class="text-sm text-gray-500">
              Are you sure you want to delete {{ contact?.name }}? This action cannot be undone.
            </p>
          </div>
          <div class="flex justify-center space-x-3 mt-4">
            <button @click="showDeleteModal = false" class="btn-secondary">
              Cancel
            </button>
            <button @click="deleteContact" :disabled="deleting" class="btn-danger">
              <span v-if="deleting">Deleting...</span>
              <span v-else>Delete</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { contactsApi } from '../services/api'
import { 
  UserIcon, 
  PhoneIcon, 
  EnvelopeIcon, 
  MapPinIcon, 
  PencilIcon, 
  TrashIcon,
  ExclamationTriangleIcon 
} from '@heroicons/vue/24/outline'
import { useToast } from 'vue-toastification'

export default {
  name: 'ContactDetail',
  components: {
    UserIcon,
    PhoneIcon,
    EnvelopeIcon,
    MapPinIcon,
    PencilIcon,
    TrashIcon,
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
      error: null,
      showDeleteModal: false,
      deleting: false
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
    confirmDelete() {
      this.showDeleteModal = true
    },
    async deleteContact() {
      try {
        this.deleting = true
        await contactsApi.deleteContact(this.id)
        this.toast.success('Contact deleted successfully')
        this.$router.push('/')
      } catch (error) {
        console.error('Error deleting contact:', error)
        this.toast.error('Failed to delete contact')
      } finally {
        this.deleting = false
        this.showDeleteModal = false
      }
    },
    formatAge(age) {
      const ageMap = {
        'CHILD': 'Child',
        'ADOLESCENT': 'Adolescent',
        'YOUNG_ADULT': 'Young Adult',
        'ADULT': 'Adult',
        'ELDER': 'Elder'
      }
      return ageMap[age] || age
    },
    formatSex(sex) {
      return sex === 'MALE' ? 'Male' : 'Female'
    },
    formatSkinTone(skinTone) {
      const toneMap = {
        'DARK': 'Dark',
        'MEDIUM': 'Medium',
        'LIGHT': 'Light'
      }
      return toneMap[skinTone] || skinTone
    },
    formatEyeColor(eyeColor) {
      const colorMap = {
        'BROWN': 'Brown',
        'GREEN': 'Green',
        'BLUE': 'Blue'
      }
      return colorMap[eyeColor] || eyeColor
    },
    formatHairType(hairType) {
      const typeMap = {
        'LONG': 'Long',
        'MEDIUM': 'Medium',
        'SHORT': 'Short',
        'NONE': 'None'
      }
      return typeMap[hairType] || hairType
    },
    formatHairColor(hairColor) {
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
      return colorMap[hairColor] || hairColor
    }
  }
}
</script>