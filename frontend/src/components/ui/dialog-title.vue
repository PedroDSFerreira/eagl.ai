<template>
  <DialogTitle
    :class="cn('text-lg font-semibold leading-none tracking-tight', $attrs.class)"
  >
    <slot />
  </DialogTitle>
</template>

<script setup lang="ts">
import { DialogTitle } from 'radix-vue'
import { cn } from '@/lib/utils'
</script>