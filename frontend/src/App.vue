<template>
  <div id="app">
    <NavBar />
    <main class="min-h-screen pt-16">
      <router-view />
    </main>
  </div>
</template>

<script>
import NavBar from './components/NavBar.vue'

export default {
  name: 'App',
  components: {
    NavBar
  }
}
</script>