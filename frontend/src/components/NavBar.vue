<template>
  <nav class="border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 sticky top-0 z-50">
    <div class="container mx-auto px-4">
      <div class="flex h-16 items-center justify-between">
        <div class="flex items-center space-x-2">
          <router-link to="/" class="flex items-center space-x-2 hover:opacity-80 transition-opacity">
            <div class="h-8 w-8 bg-primary rounded-lg flex items-center justify-center">
              <Eye class="h-5 w-5 text-primary-foreground" />
            </div>
            <span class="text-xl font-bold">EaglAI</span>
          </router-link>
        </div>
        
        <div class="flex items-center space-x-4">
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
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { Eye, Plus } from 'lucide-vue-next'
import Button from '@/components/ui/button.vue'
</script>