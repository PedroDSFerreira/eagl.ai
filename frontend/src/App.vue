<template>
  <div id="app" class="min-h-screen bg-background">
    <NavBar />
    <main>
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import NavBar from '@/components/NavBar.vue'
</script>