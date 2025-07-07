<template>
  <Button variant="ghost" class="w-9 px-0" @click="toggleTheme">
    <span class="sr-only">Toggle theme</span>
    <Sun v-if="theme === 'light'" class="h-5 w-5" />
    <Moon v-else class="h-5 w-5" />
  </Button>
</template>

<script setup lang="ts">
import Button from '@/components/ui/button.vue'
import { Sun, Moon } from 'lucide-vue-next'
import { useTheme } from '@/lib/useTheme'

const { theme, toggleTheme } = useTheme()
</script>
