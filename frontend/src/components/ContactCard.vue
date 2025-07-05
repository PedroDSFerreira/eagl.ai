<template>
  <div class="card hover:shadow-md transition-shadow duration-200 cursor-pointer" @click="viewContact">
    <div class="flex items-start space-x-4">
      <div class="flex-shrink-0">
        <div v-if="contact.thumbnail" class="w-16 h-16 rounded-full overflow-hidden bg-gray-200">
          <img 
            :src="`data:image/jpeg;base64,${contact.thumbnail}`" 
            :alt="contact.name"
            class="w-full h-full object-cover"
          />
        </div>
        <div v-else class="w-16 h-16 rounded-full bg-gray-200 flex items-center justify-center">
          <UserIcon class="w-8 h-8 text-gray-400" />
        </div>
      </div>
      
      <div class="flex-1 min-w-0">
        <h3 class="text-lg font-semibold text-gray-900 truncate">{{ contact.name }}</h3>
        <div class="mt-1 space-y-1">
          <p class="text-sm text-gray-600 flex items-center">
            <PhoneIcon class="w-4 h-4 mr-2 text-gray-400" />
            {{ contact.phone }}
          </p>
          <p v-if="contact.email" class="text-sm text-gray-600 flex items-center">
            <EnvelopeIcon class="w-4 h-4 mr-2 text-gray-400" />
            {{ contact.email }}
          </p>
        </div>
        
        <div v-if="contact.description" class="mt-3 flex flex-wrap gap-1">
          <span class="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
            {{ formatAge(contact.description.age) }}
          </span>
          <span class="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800">
            {{ formatSex(contact.description.sex) }}
          </span>
          <span class="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-purple-100 text-purple-800">
            {{ formatHairColor(contact.description.hairColor) }}
          </span>
        </div>
      </div>
      
      <div class="flex-shrink-0">
        <button
          @click.stop="editContact"
          class="p-2 text-gray-400 hover:text-gray-600 transition-colors"
        >
          <PencilIcon class="w-5 h-5" />
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { UserIcon, PhoneIcon, EnvelopeIcon, PencilIcon } from '@heroicons/vue/24/outline'

export default {
  name: 'ContactCard',
  components: {
    UserIcon,
    PhoneIcon,
    EnvelopeIcon,
    PencilIcon
  },
  props: {
    contact: {
      type: Object,
      required: true
    }
  },
  methods: {
    viewContact() {
      this.$router.push(`/contacts/${this.contact.id}`)
    },
    editContact() {
      this.$router.push(`/edit/${this.contact.id}`)
    },
    formatAge(age) {
      const ageMap = {
        'CHILD': 'Child',
        'ADOLESCENT': 'Teen',
        'YOUNG_ADULT': 'Young Adult',
        'ADULT': 'Adult',
        'ELDER': 'Elder'
      }
      return ageMap[age] || age
    },
    formatSex(sex) {
      return sex === 'MALE' ? 'Male' : 'Female'
    },
    formatHairColor(hairColor) {
      const colorMap = {
        'BLACK': 'Black Hair',
        'BROWN': 'Brown Hair',
        'BLONDE': 'Blonde Hair',
        'GINGER': 'Red Hair',
        'GRAY': 'Gray Hair',
        'WHITE': 'White Hair',
        'PAINTED': 'Colored Hair',
        'NONE': 'No Hair'
      }
      return colorMap[hairColor] || hairColor
    }
  }
}
</script>