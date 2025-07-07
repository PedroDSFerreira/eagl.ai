<template>
  <nav class="border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 sticky top-0 z-50">
    <div class="container mx-auto px-4">
      <div class="flex h-16 items-center justify-between">
        <div class="flex items-center space-x-2">
          <router-link to="/" class="flex items-center space-x-2 hover:opacity-80 transition-opacity">
            <div class="h-30 w-30 rounded-lg flex items-center justify-center">
              <img
                :src="logoSrc"
                alt="Logo"
                class="h-32 w-32 transition-colors duration-300"
              />
            </div>
          </router-link>
        </div>

        <!-- Desktop & Mobile Nav Links -->
        <div class="flex items-center space-x-4">
          <ThemeToggle />
          <Button as-child>
            <router-link to="/add" class="flex items-center space-x-2">
              <Plus class="h-4 w-4" />
              <span class="hidden sm:inline">Add Contact</span>
            </router-link>
          </Button>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { Plus } from 'lucide-vue-next'
import Button from '@/components/ui/button.vue'
import ThemeToggle from '@/components/ui/theme-toggle.vue'
import { ref, onMounted } from 'vue'

const logoSrc = ref('/logo-black.svg')
const theme = ref<'light' | 'dark'>('light')

function updateLogo() {
  logoSrc.value = document.documentElement.classList.contains('dark') ? '/logo-white.svg' : '/logo-black.svg'
}

function updateTheme() {
  theme.value = document.documentElement.classList.contains('dark') ? 'dark' : 'light'
}

function toggleTheme() {
  if (theme.value === 'dark') {
    document.documentElement.classList.remove('dark')
    theme.value = 'light'
    localStorage.setItem('theme', 'light')
  } else {
    document.documentElement.classList.add('dark')
    theme.value = 'dark'
    localStorage.setItem('theme', 'dark')
  }
}

onMounted(() => {
  updateLogo()
  updateTheme()
  const observer = new MutationObserver(() => {
    updateLogo()
    updateTheme()
  })
  observer.observe(document.documentElement, { attributes: true, attributeFilter: ['class'] })
})
</script>