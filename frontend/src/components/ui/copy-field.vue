<template>
<div
  class="inline-flex items-center group space-x-3 relative cursor-pointer rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring hover:bg-accent hover:text-accent-foreground px-4 py-2 min-w-0 w-full"
  @mouseenter="onMouseEnter"
  @mouseleave="onMouseLeave"
  @click="onClick($event)"
  @touchstart="onTouchStart"
  tabindex="0"
>
    <component :is="iconComponent" class="h-5 w-5 text-muted-foreground flex-shrink-0" />
    <span class="truncate min-w-0">{{ value }}</span>
    <span
      class="absolute right-2 flex items-center transition-opacity pr-1"
      :class="{ 'opacity-100': showClipboard || copied, 'opacity-0': !(showClipboard || copied) }"
      v-if="showClipboard || copied"
    >
      <Clipboard class="h-6 w-6 text-muted-foreground" v-if="!copied" />
      <Check class="h-6 w-6 text-green-600" v-else />
    </span>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { Clipboard, Check, Phone, Mail, MapPin, Calendar } from 'lucide-vue-next'

const props = defineProps<{ value: string, icon: 'Phone' | 'Mail' | 'MapPin' | 'Calendar' }>()

const copied = ref(false)
const showClipboard = ref(false)

let copyTimeout: number | null = null

function onMouseEnter() {
  if (!copied.value) showClipboard.value = true
}
function onMouseLeave() {
  if (!copied.value) showClipboard.value = false
}
function onTouchStart() {
  // No-op: let click handle copy for both desktop and mobile
}
function onClick(event?: Event) {
  copyToClipboard(event)
}

const iconComponent = computed(() => {
  if (props.icon === 'Phone') return Phone
  if (props.icon === 'Mail') return Mail
  if (props.icon === 'MapPin') return MapPin
  if (props.icon === 'Calendar') return Calendar
  return Phone
})

function copyToClipboard(event?: Event) {
  navigator.clipboard.writeText(props.value)
  copied.value = true
  showClipboard.value = true
  if (copyTimeout) clearTimeout(copyTimeout)
  copyTimeout = window.setTimeout(() => {
    copied.value = false
    showClipboard.value = false
    // Remove focus/highlight after animation
    if (event && event.target) {
      (event.target as HTMLElement)?.blur?.()
    } else {
      (document.activeElement as HTMLElement)?.blur?.()
    }
  }, 1200)
}
</script>

<style scoped>
.group:hover button {
  opacity: 1;
}
</style>
