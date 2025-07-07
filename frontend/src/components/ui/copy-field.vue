<template>
  <div
    class="inline-flex items-center group space-x-3 relative cursor-pointer rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring hover:bg-accent hover:text-accent-foreground px-4 py-2 min-w-0 w-full"
    @mouseenter="showClipboard = true"
    @mouseleave="showClipboard = false"
    @click="copyToClipboard"
    tabindex="0"
  >
    <component :is="iconComponent" class="h-5 w-5 text-muted-foreground flex-shrink-0" />
    <span class="truncate min-w-0">{{ value }}</span>
    <span
      class="absolute right-2 flex items-center opacity-0 group-hover:opacity-100 transition-opacity pr-1"
      v-if="showClipboard || copied"
    >
      <Clipboard class="h-4 w-4 text-muted-foreground" v-if="!copied" />
      <Check class="h-4 w-4 text-green-600" v-else />
    </span>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { Clipboard, Check, Phone, Mail, MapPin } from 'lucide-vue-next'

const props = defineProps<{ value: string, icon: 'Phone' | 'Mail' | 'MapPin' }>()
const copied = ref(false)
const showClipboard = ref(false)

const iconComponent = computed(() => {
  if (props.icon === 'Phone') return Phone
  if (props.icon === 'Mail') return Mail
  if (props.icon === 'MapPin') return MapPin
  return Phone
})

function copyToClipboard() {
  navigator.clipboard.writeText(props.value)
  copied.value = true
  setTimeout(() => (copied.value = false), 1200)
}
</script>

<style scoped>
.group:hover button {
  opacity: 1;
}
</style>
