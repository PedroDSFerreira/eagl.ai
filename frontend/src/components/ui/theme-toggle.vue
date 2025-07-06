<template>
  <Button variant="ghost" class="w-9 px-0" @click="toggleTheme">
    <span class="sr-only">Toggle theme</span>
    <Sun v-if="theme === 'light'" class="h-5 w-5" />
    <Moon v-else class="h-5 w-5" />
  </Button>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import Button from '@/components/ui/button.vue'
import { Sun, Moon } from 'lucide-vue-next'

const theme = ref<'light' | 'dark'>('light')

onMounted(() => {
  theme.value = document.documentElement.classList.contains('dark') ? 'dark' : 'light'
})

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
</script>
