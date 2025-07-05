<template>
  <nav class="bg-white shadow-lg fixed top-0 left-0 right-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between items-center h-16">
        <div class="flex items-center">
          <router-link to="/" class="flex items-center space-x-2">
            <div class="w-8 h-8 bg-primary-600 rounded-lg flex items-center justify-center">
              <EyeIcon class="w-5 h-5 text-white" />
            </div>
            <span class="text-xl font-bold text-gray-900">EaglAI</span>
          </router-link>
        </div>
        
        <div class="flex items-center space-x-4">
          <router-link
            to="/"
            class="text-gray-700 hover:text-primary-600 px-3 py-2 rounded-md text-sm font-medium transition-colors"
            :class="{ 'text-primary-600': $route.name === 'ContactList' }"
          >
            Contacts
          </router-link>
          <router-link
            to="/add"
            class="btn-primary text-sm"
          >
            <PlusIcon class="w-4 h-4 mr-1" />
            Add Contact
          </router-link>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import { EyeIcon, PlusIcon } from '@heroicons/vue/24/outline'

export default {
  name: 'NavBar',
  components: {
    EyeIcon,
    PlusIcon
  }
}
</script>