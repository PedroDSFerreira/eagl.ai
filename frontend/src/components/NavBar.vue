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

        <!-- Desktop Nav Links (unchanged style) -->
        <div class="hidden sm:flex items-center space-x-4">
          <router-link
            to="/"
            class="text-sm font-medium transition-colors hover:text-primary"
            :class="{ 'text-primary': $route.name === 'ContactList', 'text-muted-foreground': $route.name !== 'ContactList' }"
          >
            Contacts
          </router-link>
          <Button as-child>
            <router-link to="/add" class="flex items-center space-x-2">
              <Plus class="h-4 w-4" />
              <span>Add Contact</span>
            </router-link>
          </Button>
          <ThemeToggle />
        </div>

        <!-- Mobile Hamburger (shown below sm) -->
        <div class="sm:hidden flex items-center">
          <Dialog>
            <DialogTrigger as-child>
              <Button variant="ghost" size="icon" aria-label="Open menu">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                </svg>
              </Button>
            </DialogTrigger>
            <DialogContent class="p-0 w-full max-w-xs rounded-lg">
              <div class="flex flex-col gap-2 p-6">
                <DialogClose as-child>
                  <router-link to="/" class="w-full">
                    <Button variant="ghost" class="w-full flex items-center justify-start">
                      <Home class="h-5 w-5 mr-2" />
                      <span>Contacts</span>
                    </Button>
                  </router-link>
                </DialogClose>
                <DialogClose as-child>
                  <router-link to="/add" class="w-full">
                    <Button variant="ghost" class="w-full flex items-center justify-start">
                      <Plus class="h-5 w-5 mr-2" />
                      <span>Add Contact</span>
                    </Button>
                  </router-link>
                </DialogClose>
                <Button variant="ghost" class="w-full flex items-center justify-start" @click="toggleTheme">
                  <component :is="theme === 'light' ? Sun : Moon" class="h-5 w-5 mr-2" />
                  <span>Theme</span>
                </Button>
              </div>
            </DialogContent>
          </Dialog>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { Plus, Home, Sun, Moon } from 'lucide-vue-next'
import Button from '@/components/ui/button.vue'
import ThemeToggle from '@/components/ui/theme-toggle.vue'
import Dialog from '@/components/ui/dialog.vue'
import DialogTrigger from '@/components/ui/dialog-trigger.vue'
import DialogContent from '@/components/ui/dialog-content.vue'
import { DialogClose } from 'radix-vue'
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