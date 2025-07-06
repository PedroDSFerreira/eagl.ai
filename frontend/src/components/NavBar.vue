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

        <!-- Desktop Nav Links -->
        <div class="hidden md:flex items-center space-x-4">
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

        <!-- Mobile Hamburger -->
        <div class="md:hidden flex items-center">
          <Dialog>
            <DialogTrigger as-child>
              <Button variant="ghost" size="icon" aria-label="Open menu">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                </svg>
              </Button>
            </DialogTrigger>
            <DialogContent class="p-0 w-full max-w-xs">
              <div class="flex flex-col gap-4 p-6">
                <router-link
                  to="/"
                  class="text-lg font-medium transition-colors hover:text-primary"
                  :class="{ 'text-primary': $route.name === 'ContactList', 'text-muted-foreground': $route.name !== 'ContactList' }"
                >
                  Contacts
                </router-link>
                <Button as-child class="w-full">
                  <router-link to="/add" class="flex items-center space-x-2 justify-center">
                    <Plus class="h-4 w-4" />
                    <span>Add Contact</span>
                  </router-link>
                </Button>
                <ThemeToggle />
              </div>
            </DialogContent>
          </Dialog>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { Plus } from 'lucide-vue-next'
import Button from '@/components/ui/button.vue'
import ThemeToggle from '@/components/ui/theme-toggle.vue'
import Dialog from '@/components/ui/dialog.vue'
import DialogTrigger from '@/components/ui/dialog-trigger.vue'
import DialogContent from '@/components/ui/dialog-content.vue'
import { ref, onMounted } from 'vue'

const logoSrc = ref('/logo-black.svg')

function updateLogo() {
  logoSrc.value = document.documentElement.classList.contains('dark') ? '/logo-white.svg' : '/logo-black.svg'
}

onMounted(() => {
  updateLogo()
  const observer = new MutationObserver(updateLogo)
  observer.observe(document.documentElement, { attributes: true, attributeFilter: ['class'] })
})
</script>